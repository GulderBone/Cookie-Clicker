package com.gulderbone.cookieclicker.activities

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.gulderbone.cookieclicker.Game
import com.gulderbone.cookieclicker.R
import com.gulderbone.cookieclicker.data.CookieProducer
import com.gulderbone.cookieclicker.utilities.FileHelper.Companion.getTextFromResources
import com.gulderbone.cookieclicker.utilities.FileHelper.Companion.saveTextToFile
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

class ItemShop : MainActivity() {

    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    private lateinit var cookieProducers: Map<String, CookieProducer>
    private lateinit var scoreCounter: TextView
    private lateinit var cpmCounter: TextView

    private lateinit var grandmaButton: Button
    private lateinit var grandmaCounter: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.item_shop)

        scoreCounter = findViewById(R.id.scoreCounter)
        cpmCounter = findViewById(R.id.cpmCounter)
        Game.startUpdatingScoreCounter(scoreCounter)

        cookieProducers = parseCookieProducersToMap(getTextFromResources(application, R.raw.producers_data))

        grandmaButton = findViewById(R.id.grandma)
        grandmaCounter = findViewById(R.id.grandmaCounter)
        setupProducer(grandmaButton, "Grandma", grandmaCounter)
    }

    override fun onResume() {
        super.onResume()
        Game.updateCpmCounter(cpmCounter)
    }

    private fun handlePurchase(producerName: String, counter: TextView) {
        val producer = cookieProducers[producerName] ?: CookieProducer(producerName, 0, 0)

        if (enoughCookiesToBuy(producer)) {
            deductCookiesFromScore(producer)
            addProducer(producer)
            updateProducerCounter(producerName, counter)
            Game.updateCpmCounter(cpmCounter)
            Log.i("cpm", "${Game.cpm}")
            saveOwnedProducers()
        } else {
            Toast.makeText(this, "Not enough cookies", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupProducer(producerButton: Button, producerName: String, counter: TextView) {
        producerButton.setOnClickListener { handlePurchase(producerName, grandmaCounter) }
        updateProducerCounter(producerName, counter)
    }

    private fun addProducer(cookieProducer: CookieProducer) {
        if (Game.producers.containsKey(cookieProducer)) {
            Game.producers[cookieProducer] = Game.producers[cookieProducer]!!.plus(1)
        } else {
            Game.producers[cookieProducer] = 1
        }
    }

    private fun updateProducerCounter(producerName: String, counter: TextView) {
        val producer = cookieProducers[producerName]  ?: CookieProducer(producerName, 0, 0)
        counter.text = Game.producers[producer]?.toString() ?: 0.toString()
    }

    private fun enoughCookiesToBuy(cookieProducer: CookieProducer): Boolean {
        val currentProducerPrice = cookieProducer.calculatePrice(cookieProducer)
        if (Game.score < currentProducerPrice) {
            return false
        } else {
            Log.i("expenses", "$currentProducerPrice cookies spent")
        }
        return true
    }

    private fun deductCookiesFromScore(cookieProducer: CookieProducer) {
        Game.score -= cookieProducer.calculatePrice(cookieProducer)
    }

    private fun parseCookieProducersToMap(text: String): Map<String, CookieProducer> {
        val cookieProducerList = Types.newParameterizedType(
            List::class.java, CookieProducer::class.java
        )
        val jsonAdapter: JsonAdapter<List<CookieProducer>> = moshi.adapter(cookieProducerList)

        return jsonAdapter.fromJson(text)?.map { it.name to it }?.toMap() ?: emptyMap()
    }

    private fun saveOwnedProducers() {
        val ownedProducers = Game.producers.map { it.value.toString() to it.key }.toMap()
        val cookieProducerMap = Types.newParameterizedType(
            Map::class.java, String()::class.java, CookieProducer::class.java
        )
        val jsonAdapter: JsonAdapter<Map<String, CookieProducer>> = moshi.adapter(cookieProducerMap)
        val json = jsonAdapter.toJson(ownedProducers)

        saveTextToFile(application, "producersOwned.json", json)
    }
}
