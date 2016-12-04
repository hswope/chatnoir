package com.hms3.chatnoir.desktopclient.utility

import javafx.beans.property.StringProperty
import org.springframework.stereotype.Component

@Component
open class Validator {

    fun isValidUsername(username : StringProperty , error : StringProperty) : Boolean {
        //TODO: Implement real validation
        if (false) {
            error.set(StringRes.get("Error.InvalidUsername"))
            return false
        }
        return true
    }

    fun isValidPassword(password : StringProperty , error : StringProperty) : Boolean {
        //TODO: Implement real validation
        if (false) {
            error.set(StringRes.get("Error.InvalidPassword"))
            return false
        }
        return true
    }
}