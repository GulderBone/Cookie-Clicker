package com.gulderbone.cookieclicker

import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class Game(activity: AppCompatActivity) : AppCompatActivity() {

    private val cookie: ImageView = activity.findViewById(R.id.cookie)
    private val scoreCounter: TextView = activity.findViewById(R.id.scoreCounter)

    companion object {
        var score = 0
    }

    init {
        cookie.setOnClickListener { Cookie.cookieClicked(scoreCounter) }
    }
}