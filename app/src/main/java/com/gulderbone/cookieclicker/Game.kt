package com.gulderbone.cookieclicker

import android.os.Handler
import android.os.Looper
import android.widget.TextView
import com.gulderbone.cookieclicker.cookieproducers.CookieProducer

object Game {
    var score = 0.0
    var cpm = 0.0
    var producers = mutableMapOf<CookieProducer, Int>() //TODO serialize

    fun startCountingCookies(counter: TextView) {
        val mainHandler = Handler(Looper.getMainLooper())

        mainHandler.post(object : Runnable {
            override fun run() {
                score += (cpm / 20)
                counter.text = score.toInt().toString()
                mainHandler.postDelayed(this, 50)
            }
        })
    }

    fun producerPurchased(cookieProducer: CookieProducer) {
        addProducer(cookieProducer)
        recalculateCpm()
        println("Producer ${cookieProducer.name} purchased, current cpm: $cpm")
    }

    private fun addProducer(cookieProducer: CookieProducer) {
        if (producers.containsKey(cookieProducer)) {
            producers[cookieProducer] = producers[cookieProducer]!!.plus(1)
        } else {
            producers[cookieProducer] = 1
        }
    }

    private fun recalculateCpm() {
        cpm = 0.0
        producers.forEach { producer ->
            cpm += producer.key.cpm * producer.value
        }
    }
}