package com.linda.base.utils.file

import android.annotation.SuppressLint
import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import java.io.File


object FileUtil {

    fun getFilePathByUri(context: Context, uri: Uri?): String? {
        if (uri == null) {
            return null
        }
        uri.let {
            if ("content".equals(it.scheme, ignoreCase = true)) {
                val sdkVersion = Build.VERSION.SDK_INT
                return if (sdkVersion >= 19) { // api >= 19
                    getRealPathFromUriAboveApi19(context, it)
                } else { // api < 19
                    getRealPathFromUriBelowAPI19(context, it)
                }
            } else if ("file".equals(it.scheme, ignoreCase = true)) {
                return it.path
            }
        }
        return null
    }

    private fun getRealPathFromUriAboveApi19(context: Context, uri: Uri): String? {
        var filePath: String? = null
        if (DocumentsContract.isDocumentUri(context, uri)) {
            val documentId = DocumentsContract.getDocumentId(uri)
            if ("com.android.providers.media.documents" == uri.authority) {
                val type = documentId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0]
                val id = documentId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1]
                val selection = MediaStore.Images.Media._ID + "=?"
                val selectionArgs = arrayOf(id)
                var contentUri: Uri? = null
                when (type) {
                    "image" -> contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                    "video" -> contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                    "audio" -> contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                }
                filePath = getDataColumn(context, contentUri, selection, selectionArgs)
            } else if ("com.android.providers.downloads.documents" == uri.authority) {
                val contentUri = ContentUris.withAppendedId(
                    Uri.parse("content://downloads/public_downloads"),
                    java.lang.Long.valueOf(documentId)
                )
                filePath = getDataColumn(context, contentUri, null, null)
            } else if ("com.android.externalstorage.documents" == uri.authority) {
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                val type = split[0]
                if ("primary".equals(type, ignoreCase = true)) {
                    filePath = Environment.getExternalStorageDirectory().absolutePath + File.separator + split[1]
                }
            }
        } else if ("content".equals(uri.scheme!!, ignoreCase = true)) {
            filePath = getDataColumn(context, uri, null, null)
        } else if ("file" == uri.scheme) {
            filePath = uri.path
        }
        return filePath
    }

    private fun getRealPathFromUriBelowAPI19(context: Context, uri: Uri): String? {
        return getDataColumn(context, uri, null, null)
    }

    @SuppressLint("Recycle")
    private fun getDataColumn(context: Context, uri: Uri?, selection: String?, selectionArgs: Array<String>?): String? {
        var path: String? = null
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        var cursor: Cursor? = null
        if (uri == null) {
            return null
        }
        try {
            cursor = context.contentResolver.query(uri, projection, selection, selectionArgs, null)
            if (cursor != null && cursor.moveToFirst()) {
                val columnIndex = cursor.getColumnIndexOrThrow(projection[0])
                path = cursor.getString(columnIndex)
            }
        } catch (e: Exception) {
            cursor?.close()
        }
        return path
    }

}