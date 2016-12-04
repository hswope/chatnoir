package com.hms3.chatnoir.desktopclient

import com.hms3.chatnoir.desktopclient.utility.SpringFXMLLoader
import com.hms3.chatnoir.desktopclient.utility.StringRes
import javafx.application.Application
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.stage.Stage
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.support.ClassPathXmlApplicationContext


class ChatNoirApplication : Application() {

    lateinit var applicationContext: ClassPathXmlApplicationContext
    lateinit var rootNode : Parent

    companion object {
        val log : Logger = LoggerFactory.getLogger("com.hms3.chatnoir.desktopclient.ChatNoirApplication")

        @JvmStatic fun main(args: Array<String>) {
            log.info("Starting Route Viewer")
            log.info(String.format("Build configuration: ${BuildConfig.name}"))
            Application.launch(ChatNoirApplication::class.java)
        }
    }

    override fun init() {
        applicationContext = ClassPathXmlApplicationContext("spring/default.xml","spring/debug.xml")
        log.info("Spring active profile: ${applicationContext.environment.activeProfiles.joinToString(",")}")
        val fxmlLoader = applicationContext.getBean(SpringFXMLLoader::class.java)
        rootNode = fxmlLoader.load("main.fxml")
    }

    override fun start(stage: Stage?) {
        log.info("Loading UI")
        stage?.title = StringRes.get("App.Title")
        stage?.scene = Scene(rootNode, 800.0, 600.0)
        stage?.show()
    }

    override fun stop() {
        log.info("Qitting Route Viewer")
        super.stop()
        applicationContext.close()
    }
}