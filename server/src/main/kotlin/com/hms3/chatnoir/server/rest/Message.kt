package com.hms3.chatnoir.server.rest

import com.hms3.chatnoir.server.model.Message
import com.hms3.chatnoir.server.repository.MessageRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import javax.ws.rs.Consumes
import javax.ws.rs.POST
import javax.ws.rs.Path
import javax.ws.rs.Produces
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
        messageRepository.save(message)
    }

}