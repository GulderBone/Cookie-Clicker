package com.gulderbone.cookieclicker.activities

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.gulderbone.cookieclicker.Game
import com.gulderbone.cookieclicker.R
import com.gulderbone.cookieclicker.cookieproducers.CookieProducer
import com.gulderbone.cookieclicker.cookieproducers.Grandma

class ItemShop : MainActivity() {

    private lateinit var scoreCounter: TextView
    private lateinit var grandmaButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.item_shop)

        scoreCounter = findViewById(R.id.scoreCounter)
        Game.stareUpdatingScoreCounter(scoreCounter)

        grandmaButton = findViewById(R.id.grandma)
        grandmaButton.setOnClickListener {
            if (enoughCookiesToBuy(Grandma())) {
                deductCookiesFromScore(Grandma())
                addProducer(Grandma())
                recalculateCpm()
                println("Current cpm: ${Game.cpm}")
            } else {
                Toast.makeText(this, "Not enough cookies", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun addProducer(cookieProducer: CookieProducer) {
        if (Game.producers.containsKey(cookieProducer)) {
            Game.producers[cookieProducer] = Game.producers[cookieProducer]!!.plus(1)
        } else {
            Game.producers[cookieProducer] = 1
        }
    }

    private fun recalculateCpm() {
        Game.cpm = 0.0
        Game.producers.forEach { producer ->
            Game.cpm += producer.key.cpm * producer.value
        }
    }

    private fun enoughCookiesToBuy(cookieProducer: CookieProducer): Boolean {
        val currentProducerPrice = cookieProducer.calculatePrice(Grandma())
        if (Game.score < currentProducerPrice) {
            return false
        } else {
            println("$currentProducerPrice cookies spent")
        }
        return true
    }

    private fun deductCookiesFromScore(cookieProducer: CookieProducer) {
        Game.score -= cookieProducer.calculatePrice(Grandma())
    }
}