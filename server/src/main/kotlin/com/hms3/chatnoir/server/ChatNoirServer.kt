package com.hms3.chatnoir.server

import org.glassfish.jersey.jackson.JacksonFeature
import org.glassfish.jersey.server.ResourceConfig
import org.glassfish.jersey.server.ResourceFinder
import org.slf4j.LoggerFactory
import org.springframework.context.support.ClassPathXmlApplicationContext
import javax.ws.rs.core.UriBuilder


class ChatNoirServer {
    val log = LoggerFactory.getLogger(javaClass)
    lateinit var applicationContext: ClassPathXmlApplicationContext

    fun main(args: Array<String>) {
        applicationContext = ClassPathXmlApplicationContext("spring/default.xml","spring/debug.xml")
        log.info("Spring active profile: ${applicationContext.environment.activeProfiles.joinToString(",")}")

        val baseUrl = UriBuilder.fromUri("http://localhost/").port(8080).build()
        val config = ResourceConfig()
                .register(JacksonFeature::class.java)
                .registerFinder()

    }
}