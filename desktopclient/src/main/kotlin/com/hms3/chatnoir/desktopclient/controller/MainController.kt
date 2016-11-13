package com.hms3.chatnoir.desktopclient.controller

import com.hms3.chatnoir.desktopclient.utility.SpringFXMLLoader
import com.hms3.chatnoir.desktopclient.utility.StringRes
import javafx.application.Platform
import javafx.event.EventHandler
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.MenuItem
import javafx.stage.Modality
import javafx.stage.Stage
import javafx.stage.StageStyle
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.net.URL
import java.util.*

@Component
class MainController : Initializable{
    val log = LoggerFactory.getLogger(javaClass)

    @FXML lateinit private var fileLoginMenu : MenuItem
    @FXML lateinit private var fileCloseMenu : MenuItem
    @FXML lateinit private var helpAboutMenu : MenuItem
    @FXML lateinit private var aboutButton : Button
    @Autowired lateinit private var fxmlLoader : SpringFXMLLoader

    private var aboutDlg : Scene? = null
    private var loginDlg : Scene? = null
    private var loginController : LoginController? = null

    override fun initialize(location: URL?, resources: ResourceBundle?) {

        /////////////////////////////////////////////
        //region File Menu
        /////////////////////////////////////////////
        fileLoginMenu.onAction = EventHandler { login() }
        fileCloseMenu.onAction = EventHandler { close() }
        // endregion

        /////////////////////////////////////////////
        //region Help Menu
        /////////////////////////////////////////////
        helpAboutMenu.onAction = EventHandler { showAbout() }
        // endregion

        /////////////////////////////////////////////
        //region Button Bar
        /////////////////////////////////////////////
        aboutButton.onAction = EventHandler { showAbout() }
        //endregion
    }

    private fun close() {
        Platform.exit()
    }

    private fun login() {
        showLogin()
    }

    private fun showAbout() {
        if (aboutDlg == null) {
            aboutDlg = Scene(fxmlLoader.load("dialog_about.fxml"))
        }
        val stage = Stage()
        stage.initModality(Modality.APPLICATION_MODAL)
        stage.initStyle(StageStyle.UTILITY)
        stage.title = StringRes.get("About.title")
        stage.scene = aboutDlg
        stage.show()
    }

    private fun showLogin() {
        if (loginDlg == null){
            loginDlg = Scene(fxmlLoader.load("dialog_login.fxml"))
            loginController = fxmlLoader.getController()
        }
        val stage = Stage()
        stage.initModality(Modality.APPLICATION_MODAL)
        stage.initStyle(StageStyle.UTILITY)
        stage.title = StringRes.get("Login.title")
        stage.scene = loginDlg
        stage.show()
        log.info("exited")
    }
}