package com.hms3.chatnoir.server.rest

import com.hms3.chatnoir.server.model.Message
import com.hms3.chatnoir.server.model.User
import com.hms3.chatnoir.server.repository.MessageRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import javax.ws.rs.*
import javax.ws.rs.core.Context
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.SecurityContext

@Component
@Path("/message")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
open class Message {

    @Context
    lateinit var securityContext: SecurityContext

    @Autowired
    lateinit var messageRepository : MessageRepository

    @POST
    fun addMessage(message : Message) {
        if ((securityContext.userPrincipal as User).id != message.sender) {
            throw WebApplicationException(401)
        }
        messageRepository.save(message)
    }

}