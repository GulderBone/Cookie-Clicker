package com.gulderbone.cookieclicker.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.gulderbone.cookieclicker.Game
import com.gulderbone.cookieclicker.R

class StartActivity : MainActivity() {

    private lateinit var cookie: ImageView
    private lateinit var scoreCounter: TextView
    private lateinit var shopButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        startNewGame()

        cookie.setOnClickListener { cookieClicked() }
        shopButton.setOnClickListener { openShop() }
    }

    private fun startNewGame() {
        cookie = findViewById(R.id.cookie)
        scoreCounter = findViewById(R.id.scoreCounter)
        shopButton = findViewById(R.id.openItemShopButton)
        Game.startCountingCookies(scoreCounter)
    }

    private fun cookieClicked() {
        Game.score++
        scoreCounter.text = Game.score.toInt().toString()
    }

    private fun openShop() {
        val intent = Intent(applicationContext, ItemShop::class.java)
        startActivity(intent)
    }
}
