package com.freebitcoins.btcmining.satoshi.core.advertisements

import android.content.Context
import android.widget.TextView
import android.widget.Toast
import com.freebitcoins.btcmining.satoshi.core.analytics.Analytics
import com.freebitcoins.btcmining.satoshi.core.managers.CoinsManager
import mo.offers.lib.core.managers.OffersManager

class HouseOffers(private var context: Context,
                  private var currency: String,
                  private var coinManager: CoinsManager) {

    private var coinsText: TextView? = null

    private var manager: OffersManager = OffersManager(context, {}, context.packageName.toString(), currency)

    fun init() {
        manager.attachRewardListener { rw ->
            coinManager.addCoins(rw)
            coinsText?.text = coinManager.getCoins().toString()
            Analytics.report(Analytics.OFFER, Analytics.MOOFFERS, Analytics.REWARD)
        }
    }

    fun show(coinsText: TextView) {
        this.coinsText = coinsText
        manager.show()
        Analytics.report(Analytics.OFFER, Analytics.MOOFFERS, Analytics.OPEN)
    }
 }
