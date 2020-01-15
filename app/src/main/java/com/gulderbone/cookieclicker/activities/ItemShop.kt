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
    private lateinit var cpsCounter: TextView

    private lateinit var grandmaButton: Button
    private lateinit var grandmaCounter: TextView
    private lateinit var farmButton: Button
    private lateinit var farmCounter: TextView
    private lateinit var mineButton: Button
    private lateinit var mineCounter: TextView
    private lateinit var factoryButton: Button
    private lateinit var factoryCounter: TextView
    private lateinit var bankButton: Button
    private lateinit var bankCounter: TextView
    private lateinit var templeButton: Button
    private lateinit var templeCounter: TextView
    private lateinit var wizardTowerButton: Button
    private lateinit var wizardTowerCounter: TextView
    private lateinit var shipmentButton: Button
    private lateinit var shipmentCounter: TextView
    private lateinit var alchemyLabButton: Button
    private lateinit var alchemyLabCounter: TextView
    private lateinit var portalButton: Button
    private lateinit var portalCounter: TextView
    private lateinit var timeMachineButton: Button
    private lateinit var timeMachineCounter: TextView
    private lateinit var antimatterCondenserButton: Button
    private lateinit var antimatterCondenserCounter: TextView
    private lateinit var prismButton: Button
    private lateinit var prismCounter: TextView
    private lateinit var chancemakerButton: Button
    private lateinit var chancemakerCounter: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.item_shop)

        scoreCounter = findViewById(R.id.scoreCounter)
        cpsCounter = findViewById(R.id.cpsCounter)
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
        grandmaButton = findViewById(R.id.grandmaButton)
        grandmaCounter = findViewById(R.id.grandmaCounter)
        setupProducer("Grandma", grandmaButton, grandmaCounter)

        farmButton = findViewById(R.id.farmButton)
        farmCounter = findViewById(R.id.farmCounter)
        setupProducer("Farm", farmButton, farmCounter)

        mineButton = findViewById(R.id.mineButton)
        mineCounter = findViewById(R.id.mineCounter)
        setupProducer("Mine", mineButton, mineCounter)

        factoryButton = findViewById(R.id.factoryButton)
        factoryCounter = findViewById(R.id.factoryCounter)
        setupProducer("Factory", factoryButton, factoryCounter)

        bankButton = findViewById(R.id.bankButton)
        bankCounter = findViewById(R.id.bankCounter)
        setupProducer("Bank", bankButton, bankCounter)

        templeButton = findViewById(R.id.templeButton)
        templeCounter = findViewById(R.id.templeCounter)
        setupProducer("Temple", templeButton, templeCounter)

        wizardTowerButton = findViewById(R.id.wizardTowerButton)
        wizardTowerCounter = findViewById(R.id.wizardTowerCounter)
        setupProducer("Wizard Tower", wizardTowerButton, wizardTowerCounter)

        shipmentButton = findViewById(R.id.shipmentButton)
        shipmentCounter = findViewById(R.id.shipmentCounter)
        setupProducer("Shipment", shipmentButton, shipmentCounter)

        alchemyLabButton = findViewById(R.id.alchemyLabButton)
        alchemyLabCounter = findViewById(R.id.alchemyLabCounter)
        setupProducer("Alchemy Lab", alchemyLabButton, alchemyLabCounter)

        portalButton = findViewById(R.id.portalButton)
        portalCounter = findViewById(R.id.portalCounter)
        setupProducer("Portal", portalButton, portalCounter)

        timeMachineButton = findViewById(R.id.timeMachineButton)
        timeMachineCounter = findViewById(R.id.timeMachineCounter)
        setupProducer("Time Machine", timeMachineButton, timeMachineCounter)

        antimatterCondenserButton = findViewById(R.id.antimatterCondenserButton)
        antimatterCondenserCounter = findViewById(R.id.antimatterCondenserCounter)
        setupProducer("Antimatter Condenser", antimatterCondenserButton, antimatterCondenserCounter)

        prismButton = findViewById(R.id.prismButton)
        prismCounter = findViewById(R.id.prismCounter)
        setupProducer("Prism", prismButton, prismCounter)

        chancemakerButton = findViewById(R.id.chancemakerButton)
        chancemakerCounter = findViewById(R.id.chancemakerCounter)
        setupProducer("Chancemaker", chancemakerButton, chancemakerCounter)
    }

    private fun handlePurchase(producerName: String, counter: TextView) {
        val producer = cookieProducers[producerName] ?: CookieProducer(producerName, 0, 0)

        if (enoughCookiesToBuy(producer)) {
            deductCookiesFromScore(producer)
            addProducer(producer)
            updateProducerCounter(producerName, counter)
            Game.recalculateCps()
            Game.updateCpsCounter(cpsCounter)
            Log.i("cps", "${Game.cps}")
            saveOwnedProducers()
        } else {
            Toast.makeText(this, "Not enough cookies", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupProducer(producerName: String, producerButton: Button, counter: TextView) {
        producerButton.setOnClickListener { handlePurchase(producerName, counter) }
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
        val producer = cookieProducers[producerName] ?: CookieProducer(producerName, 0, 0)
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
