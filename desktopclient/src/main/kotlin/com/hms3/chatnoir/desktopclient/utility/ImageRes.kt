package com.hms3.chatnoir.desktopclient.utility

import javafx.scene.image.Image

class ImageRes {
    companion object {
        fun get(key : String) : Image {
            return Image(ClassLoader.getSystemResourceAsStream("images/$key"))
        }
    }
}
