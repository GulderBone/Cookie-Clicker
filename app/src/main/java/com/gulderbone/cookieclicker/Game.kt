package com.gulderbone.cookieclicker

import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.TextView
import com.gulderbone.cookieclicker.data.CookieProducer
import java.math.BigDecimal

object Game {
    var score = BigDecimal.ZERO
    var cps = BigDecimal.ZERO
    var producers = mutableMapOf<CookieProducer, Int>()

    fun startCountingCookies() {
        val mainHandler = Handler(Looper.getMainLooper())

        mainHandler.post(object : Runnable {
            override fun run() {
                score += (cps / 20.toBigDecimal())
                mainHandler.postDelayed(this, 50)
            }
        })
    }

    fun startUpdatingScoreCounter(counter: TextView) {
        val mainHandler = Handler(Looper.getMainLooper())

        mainHandler.post(object : Runnable {
            override fun run() {
                counter.text = score.toPlainString()
                mainHandler.postDelayed(this, 50)
            }
        })
    }

    fun recalculateCps() {
        cps = BigDecimal.ZERO
        producers.forEach { producer ->
            cps += producer.key.cps * producer.value.toBigDecimal()
        }
        Log.i("cps", "New cps is: $cps")
    }

    fun updateCpsCounter(counter: TextView) {
        counter.text = cps.toPlainString()
    }
}