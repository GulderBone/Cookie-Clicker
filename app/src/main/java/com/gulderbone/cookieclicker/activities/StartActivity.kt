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
import com.gulderbone.cookieclicker.data.CookieProducer
import com.gulderbone.cookieclicker.utilities.FileHelper.Companion.getTextFromFile
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

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
        retrieveOwnedProducers()
        Game.recalculateCpm()
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
        val sharedPreferencesEditor =
            this.getSharedPreferences("com.gulderbone.cookieclicker.prefs", 0).edit()

        mainHandler.post(object : Runnable {
            override fun run() {
                sharedPreferencesEditor.putInt("score", Game.score.toInt())
                sharedPreferencesEditor.apply()
                mainHandler.postDelayed(this, 25)
            }
        })
    }

    private fun retrieveScore() {
        val sharedPreferences = this.getSharedPreferences("com.gulderbone.cookieclicker.prefs", 0)
        Game.score = sharedPreferences.getInt("score", 0).toDouble()
    }

    private fun retrieveOwnedProducers() {
        val json = getTextFromFile(application, "producersOwned.json")
        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
        val cookieProducerMap = Types.newParameterizedType(
            Map::class.java,
            String::class.java,
            CookieProducer::class.java
        )
        val adapter: JsonAdapter<Map<String, CookieProducer>> = moshi.adapter(cookieProducerMap)
        val producers = if (json != null) adapter.fromJson(json) else emptyMap()
        Game.producers = producers!!.map { it.value to it.key.toInt() }.toMap().toMutableMap()
    }
}
