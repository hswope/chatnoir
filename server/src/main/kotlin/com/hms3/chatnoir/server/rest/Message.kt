package com.hms3.chatnoir.server.rest

import com.hms3.chatnoir.server.model.ConversationKey
import com.hms3.chatnoir.server.model.Message
import com.hms3.chatnoir.server.model.User
import com.hms3.chatnoir.server.repository.MessageRepository
import org.glassfish.jersey.media.sse.EventOutput
import org.glassfish.jersey.media.sse.OutboundEvent
import org.glassfish.jersey.media.sse.SseFeature
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.awt.Event
import java.util.*
import java.util.concurrent.ArrayBlockingQueue
import java.util.concurrent.BlockingQueue
import java.util.concurrent.ConcurrentHashMap
import javax.ws.rs.*
import javax.ws.rs.core.Context
import javax.ws.rs.core.GenericType
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.SecurityContext

@Component
@Path("/message")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
open class Message {
    val QUEUE_MAX = 10

    @Context
    lateinit var securityContext: SecurityContext

    @Autowired
    lateinit var messageRepository : MessageRepository

    val messageQueues = ConcurrentHashMap<ConversationKey,BlockingQueue<Message>>()

    @POST
    fun addMessage(message : Message) {
        // make sure we only send messages for the caller
        if ((securityContext.userPrincipal as User).id != message.sender) {
            throw WebApplicationException(401)
        }

        // save message to database
        messageRepository.save(message)

        // add to queque if anyone is listening
        val conversationKey = ConversationKey(message.sender,message.receiver)
        val messageQueue = messageQueues[conversationKey]
        messageQueue?.add(message)
    }

    //TODO: fix thread safety issues around queue creation and initial query
    @GET
    @Produces(SseFeature.SERVER_SENT_EVENTS)
    fun getMessagesFromUser(user : User) : EventOutput{
        // we don't want to get messages from ourself
        val callerId = (securityContext.userPrincipal as User).id
        if ( callerId == user.id) {
            throw WebApplicationException(400)
        }

        val eventOutput = EventOutput()
        val converstaionKey = ConversationKey(user.id, callerId)
        Thread(Runnable {
            try {
                val messages = ArrayList<Message>(1)
                val genericType = object : GenericType<List<Message>>(){}

                // send initial query
                val currentMessages = messageRepository.findBySenderAndReceiver(converstaionKey.sender,converstaionKey.receiver)
                val event = OutboundEvent.Builder()
                        .name("messages")
                        .data(genericType,currentMessages)
                        .build()
                eventOutput.write(event)

                // create message queue
                var messageQueue = messageQueues[converstaionKey]
                if (messageQueue == null) {
                    messageQueue = ArrayBlockingQueue<Message>(QUEUE_MAX)
                    messageQueues.put(converstaionKey, messageQueue)
                } else {
                    messageQueue.clear()
                }

                // send incoming message
                while (true){
                    val message = messageQueue.take()
                    messages.clear()
                    messages.add(message)
                    val event = OutboundEvent.Builder()
                            .name("messages")
                            .data(genericType,messages)
                            .build()
                    eventOutput.write(event)
                }
            }
            catch(ex : Exception){
                throw RuntimeException("Error writing event", ex)
            }
            finally {
                messageQueues.remove(converstaionKey)
                eventOutput.close()
            }

        }).start()

        return eventOutput
    }


}