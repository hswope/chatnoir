package com.hms3.chatnoir.server.service

import com.hms3.chatnoir.server.model.Test
import org.springframework.stereotype.Component
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType

@Component
@Path("/test")
@Produces(MediaType.APPLICATION_JSON)
class Test {

    @GET
    fun get() : Test {
        return Test()
    }
}