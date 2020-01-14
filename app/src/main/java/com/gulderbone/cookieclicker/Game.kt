package com.gulderbone.cookieclicker

import android.os.Handler
import android.os.Looper
import android.widget.TextView
import com.gulderbone.cookieclicker.data.CookieProducer

object Game {
    var score = 0.0
    var cps = 0.0
    var producers = mutableMapOf<CookieProducer, Int>()

    fun startCountingCookies() {
        val mainHandler = Handler(Looper.getMainLooper())

        mainHandler.post(object : Runnable {
            override fun run() {
                score += (cps / 20)
                mainHandler.postDelayed(this, 50)
            }
        })
    }

    fun startUpdatingScoreCounter(counter: TextView) {
        val mainHandler = Handler(Looper.getMainLooper())

        mainHandler.post(object : Runnable {
            override fun run() {
                counter.text = score.toInt().toString()
                mainHandler.postDelayed(this, 50)
            }
        })
    }

    fun recalculateCps() {
        cps = 0.0
        producers.forEach { producer ->
            cps += producer.key.cps * producer.value
        }
    }

    fun updateCpsCounter(counter: TextView) {
        recalculateCps();
        counter.text = cps.toString()
    }
}