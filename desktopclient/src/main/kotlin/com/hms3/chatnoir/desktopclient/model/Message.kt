package com.hms3.chatnoir.desktopclient.model

import java.util.*


class Message() {

    constructor(sender : UUID, receiver : UUID, messageText : String) : this(){
        this.id = UUID.randomUUID()
        this.sender = sender
        this.receiver = receiver
        this.messageText = messageText
        this.timesent = System.currentTimeMillis()
    }

    lateinit var id : UUID
    lateinit var sender : UUID
    lateinit var receiver : UUID
    var timesent : Long = 0
    lateinit var messageText : String
}