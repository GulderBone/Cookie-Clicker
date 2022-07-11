@file:Suppress("unused", "unused")

package com.gulderbone.cookieclicker.utilities

import com.gulderbone.cookieclicker.data.CookieProducer
import com.squareup.moshi.FromJson
import com.squareup.moshi.Moshi
import com.squareup.moshi.ToJson
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import java.math.BigDecimal

object CookieProducerAdapter {
    private val moshi = Moshi.Builder()
        .add(BigDecimalAdapter)
        .add(KotlinJsonAdapterFactory())
        .build()
    private val jsonAdapter = moshi.adapter(CookieProducer::class.java)

    @FromJson
    fun fromJson(string: String): CookieProducer =
        jsonAdapter.fromJson(string) ?: CookieProducer("not found", BigDecimal.ZERO, BigDecimal.ZERO)

    @ToJson
    fun toJson(producer: CookieProducer) = jsonAdapter.toJson(producer)!!
}