package com.gulderbone.cookieclicker

import android.widget.TextView

class Cookie {

    companion object {
        fun cookieClicked(scoreCounter: TextView) {
            Game.score++
            scoreCounter.text = Game.score.toInt().toString()
        }
    }
}