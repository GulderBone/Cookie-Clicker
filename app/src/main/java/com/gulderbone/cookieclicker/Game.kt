package com.gulderbone.cookieclicker

import android.os.Handler
import android.os.Looper
import android.widget.TextView
import com.gulderbone.cookieclicker.data.CookieProducer

object Game {
    var score = 0.0
    var cpm = 0.0
    var producers = mutableMapOf<CookieProducer, Int>() //TODO serialize

    fun startCountingCookies() {
        val mainHandler = Handler(Looper.getMainLooper())

        mainHandler.post(object : Runnable {
            override fun run() {
                score += (cpm / 20)
                mainHandler.postDelayed(this, 50)
            }
        })
    }

    fun stareUpdatingScoreCounter(counter: TextView) {
        val mainHandler = Handler(Looper.getMainLooper())

        mainHandler.post(object : Runnable {
            override fun run() {
                counter.text = score.toInt().toString()
                mainHandler.postDelayed(this, 50)
            }
        })
    }

    fun recalculateCpm() {
        cpm = 0.0
        producers.forEach { producer ->
            cpm += producer.key.cpm * producer.value
        }
    }
}