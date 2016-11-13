package com.hms3.chatnoir.desktopclient.utility

import javafx.application.Platform
import java.util.concurrent.Executor


class JavaFXExecutor : Executor{
    override fun execute(command: Runnable) {
        Platform.runLater(command)
    }
}