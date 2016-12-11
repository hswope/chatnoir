package com.hms3.chatnoir.server.model

import java.util.*
import javax.persistence.*


@Entity
@Table(name = "\"Message\"", schema = "public")
class Message {

    companion object {
        private val idZero = UUID(0,0)

        @JvmStatic
        fun createEmptyMessage() : Message{
            val message = Message()
            message.id = idZero
            message.sender = idZero
            message.receiver = idZero
            message.timesent = 0
            message.messageText = ""
            return message
        }

        @JvmStatic
        fun isEmpty(message : Message) : Boolean{
            return message.id == idZero &&
                    message.sender == idZero &&
                    message.receiver == idZero &&
                    message.timesent == 0L &&
                    message.messageText == ""
        }
    }

    @Id
    @Column(name = "id")
    @org.hibernate.annotations.Type(type="pg-uuid")
    lateinit var id : UUID

    @Column(name = "sender")
    @org.hibernate.annotations.Type(type="pg-uuid")
    lateinit var sender : UUID

    @Column(name = "receiver")
    @org.hibernate.annotations.Type(type="pg-uuid")
    lateinit var receiver : UUID

    @Column(name = "timesent")
    var timesent : Long = 0

    @Column(name = "messageText")
    lateinit var messageText : String

}