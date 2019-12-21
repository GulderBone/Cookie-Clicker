package com.gulderbone.cookieclicker.utilities

import android.app.Application
import android.content.Context
import java.io.File

class FileHelper {
    companion object {
        fun getTextFromResources(context: Context, resourceId: Int): String {
            return context.resources.openRawResource(resourceId).use {
                it.bufferedReader().use {
                    it.readText()
                }
            }
        }

        fun saveTextToFile(app: Application, fileName: String, json: String?) {
            val file = File(app.filesDir, fileName)
            file.writeText(json ?: "", Charsets.UTF_8)
        }

        fun getTextFromFile(app: Application, fileName: String): String? {
            val file = File(app.filesDir, fileName)
            return if (file.exists()) {
                file.readText()
            } else {
                null
            }
        }
    }
}