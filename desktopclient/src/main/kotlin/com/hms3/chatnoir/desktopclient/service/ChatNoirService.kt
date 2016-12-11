package com.hms3.chatnoir.desktopclient.service

import com.hms3.chatnoir.desktopclient.model.Credentials
import com.hms3.chatnoir.desktopclient.model.Message
import com.hms3.chatnoir.desktopclient.model.User
import javafx.application.Platform
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature
import org.glassfish.jersey.media.sse.EventListener
import org.glassfish.jersey.media.sse.EventSource
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.util.*
import java.util.concurrent.CompletableFuture
import javax.annotation.PostConstruct
import javax.ws.rs.core.GenericType

@Component
open class ChatNoirService : RestService() {

    @Value("\${chatNoirApiBaseUrl}")
    private lateinit var url: String

    private var eventSource : EventSource? = null

    var loggedInUser : User? = null
        set (value) {
            field = value
            if (value != null)
                baseTarget = baseTarget.register(HttpAuthenticationFeature.basic(value.username, value.password))
            else
                setBaseUrl(url)
        }
        get() {
            return field
        }

    @PostConstruct
    fun postConstruct() {
        setBaseUrl(url)
    }

    fun login(username : String, password : String) : CompletableFuture<User> {
        return post("login", object : GenericType<User>(){}, Credentials(username,password))
    }

    fun getUsers() : CompletableFuture<List<User>> {
        return get("user", object : GenericType<List<User>>(){})
    }

    fun sendMessage(message : Message) : CompletableFuture<Unit> {
        return post("message", object : GenericType<Unit>(){}, message)
    }

    fun listenForMessages(user : User, eventListener: EventListener){
        eventSource?.close()
        val webTarget = baseTarget.path("/message/${user.id}")
        eventSource = EventSource.target(webTarget).build()
        eventSource?.register(EventListener{ inboundEvent -> Platform.runLater {eventListener.onEvent(inboundEvent)}}, "messages")
        eventSource?.open()
    }

    fun stopListeningForMessages(user : User) {
        eventSource?.close()
        eventSource = null
        post("message/${user.id}/close", object : GenericType<Unit>(){}, "")
    }
}