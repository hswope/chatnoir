package com.hms3.chatnoir.desktopclient.controller

import com.hms3.chatnoir.desktopclient.service.AuthService
import com.hms3.chatnoir.desktopclient.service.ChatNoirService
import com.hms3.chatnoir.desktopclient.utility.Validator
import javafx.application.Platform
import javafx.event.EventHandler
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.TextField
import javafx.stage.Stage
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.net.URL
import java.util.*

@Component
class LoginController : Initializable {
    val log = LoggerFactory.getLogger(javaClass)

    @Autowired lateinit private var service : ChatNoirService;
    @Autowired lateinit private var authService : AuthService
    @Autowired lateinit private var validator : Validator
    @FXML lateinit private var errorLabel : Label
    @FXML lateinit private var okButton : Button
    @FXML lateinit private var cancelButton : Button
    @FXML lateinit private var usernameTextField : TextField
    @FXML lateinit private var passwordTextField : TextField

    override fun initialize(location: URL?, resources: ResourceBundle?) {

        okButton.onAction = EventHandler{
            if (validate()){
                service.getUsers().handle { users, throwable ->
                    if (users != null)
                        log.info("${users.count()}")
                    else
                        log.info("test failed")
                }
                authService.login(usernameTextField.text, passwordTextField.text).handle { userClient, throwable ->
                    log.info("Handle")
                    if (throwable != null){
                       errorLabel.text = "Failed to login"
                    }
                    else {
                        (okButton.scene.window as Stage).close()
                    }
                }
            }
        }

        cancelButton.onAction = EventHandler {
            (cancelButton.scene.window as Stage).close()
        }

    }

    fun validate() : Boolean {
        if (!validator.isValidUsername(usernameTextField.textProperty(), errorLabel.textProperty()))
            return false;
        if (!validator.isValidPassword(passwordTextField.textProperty(), errorLabel.textProperty()))
            return false;

        return true;
    }

}