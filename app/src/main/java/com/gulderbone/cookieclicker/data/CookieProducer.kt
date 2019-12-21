package com.gulderbone.cookieclicker.data

data class CookieProducer (
    val name: String,
    val startingPrice: Int,
    val cpm: Int
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as CookieProducer

        if (name != other.name) return false
        if (startingPrice != other.startingPrice) return false
        if (cpm != other.cpm) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + startingPrice
        result = 31 * result + cpm
        return result
    }
}