package com.gulderbone.cookieclicker.data

import com.gulderbone.cookieclicker.Game
import java.math.BigDecimal
import java.math.RoundingMode

data class CookieProducer (
    val name: String,
    val startingPrice: BigDecimal,
    val cps: BigDecimal
) : Comparable<CookieProducer> {
    fun calculatePrice(cookieProducer: CookieProducer): BigDecimal {
        var price = startingPrice
        val amount: Int? =
            if (Game.producers.containsKey(cookieProducer)) Game.producers[cookieProducer] else 0

        if (amount != null) {
            for (i in 0 until amount) {
                price *= BigDecimal(1.10)
            }
        }

        return price.setScale(0, RoundingMode.FLOOR)
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
        result = 31 * result + startingPrice.toInt()
        result = 31 * result + cps.toInt()
        return result
    }

    override fun compareTo(other: CookieProducer): Int {
        return this.cps.toInt() - other.cps.toInt()
    }
}