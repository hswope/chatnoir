package com.hms3.chatnoir.server.rest

import com.hms3.chatnoir.server.model.Credentials
import com.hms3.chatnoir.server.repository.UserRepository
import com.hms3.chatnoir.server.model.User

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import javax.ws.rs.*
import javax.ws.rs.core.MediaType

@Component
@Path("/login")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
open class Login {

    @Autowired
    private lateinit var userRepository : UserRepository

    @POST
    fun login(credentials: Credentials) : User {
        val user = userRepository.findByUsername(credentials.username)
        if (user == null || user.password != credentials.password)
            throw WebApplicationException(401)
        return user
    }
}