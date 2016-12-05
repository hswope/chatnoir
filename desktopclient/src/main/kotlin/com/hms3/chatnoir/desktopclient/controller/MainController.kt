package com.hms3.chatnoir.desktopclient.controller

import com.hms3.chatnoir.desktopclient.model.Message
import com.hms3.chatnoir.desktopclient.model.User
import com.hms3.chatnoir.desktopclient.service.ChatNoirService
import com.hms3.chatnoir.desktopclient.utility.ImageRes
import com.hms3.chatnoir.desktopclient.utility.SpringFXMLLoader
import com.hms3.chatnoir.desktopclient.utility.StringRes

import javafx.application.Platform
import javafx.event.EventHandler
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.Scene
import javafx.scene.control.*
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.layout.VBox
import javafx.stage.Modality
import javafx.stage.Stage
import javafx.stage.StageStyle
import javafx.stage.WindowEvent
import org.glassfish.jersey.media.sse.EventListener
import org.glassfish.jersey.media.sse.InboundEvent
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.net.URL
import java.util.*
import javax.ws.rs.core.GenericType

@Component
open class MainController : Initializable{
    val log = LoggerFactory.getLogger(javaClass)

    @FXML lateinit private var topContainer : VBox
    @FXML lateinit private var fileLoginMenu : MenuItem
    @FXML lateinit private var loginButton : Button
    @FXML lateinit private var fileExitMenu: MenuItem
    @FXML lateinit private var viewRefreshMenu : MenuItem
    @FXML lateinit private var refreshButton : Button
    @FXML lateinit private var helpAboutMenu : MenuItem
    @FXML lateinit private var aboutButton : Button
    @FXML lateinit private var userTree : TreeView<User>
    @FXML lateinit private var conversationTextArea : TextArea
    @FXML lateinit private var messageTextArea : TextArea
    @FXML lateinit private var messageSubmitButton : Button
    @Autowired lateinit private var fxmlLoader : SpringFXMLLoader
    @Autowired lateinit  private var service : ChatNoirService

    private var aboutDlg : Scene? = null
    private var loginDlg : Scene? = null
    private var loginController : LoginController? = null
    private lateinit var userIcon : Image
    private var conversingWith : User? = null
    private val messagesGenericType = object : GenericType<List<Message>>(){}
    private var lastWrittenSender : User? = null

    override fun initialize(location: URL?, resources: ResourceBundle?) {

        //file menu
        fileLoginMenu.onAction = EventHandler { login() }
        fileExitMenu.onAction = EventHandler { exit() }

        //view menu
        viewRefreshMenu.onAction = EventHandler { refreshView() }

        //help menu
        helpAboutMenu.onAction = EventHandler { showAbout() }

        //button bar
        loginButton.onAction = EventHandler { login() }
        refreshButton.onAction = EventHandler { refreshView() }
        aboutButton.onAction = EventHandler { showAbout() }

        //user tree
        userIcon = ImageRes.get("user.png")
        val rootUser = User(StringRes.get("Tree.Root"))
        userTree.root = TreeItem<User>(rootUser,ImageView(ImageRes.get("userfolder.png")))
        userTree.selectionModel.selectedItemProperty().addListener { observable, oldValue, newValue ->
            converseWith( if (newValue == null) null else (newValue as TreeItem<User>).value)
        }

        // conversation pane
        messageSubmitButton.onAction = EventHandler { submitMessage() }
    }

    private fun exit() {
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

        stage.onHidden = EventHandler<WindowEvent> { we -> if (loginController?.exitStatus == Dialog.ExitStatus.OK) refreshUsers()  }
    }

    private fun refreshView() {
        refreshUsers()
    }

    private fun refreshUsers() {
        // clear existing state
        val mainStage = topContainer.scene.window as Stage
        mainStage.title = StringRes.get("App.Title")
        userTree.root.children.clear()

        // if we are not logged in return
        val loggedInUser = service.loggedInUser ?: return
        mainStage.title = mainStage.title + " - ${loggedInUser.displayname}"

        // get users and update tree
        service.getUsers().handle { users, throwable ->
            if (throwable != null) return@handle
            users.filter {it.id != service.loggedInUser?.id }
                .forEach{
                    userTree.root.children.add(TreeItem<User>(it,ImageView(userIcon)))
                }
        }
    }

    private fun submitMessage() {
        if (service.loggedInUser == null || conversingWith == null) return
        val msg = Message(service.loggedInUser!!.id, conversingWith!!.id, messageTextArea.text)
        displayMessage(msg)
        service.sendMessage(msg)
        messageTextArea.clear()
    }

    private fun converseWith(user : User?) {
        if (conversingWith == user) return

        // clear the existing conversation
        conversationTextArea.clear()
        service.stopListeningForMessage()

        // if it is the root user no real converstion
        if (user == null || user.username == "") {
            conversingWith = null
            return
        }

        //start new converstation
        conversingWith = user
        log.info("Now conversing with ${user.displayname}")

        // listen for messages
        service.listenForMessages(conversingWith!!, EventListener {inboundEvent -> onMessagesEvent(inboundEvent)})
    }

    private fun onMessagesEvent(inboundEvent : InboundEvent){
        var messages : List<Message> = inboundEvent.readData(messagesGenericType)
        for (message in messages) {
            displayMessage(message)
        }
    }

    private fun displayMessage(message : Message){
        val sender = if (message.sender == service.loggedInUser?.id) service.loggedInUser else conversingWith
        if (sender != lastWrittenSender){
            if (!conversationTextArea.text.isEmpty()){
                conversationTextArea.appendText("\n")
            }
            conversationTextArea.appendText("${sender?.displayname}:\n")
            lastWrittenSender = sender
        }
        conversationTextArea.appendText("${message.messageText}\n")
    }
}