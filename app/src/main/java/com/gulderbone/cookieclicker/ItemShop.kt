package com.gulderbone.cookieclicker

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class ItemShop : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.item_shop)
        Game.switchToFullScreen(this)

    }
}