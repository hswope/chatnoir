package com.hms3.chatnoir.server.repository

import com.hms3.chatnoir.server.model.Message
import org.springframework.data.repository.CrudRepository
import java.util.*

interface MessageRepository : CrudRepository<Message, UUID> {
    fun findBySenderAndReceiver(sender : UUID, receiver : UUID) : List<Message>
}
