package com.gulderbone.cookieclicker.activities

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.gulderbone.cookieclicker.Game
import com.gulderbone.cookieclicker.R
import com.gulderbone.cookieclicker.data.CookieProducer
import com.gulderbone.cookieclicker.utilities.BigDecimalAdapter
import com.gulderbone.cookieclicker.utilities.FileHelper.Companion.getTextFromResources
import com.gulderbone.cookieclicker.utilities.FileHelper.Companion.saveTextToFile
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.android.synthetic.main.item_shop.*
import java.math.BigDecimal

class ItemShop : MainActivity() {

    private val moshi = Moshi.Builder()
        .add(BigDecimalAdapter)
        .add(KotlinJsonAdapterFactory())
        .build()

    private lateinit var cookieProducers: Map<String, CookieProducer>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.item_shop)

        cookieProducers = parseCookieProducersToMap(getTextFromResources(application, R.raw.producers_data))
        setupProducers()
        Game.startUpdatingScoreCounter(scoreCounter)
    }

    override fun onResume() {
        super.onResume()
        Game.recalculateCps()
        Game.updateCpsCounter(cpsCounter)
    }

    private fun setupProducers() {
        setupProducer("Grandma", grandmaButton, grandmaCounter)
        setupProducer("Farm", farmButton, farmCounter)
        setupProducer("Mine", mineButton, mineCounter)
        setupProducer("Factory", factoryButton, factoryCounter)
        setupProducer("Bank", bankButton, bankCounter)
        setupProducer("Temple", templeButton, templeCounter)
        setupProducer("Wizard Tower", wizardTowerButton, wizardTowerCounter)
        setupProducer("Shipment", shipmentButton, shipmentCounter)
        setupProducer("Alchemy Lab", alchemyLabButton, alchemyLabCounter)
        setupProducer("Portal", portalButton, portalCounter)
        setupProducer("Time Machine", timeMachineButton, timeMachineCounter)
        setupProducer("Antimatter Condenser", antimatterCondenserButton, antimatterCondenserCounter)
        setupProducer("Chancemaker", chancemakerButton, chancemakerCounter)
    }

    private fun setupProducer(producerName: String, producerButton: Button, counter: TextView) {
        producerButton.setOnClickListener { handlePurchase(producerName, counter) }
        updateProducerCounter(producerName, counter)
    }

    private fun handlePurchase(producerName: String, counter: TextView) {
        val producer = cookieProducers[producerName] ?: CookieProducer(producerName, BigDecimal.ZERO, BigDecimal.ZERO)

        if (enoughCookiesToBuy(producer)) {
            deductCookiesFromScore(producer)
            addProducer(producer)
            updateProducerCounter(producerName, counter)
            Game.recalculateCps()
            Game.updateCpsCounter(cpsCounter)
            saveOwnedProducers()
        } else {
            Toast.makeText(this, "Not enough cookies", Toast.LENGTH_SHORT).show()
        }
    }

    private fun addProducer(cookieProducer: CookieProducer) {
        if (Game.producers.containsKey(cookieProducer)) {
            Game.producers[cookieProducer] = Game.producers[cookieProducer]!!.plus(1)
        } else {
            Game.producers[cookieProducer] = 1
        }
        Log.i("event", "Producer ${cookieProducer.name}} bought, current amount is: ${Game.producers[cookieProducer]}")
    }

    private fun updateProducerCounter(producerName: String, counter: TextView) {
        val producer = cookieProducers[producerName] ?: CookieProducer(producerName, BigDecimal.ZERO, BigDecimal.ZERO)
        counter.text = Game.producers[producer]?.toString() ?: 0.toString()
    }

    private fun enoughCookiesToBuy(cookieProducer: CookieProducer): Boolean {
        val currentProducerPrice = cookieProducer.calculatePrice(cookieProducer)
        if (Game.score < currentProducerPrice) {
            return false
        } else {
            Log.i("event", "$currentProducerPrice cookies spent")
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
