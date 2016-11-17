package com.hms3.chatnoir.desktopclient.service

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider
import com.hms3.chatnoir.desktopclient.model.Pong
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

    fun ping() : CompletableFuture<Pong> {
        return get<Pong>("ping")
    }
}