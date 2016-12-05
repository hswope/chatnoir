package com.hms3.chatnoir.server.model

import java.util.*
import javax.persistence.*


@Entity
@Table(name = "\"Message\"", schema = "public")
class Message {

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