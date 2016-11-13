package com.hms3.chatnoir.desktopclient.model.account

import com.fasterxml.jackson.annotation.JsonProperty
import com.hms3.chatnoir.desktopclient.model.ModelObject


class Credentials() : ModelObject() {

    @JsonProperty("email")
    var username : String? = null

    @JsonProperty("password")
    var password : String? = null

    constructor(username : String, password : String) : this(){
        this.username = username
        this.password = password
    }

}