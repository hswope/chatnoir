package com.hms3.chatnoir.server.rest

import com.hms3.chatnoir.server.repository.UserRepository
import com.hms3.chatnoir.server.model.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import javax.ws.rs.Consumes
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.Context
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.SecurityContext


@Component
@Path("/user")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
open class User {

    @Context
    lateinit var securityContext: SecurityContext

    @Autowired
    lateinit var userRepository : UserRepository

    @GET
    fun get() : Iterable<User> {
        val users = userRepository.findAll()
        users.forEach { it.password = "********" }
//        for (user in users) {
//            user.password = "********"
//        }
        return users
    }
}