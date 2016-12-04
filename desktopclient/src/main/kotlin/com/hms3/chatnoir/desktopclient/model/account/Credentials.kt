package com.hms3.chatnoir.desktopclient.model.account


class Credentials(){

    var username : String? = null
    var password : String? = null

    constructor(username : String, password : String) : this(){
        this.username = username
        this.password = password
    }

}