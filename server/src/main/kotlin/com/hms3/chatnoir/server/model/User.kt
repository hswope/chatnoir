package com.hms3.chatnoir.server.model

import com.fasterxml.jackson.annotation.JsonIgnore
import java.security.Principal
import java.util.*
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "\"User\"", schema = "public")
class User : Principal{

    @Id
    @Column(name = "id")
    @org.hibernate.annotations.Type(type="pg-uuid")
    lateinit var id : UUID

    @Column(name = "username")
    lateinit var username : String

    @Column(name = "password")
    lateinit var password : String

    @Column(name = "displayname")
    lateinit var displayname : String

    @JsonIgnore
    override fun getName(): String {
        return username
    }
}