package com.hms3.chatnoir.server.repository

import com.hms3.chatnoir.server.model.User
import org.springframework.data.repository.CrudRepository
import java.util.*


interface UserRepository : CrudRepository<User,UUID> {

    fun findByUsername(username : String) : User?
}