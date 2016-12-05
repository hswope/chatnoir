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
import java.util.*
import java.util.concurrent.ArrayBlockingQueue
import java.util.concurrent.BlockingQueue
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.TimeUnit
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
    val POLL_MS = 2000L

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
    @Path("/{userId}")
    @Produces(SseFeature.SERVER_SENT_EVENTS)
    fun getMessagesFromUser(@PathParam("userId") userId : UUID) : EventOutput{
        // we don't want to get messages from ourself
        val callerId = (securityContext.userPrincipal as User).id
        if ( callerId == userId) {
            throw WebApplicationException(400)
        }

        val eventOutput = EventOutput()
        val conversationKey = ConversationKey(userId, callerId)
        Thread(Runnable {
            try {
                val messages = ArrayList<Message>(1)
                val genericType = object : GenericType<List<Message>>(){}

                // send initial query
                var currentMessages = messageRepository.findBySenderAndReceiver(conversationKey.sender,conversationKey.receiver)
                messages.addAll(currentMessages)
                currentMessages = messageRepository.findBySenderAndReceiver(conversationKey.receiver,conversationKey.sender)
                messages.addAll(currentMessages)
                messages.sort { message1, message2 ->
                    if (message1.timesent < message2.timesent) -1
                    else if (message1.timesent > message2.timesent) 1
                    else 0
                }
                if (messages.size > 0) {
                    val currentEvent = OutboundEvent.Builder()
                            .name("messages")
                            .mediaType(MediaType.APPLICATION_JSON_TYPE)
                            .data(genericType, messages)
                            .build()
                    eventOutput.write(currentEvent)
                }

                // create message queue
                var messageQueue = messageQueues[conversationKey]
                if (messageQueue == null) {
                    messageQueue = ArrayBlockingQueue<Message>(QUEUE_MAX)
                    messageQueues.put(conversationKey, messageQueue)
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
                            .mediaType(MediaType.APPLICATION_JSON_TYPE)
                            .data(genericType,messages)
                            .build()
                    eventOutput.write(event)
                }
            }
            catch(ex : Exception){
                throw RuntimeException("Error writing event", ex)
            }
            finally {
                messageQueues.remove(conversationKey)
                eventOutput.close()
            }

        }).start()

        return eventOutput
    }


}