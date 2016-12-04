package com.hms3.chatnoir.server.utility

import java.util.*

class StringRes {

    companion object {
        private val bundle = ResourceBundle.getBundle("strings.strings", Locale.getDefault(), ClassLoader.getSystemClassLoader())

        fun get(key : String ): String {
            return bundle.getString(key)
        }
    }
}