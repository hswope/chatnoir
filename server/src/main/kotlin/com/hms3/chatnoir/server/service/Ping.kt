package com.hms3.chatnoir.server.service

import com.hms3.chatnoir.server.model.Pong
import org.springframework.stereotype.Component
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType

@Component
@Path("/ping")
@Produces(MediaType.APPLICATION_JSON)
class Ping {

    @GET
    fun get() : Pong {
        return Pong()
    }
}