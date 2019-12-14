package com.gulderbone.cookieclicker.cookieproducers

import com.gulderbone.cookieclicker.Game

abstract class CookieProducer(val name: String, val cpm: Int, val startingPrice: Int) {

    fun calculatePrice(cookieProducer: CookieProducer): Int {
        var price = startingPrice
        val amount: Int? =
            if (Game.producers.containsKey(cookieProducer)) Game.producers[cookieProducer] else 0

        if (amount != null) {
            for (i in 0 until amount) {
                price = (price * 1.10).toInt()
            }
        }

        return price
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + cpm
        result = 31 * result + startingPrice
        return result
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as CookieProducer

        if (name != other.name) return false
        if (cpm != other.cpm) return false
        if (startingPrice != other.startingPrice) return false

        return true
    }
}