package com.linda.base.component.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.PowerManager
import android.os.PowerManager.WakeLock
import android.provider.MediaStore
import android.view.View
import android.view.WindowManager
import androidx.core.content.FileProvider
import com.linda.base.utils.file.FileUtil
import java.io.File

/*************************** full screen / keep awake *********************************/

fun Activity.setFullScreen(fullScreen: Boolean) {
    if (fullScreen) {
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        val params = window.attributes
        params.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        window.attributes = params
    } else {
        window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
    }
}

fun Activity.isFullScreen(): Boolean {
    return window.attributes.flags and WindowManager.LayoutParams.FLAG_FULLSCREEN == WindowManager.LayoutParams.FLAG_FULLSCREEN
}

var wakeLock: WakeLock? = null

fun Activity.setScreenAlwaysWaked(alwaysWaked: Boolean) {
    if (alwaysWaked) {
        val powerManager = application.getSystemService(Context.POWER_SERVICE) as PowerManager
        wakeLock = powerManager.newWakeLock(
            PowerManager.SCREEN_BRIGHT_WAKE_LOCK or PowerManager.ON_AFTER_RELEASE,
            this.toString()
        )
        wakeLock?.acquire(10 * 60 * 1000L /*10 minutes*/)
    } else {
        if (wakeLock != null && wakeLock?.isHeld == true) {
            wakeLock?.release()
        }
        wakeLock = null
    }
}


/*********************************** Common Request **********************************/

const val RC_CHOOSE_PHOTO = 101
const val RC_TAKE_PHOTO = 102

fun Activity.choosePhoto() {
    val intentToPickPic = Intent(Intent.ACTION_PICK, null)
    intentToPickPic.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")
    startActivityForResult(intentToPickPic, RC_CHOOSE_PHOTO)
}

fun Activity.getChoosePhotoResult(requestCode: Int, resultCode: Int, data: Intent?): String? {
    if (resultCode == Activity.RESULT_OK) {
        when (requestCode) {
            RC_CHOOSE_PHOTO -> {
                val uri = data?.data
                return FileUtil.getFilePathByUri(this, uri)
            }
        }
    }
    return null
}

fun Activity.takePhoto(path: String) {
    val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
    val file = File(path)
    if (file.parentFile?.exists() == false) {
        file.parentFile?.mkdirs()
    }
    val uri: Uri
    uri = if (Build.VERSION.SDK_INT >= 24) {
        FileProvider.getUriForFile(this, "com.linda.base.fileprovider", file)
    } else {
        Uri.fromFile(file)
    }
    intent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
    startActivityForResult(intent, RC_TAKE_PHOTO)
}

fun Activity.getTakePhotoResult(requestCode: Int, resultCode: Int, data: Intent?): Boolean {
    if (resultCode == Activity.RESULT_OK) {
        when (requestCode) {
            RC_TAKE_PHOTO -> {
                return true
            }
        }
    }
    return false
}