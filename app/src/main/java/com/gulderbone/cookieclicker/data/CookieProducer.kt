package com.gulderbone.cookieclicker.data

import com.gulderbone.cookieclicker.Game

data class CookieProducer (
    val name: String,
    val startingPrice: Int,
    val cps: Int
) {
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

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as CookieProducer

        if (name != other.name) return false
        if (startingPrice != other.startingPrice) return false
        if (cps != other.cps) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + startingPrice
        result = 31 * result + cps
        return result
    }
}