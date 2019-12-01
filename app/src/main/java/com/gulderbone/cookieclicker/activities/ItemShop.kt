package com.gulderbone.cookieclicker.activities

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.gulderbone.cookieclicker.Game
import com.gulderbone.cookieclicker.R
import com.gulderbone.cookieclicker.cookieproducers.Grandma

class ItemShop : AppCompatActivity() {

    private lateinit var upgradeButton: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.item_shop)
        Game.switchToFullScreen(this)

        upgradeButton = findViewById(R.id.upgrade)

        upgradeButton.setOnClickListener { Game.producerPurchased(Grandma()) }

    }
}