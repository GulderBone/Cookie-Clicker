package com.gulderbone.cookieclicker

import android.os.Handler
import android.os.Looper
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.gulderbone.cookieclicker.cookieproducers.CookieProducer

object Game {
    var score = 0.0
    var cpm = 0.0
    var producers = mutableMapOf<CookieProducer, Int>()

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

    fun switchToFullScreen(activity: AppCompatActivity) {
        activity.supportActionBar?.hide()
        activity.window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
    }

    fun producerPurchased(cookieProducer: CookieProducer, activity: AppCompatActivity) {
        if (deductCookiesFromScore(cookieProducer)) {
            addProducer(cookieProducer)
            recalculateCpm()
            println("Current cpm: $cpm")
        } else {
            val toast = Toast.makeText(activity, "Not enough cookies", Toast.LENGTH_SHORT)
            toast.show()
        }
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

    private fun deductCookiesFromScore(cookieProducer: CookieProducer): Boolean {
        val amount = if (producers.containsKey(cookieProducer)) producers[cookieProducer] else 0
        val currentProducerPrice = cookieProducer.calculatePrice(amount!!)
        if (score < currentProducerPrice) {
            return false
        } else {
            score -= currentProducerPrice
            println("$currentProducerPrice cookies spent")
        }
        return true
    }
}