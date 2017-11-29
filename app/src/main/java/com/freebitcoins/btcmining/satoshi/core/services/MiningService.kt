package com.freebitcoins.btcmining.satoshi.core.services

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.freebitcoins.btcmining.satoshi.AppTools
import com.freebitcoins.btcmining.satoshi.core.MyApplication
import com.freebitcoins.btcmining.satoshi.core.data.Booster
import com.freebitcoins.btcmining.satoshi.core.managers.CoinsManager
import com.freebitcoins.btcmining.satoshi.core.managers.PreferencesManager
import com.freebitcoins.btcmining.satoshi.core.receiver.NoNetworkReceiver
import com.freebitcoins.btcmining.satoshi.inject.AppModule
import com.freebitcoins.btcmining.satoshi.inject.DaggerAppComponent
import com.vicpin.krealmextensions.delete
import com.vicpin.krealmextensions.queryAll
import java.util.*
import javax.inject.Inject

class MiningService : Service() {

    private var timer: Timer = Timer()
    @Inject lateinit var preferencesManager: PreferencesManager
    @Inject lateinit var coinsManager: CoinsManager

    private var remainingTime = 60000 * 180

    companion object {
        var isTimerRunning = false
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        DaggerAppComponent.builder()
                .appModule(AppModule(this))
                .mainModule((application as MyApplication).mainModule)
                .build().inject(this)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (AppTools.isNetworkAvaliable(applicationContext)) {
            startTimer()
        }
        return START_STICKY
    }

    private fun startTimer() {
        isTimerRunning = true
        timer.schedule(ClaimTextUpdateTimer(), 0, 60000)
    }

    private fun stopService() {
        isTimerRunning = false
        timer.cancel()
        stopSelf()
    }

    inner class ClaimTextUpdateTimer : TimerTask() {
        override fun run() {
            coinsManager.addCoins(1 + Booster().queryAll().sumBy { s -> s.satoshi })
            Booster().delete { q -> q.lessThan("time", System.currentTimeMillis()); q.greaterThan("time", 0) }
            remainingTime -= 60000
            if (remainingTime <= 0) {
                preferencesManager.put(PreferencesManager.MINING_PAUSED, true)
                stopService()
            }
            if (!AppTools.isNetworkAvaliable(applicationContext)) {
                sendBroadcast(Intent(applicationContext, NoNetworkReceiver::class.java))
                stopService()
            }
        }
    }

}