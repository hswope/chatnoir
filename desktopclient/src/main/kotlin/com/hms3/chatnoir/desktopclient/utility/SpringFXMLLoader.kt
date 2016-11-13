package com.hms3.chatnoir.desktopclient.utility

import javafx.fxml.FXMLLoader
import javafx.util.Callback
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Component
import java.util.*

@Component
@Scope("prototype")
class SpringFXMLLoader : FXMLLoader(), ApplicationContextAware{
    val pathBase = "fxml/"

    fun <T>load(path : String) : T {
        setRoot(null)
        location = ClassLoader.getSystemResource(pathBase + path)
        this.resources = ResourceBundle.getBundle("strings.strings", Locale.getDefault())
        return load()
    }

    override fun setApplicationContext(applicationContext: ApplicationContext?) {
        controllerFactory = Callback{clazz -> applicationContext?.getBean(clazz)}
    }
}