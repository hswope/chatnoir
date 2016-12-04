package com.hms3.chatnoir.desktopclient.controller


abstract class Dialog {
    enum class ExitStatus {NONE,OK,CANCELED}

    var exitStatus : ExitStatus = ExitStatus.NONE
        protected set(value) { field = value}
}