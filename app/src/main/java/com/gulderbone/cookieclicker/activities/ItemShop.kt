package com.gulderbone.cookieclicker.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.gulderbone.cookieclicker.Game
import com.gulderbone.cookieclicker.R

class ItemShop : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.item_shop)
        Game.switchToFullScreen(this)

    }
}