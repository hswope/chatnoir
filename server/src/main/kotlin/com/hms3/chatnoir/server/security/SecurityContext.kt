package com.hms3.chatnoir.server.security

import com.hms3.chatnoir.server.model.User
import java.security.Principal


class SecurityContext(val user : User) :  javax.ws.rs.core.SecurityContext{

    override fun isUserInRole(role: String?): Boolean {
        return false
    }

    override fun getAuthenticationScheme(): String {
        return javax.ws.rs.core.SecurityContext.BASIC_AUTH
    }

    override fun getUserPrincipal(): Principal {
        return user
    }

    override fun isSecure(): Boolean {
        return false;
    }
}