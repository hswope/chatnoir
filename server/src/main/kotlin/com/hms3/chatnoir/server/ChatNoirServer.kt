package com.hms3.chatnoir.server

import com.hms3.chatnoir.server.utility.JulFacade
import org.eclipse.jetty.server.Server
import org.glassfish.jersey.jackson.JacksonFeature
import org.glassfish.jersey.jetty.JettyHttpContainerFactory
import org.glassfish.jersey.server.ResourceConfig
import org.slf4j.LoggerFactory
import org.springframework.context.support.ClassPathXmlApplicationContext
import javax.ws.rs.core.UriBuilder
import org.glassfish.jersey.logging.LoggingFeature
import com.sun.xml.internal.ws.spi.db.BindingContextFactory.LOGGER
import org.slf4j.bridge.SLF4JBridgeHandler


class ChatNoirServer {

    companion object {
        val log = LoggerFactory.getLogger("com.hms3.chatnoir.server.ChatNoirServer")
        lateinit var applicationContext: ClassPathXmlApplicationContext

        @JvmStatic fun main(args: Array<String>) {
            applicationContext = ClassPathXmlApplicationContext("spring/default.xml", "spring/debug.xml")

            SLF4JBridgeHandler.install()
            val logLevel = applicationContext.beanFactory.resolveEmbeddedValue("\${loglevel}")
            System.setProperty(org.slf4j.impl.SimpleLogger.DEFAULT_LOG_LEVEL_KEY, logLevel);

            log.info("Spring active profile: ${applicationContext.environment.activeProfiles.joinToString(",")}")

            val base = applicationContext.beanFactory.resolveEmbeddedValue("\${Server.BaseUrl}")
            val port = applicationContext.beanFactory.resolveEmbeddedValue("\${Server.Port}").toInt()
            val baseUrl = UriBuilder.fromUri(base).port(port).build()
            val config = ResourceConfig()
                    .register(JacksonFeature::class.java)
                    .register(LoggingFeature(JulFacade("JerseyLogger"), LoggingFeature.Verbosity.PAYLOAD_ANY))
                    .packages("com.hms3.chatnoir.server.service")
                    .property("contextConfig", applicationContext)

            val server: Server = JettyHttpContainerFactory.createServer(baseUrl, config, false)
            server.start()
            server.join()
        }
    }

}