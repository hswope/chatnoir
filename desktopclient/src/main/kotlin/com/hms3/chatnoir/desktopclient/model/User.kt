package com.hms3.chatnoir.desktopclient.model

import java.util.*

class User(){

    constructor(displayname : String) : this(){
        id = UUID.randomUUID()
        username = ""
        password = ""
        this.displayname = displayname
    }

    lateinit var id : UUID

    lateinit var username : String

    lateinit var password : String

    lateinit var displayname : String

    override fun toString(): String {
        return displayname
    }

    override fun equals(other: Any?): Boolean {
        if (other !is User) return false
        return id == (other as User).id
    }
}
