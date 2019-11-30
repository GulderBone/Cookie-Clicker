package com.gulderbone.cookieclicker.cookieproducers

interface CookieProducer {
    val name: String
    val cpm: Int
    val startingPrice: Int

    fun calculatePrice(amount: Int): Int {
        var price = startingPrice

        for (i in 0 until amount) {
            price = (price * 1.10).toInt()
        }

        return price
    }

}