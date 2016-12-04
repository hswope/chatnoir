package com.hms3.chatnoir.desktopclient.service

import com.hms3.chatnoir.desktopclient.model.account.Credentials
import com.hms3.chatnoir.desktopclient.model.account.User
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.util.concurrent.CompletableFuture
import javax.annotation.PostConstruct
import javax.ws.rs.core.GenericType

@Component
open class ChatNoirService : RestService() {

    @Value("\${chatNoirApiBaseUrl}")
    private lateinit var url: String

    var loggedInUser : User? = null
        set (value) {
            field = value
            if (value != null)
                baseTarget = baseTarget.register(HttpAuthenticationFeature.basic(value.username, value.password))
            else
                setBaseUrl(url)
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
}