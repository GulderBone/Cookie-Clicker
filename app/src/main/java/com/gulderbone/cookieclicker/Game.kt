package com.gulderbone.cookieclicker

import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity

class Game {
    companion object {
        var score = 0

        fun switchToFullScreen(activity: AppCompatActivity) {
            activity.supportActionBar?.hide()
            activity.window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
    }
}