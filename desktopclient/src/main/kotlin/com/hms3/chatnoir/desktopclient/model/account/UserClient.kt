package com.hms3.chatnoir.desktopclient.model.account

import com.fasterxml.jackson.annotation.JsonProperty
import com.hms3.chatnoir.desktopclient.model.ModelObject


class UserClient : ModelObject(){

    @JsonProperty("id")
    var id : Int? = null

    @JsonProperty("email")
    var email : String? = null

    @JsonProperty("client_id")
    var clientId : String? = null

    @JsonProperty("client_secret")
    var clientSecret : String? = null

}