package com.hms3.chatnoir.server

import com.hms3.chatnoir.server.utility.JulFacade
import org.glassfish.jersey.jackson.JacksonFeature
import org.glassfish.jersey.jetty.JettyHttpContainerFactory
import org.glassfish.jersey.server.ResourceConfig
import org.slf4j.LoggerFactory
import org.springframework.context.support.ClassPathXmlApplicationContext
import javax.ws.rs.core.UriBuilder
import org.glassfish.jersey.logging.LoggingFeature

class ChatNoirServer {

    companion object {

        @JvmStatic fun main(args: Array<String>) {

            //create spring context
            val applicationContext = ClassPathXmlApplicationContext("spring/default.xml", "spring/debug.xml")

            // log spring info
            val log = LoggerFactory.getLogger("com.hms3.chatnoir.server.ChatNoirServer")
            log.info("Spring active profile: ${applicationContext.environment.activeProfiles.joinToString(",")}")

            // set up server
            val base = applicationContext.beanFactory.resolveEmbeddedValue("\${Server.BaseUrl}")
            val port = applicationContext.beanFactory.resolveEmbeddedValue("\${Server.Port}").toInt()
            val baseUrl = UriBuilder.fromUri(base).port(port).build()
            val config = ResourceConfig()
                    .register(JacksonFeature::class.java)
                    .register(LoggingFeature(JulFacade("JerseyLogger"), LoggingFeature.Verbosity.PAYLOAD_ANY))
                    .packages("com.hms3.chatnoir.server.service")
                    .property("contextConfig", applicationContext)

            // launch server and wait for exit
            JettyHttpContainerFactory.createServer(baseUrl, config).join()
        }
    }

}