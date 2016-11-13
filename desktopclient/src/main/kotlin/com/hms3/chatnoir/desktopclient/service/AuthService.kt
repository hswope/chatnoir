package com.hms3.chatnoir.desktopclient.service

import com.hms3.chatnoir.desktopclient.model.account.Credentials
import com.hms3.chatnoir.desktopclient.model.account.UserClient
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component
import java.util.concurrent.CompletableFuture


@Component
class AuthService {


    fun login(username : String, password : String) : CompletableFuture<UserClient> {
        return CompletableFuture()
    }
}