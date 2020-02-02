# Cookie-Clicker
Cookie clicker clone made to learn Kotlin and Android API

Download APK: [Cookie Clicker](https://github.com/GulderBone/Cookie-Clicker/raw/master/Cookie%20Clicker.apk)

## What have I learnt

1. Kotlin basics: variables, conditionals, loops, data classes, abstract classes, singletons etc. (This is my first real Kotlin app)
2. Android API basics: activities, activity lifecycle, events, assets, aesources, persisting data in Android, views and more.
3. Moshi (new alternative to GSON).
4. Handler / Looper classes so I can keep updating the view in set time intervals.

## Biggest problem so far?

How to effectively store the state of the game.
I had to store cookie count (score) and cookies per second (cps) and the amount of all of the cookie producers.

Currently I am persisting my score in shared preferences and parsed map in a json file on the internal storage
of the device
