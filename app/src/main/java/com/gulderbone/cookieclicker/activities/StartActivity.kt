package com.gulderbone.cookieclicker.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
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
        retrieveScore()
        Game.startCountingCookies()
        Game.stareUpdatingScoreCounter(scoreCounter)
        startSavingScore()
    }

    private fun cookieClicked() {
        Game.score++
        scoreCounter.text = Game.score.toInt().toString()
    }


    private fun openShop() {
        val intent = Intent(applicationContext, ItemShop::class.java)
        startActivity(intent)
    }

    private fun startSavingScore() {
        val mainHandler = Handler(Looper.getMainLooper())
        val sharedPreferencesEditor = this.getSharedPreferences("com.gulderbone.cookieclicker.prefs", 0).edit()

        mainHandler.post(object : Runnable {
            override fun run() {
                sharedPreferencesEditor.putInt("score", Game.score.toInt())
                sharedPreferencesEditor.apply()
                mainHandler.postDelayed(this, 50)
            }
        })
    }

    private fun retrieveScore() {
        val sharedPreferences = this.getSharedPreferences("com.gulderbone.cookieclicker.prefs", 0)
        Game.score = sharedPreferences.getInt("score", 0).toDouble()
    }
}
