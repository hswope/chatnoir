package com.hms3.chatnoir.desktopclient.service

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider
import com.hms3.chatnoir.desktopclient.model.Test
import org.glassfish.jersey.client.JerseyClientBuilder
import org.glassfish.jersey.client.JerseyWebTarget
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.util.concurrent.CompletableFuture
import javax.annotation.PostConstruct


@Component
class ChatNoirService : RestService() {

    @Value("\${chatNoirApiBaseUrl}")
    private lateinit var url: String

    @PostConstruct
    fun postConstruct() {
        setBaseUrl(url)
    }

    fun getTest() : CompletableFuture<Test> {

        val baseTarget = JerseyClientBuilder.createClient().register(JacksonJsonProvider::class.java).target(url)
        val request = baseTarget.path("/test").request()
        val test = request.get()

        return get<Test>("/test")
    }
}