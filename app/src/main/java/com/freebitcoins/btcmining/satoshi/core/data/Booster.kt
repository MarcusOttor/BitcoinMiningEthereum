package com.freebitcoins.btcmining.satoshi.core.data

import io.realm.RealmObject

open class Booster() : RealmObject() {

    var satoshi: Int = 0
    var time: Long = 0L

    constructor(satoshi: Int, time: Long) : this() {
        this.satoshi = satoshi
        this.time = time
    }
}