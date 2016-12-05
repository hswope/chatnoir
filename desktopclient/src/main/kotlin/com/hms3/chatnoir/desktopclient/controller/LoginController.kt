package com.hms3.chatnoir.desktopclient.controller

import com.hms3.chatnoir.desktopclient.service.ChatNoirService
import com.hms3.chatnoir.desktopclient.utility.Validator
import javafx.event.EventHandler
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.TextField
import javafx.stage.Stage
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.net.URL
import java.util.*

@Component
open class LoginController : Dialog(), Initializable {

    @Autowired lateinit private var service : ChatNoirService
    @Autowired lateinit private var validator : Validator
    @FXML lateinit private var errorLabel : Label
    @FXML lateinit private var okButton : Button
    @FXML lateinit private var cancelButton : Button
    @FXML lateinit private var usernameTextField : TextField
    @FXML lateinit private var passwordTextField : TextField

    override fun initialize(location: URL?, resources: ResourceBundle?) {

        okButton.onAction = EventHandler{
            if (validate()){
                service.login(usernameTextField.text, passwordTextField.text).handle { user, throwable ->
                    if (throwable == null){
                        service.loggedInUser = user
                        exitStatus = ExitStatus.OK
                        close()
                    }
                    else {
                        service.loggedInUser = null
                        errorLabel.text = "Failed to login"
                    }
                }
            }
        }

        cancelButton.onAction = EventHandler {
            exitStatus = ExitStatus.CANCELED
            close()
        }

    }

    private fun  close() {
        errorLabel.text = ""
        (okButton.scene.window as Stage).close()
    }

    fun validate() : Boolean {
        if (!validator.isValidUsername(usernameTextField.textProperty(), errorLabel.textProperty()))
            return false
        if (!validator.isValidPassword(passwordTextField.textProperty(), errorLabel.textProperty()))
            return false

        return true
    }

}