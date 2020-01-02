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

    private lateinit var grandmaButton: Button
    private lateinit var farmButton: Button
    private lateinit var mineButton: Button
    private lateinit var factoryButton: Button
    private lateinit var bankButton: Button
    private lateinit var templeButton: Button
    private lateinit var wizardTowerButton: Button
    private lateinit var shipmentButton: Button
    private lateinit var alchemyLabButton: Button
    private lateinit var portalButton: Button
    private lateinit var timeMachineButton: Button
    private lateinit var antimatterCondenserButton: Button
    private lateinit var prismButton: Button
    private lateinit var chancemakerButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.item_shop)

        scoreCounter = findViewById(R.id.scoreCounter)
        Game.stareUpdatingScoreCounter(scoreCounter)

        cookieProducers = parseCookieProducersToMap(getTextFromResources(application, R.raw.producers_data))

        this.grandmaButton = findViewById(R.id.grandma)
        this.grandmaButton.setOnClickListener { handlePurchase("Grandma") }

        this.farmButton = findViewById(R.id.farm)
        this.farmButton.setOnClickListener { handlePurchase("Farm") }

        this.mineButton = findViewById(R.id.mine)
        this.mineButton.setOnClickListener { handlePurchase("Mine") }

        this.factoryButton = findViewById(R.id.factory)
        this.factoryButton.setOnClickListener { handlePurchase("Factory") }

        this.bankButton = findViewById(R.id.bank)
        this.bankButton.setOnClickListener { handlePurchase("Bank") }

        this.templeButton = findViewById(R.id.temple)
        this.templeButton.setOnClickListener { handlePurchase("Temple") }

        this.wizardTowerButton = findViewById(R.id.wizardTower)
        this.wizardTowerButton.setOnClickListener { handlePurchase("Wizard Tower") }

        this.shipmentButton = findViewById(R.id.shipment)
        this.shipmentButton.setOnClickListener { handlePurchase("Shipment") }

        this.alchemyLabButton = findViewById(R.id.alchemyLab)
        this.alchemyLabButton.setOnClickListener { handlePurchase("Alchemy Lab") }

        this.portalButton = findViewById(R.id.portal)
        this.portalButton.setOnClickListener { handlePurchase("Portal") }

        this.timeMachineButton = findViewById(R.id.timeMachine)
        this.timeMachineButton.setOnClickListener { handlePurchase("Time Machine") }

        this.antimatterCondenserButton = findViewById(R.id.antimatterCondenser)
        this.antimatterCondenserButton.setOnClickListener { handlePurchase("Antimatter Condenser") }

        this.prismButton = findViewById(R.id.prism)
        this.prismButton.setOnClickListener { handlePurchase("Prism") }

        this.chancemakerButton = findViewById(R.id.chancemaker)
        this.chancemakerButton.setOnClickListener { handlePurchase("Chancemaker") }
    }

    private fun handlePurchase(producerName: String) {
        val grandma = cookieProducers[producerName] ?: CookieProducer("Not found", 0, 0)

        if (enoughCookiesToBuy(grandma)) {
            deductCookiesFromScore(grandma)
            addProducer(grandma)
            Game.recalculateCps()
            Log.i("cps", "${Game.cps}")
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
