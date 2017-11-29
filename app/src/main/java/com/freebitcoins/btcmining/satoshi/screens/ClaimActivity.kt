package com.freebitcoins.btcmining.satoshi.screens

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import butterknife.OnClick
import com.freebitcoins.btcmining.satoshi.R
import com.freebitcoins.btcmining.satoshi.core.managers.PreferencesManager
import kotlinx.android.synthetic.main.activity_claim.*
import kotlinx.android.synthetic.main.toolbar_main.*

class ClaimActivity : BaseActivity(), Runnable {

    private var handler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_claim)

        bindCoinView()
        bind()
        handler.post(this)

        runBasicHandler()

        toolbarText.text = "Claim"

        layoutBack()

        initBanner()
    }

    @OnClick(R.id.menu)
    fun back() {
        onBackPressed()
    }

    @OnClick(R.id.claim, R.id.moreSatoshi)
    fun claim(view: View) {
        when (view.id) {
            R.id.claim -> {
                if (preferencesManager.get(PreferencesManager.CLAIM_MINUTES, 0) == 30) {
                    if (preferencesManager.get(PreferencesManager.CLAIM_SECONDS, 0) == 0) {
                        coinsManager.addCoins(1000)
                        updateCoins()
                        preferencesManager.put(PreferencesManager.CLAIM_MINUTES, 0)
                        preferencesManager.put(PreferencesManager.CLAIM_SECONDS, 0)
                        dialogsManager.showAlertDialog(supportFragmentManager,
                                "Congratulations! You've been claimed 1000 Satoshi!", {
                            startService()
                            interstitial?.show()
                        })
                    } else {
                        dialogsManager.showAlertDialog(supportFragmentManager,
                                "Not available now!", {
                            interstitial?.show()
                        })
                    }
                } else {
                    dialogsManager.showAlertDialog(supportFragmentManager,
                            "Not available now!", {
                        interstitial?.show()
                    })
                }
            }
            R.id.moreSatoshi -> {
                startActivity(Intent(this, OffersActivity::class.java)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP))
                finish()
            }
        }
    }

    override fun onBackPressed() {
        startActivity(Intent(this, MiningActivity::class.java)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP))
        finish()
    }

    override fun run() {
        arcProgress.progress = preferencesManager.get(PreferencesManager.CLAIM_MINUTES, 0)
        arcProgress.suffixText = preferencesManager.get(PreferencesManager.CLAIM_SECONDS, 0).toString()
        if (preferencesManager.get(PreferencesManager.CLAIM_MINUTES, 0) == 30) {
            if (preferencesManager.get(PreferencesManager.CLAIM_SECONDS, 0) == 0) {
                arcProgress.bottomText = "Ready"
            } else {
                arcProgress.bottomText = "Mining"
            }
        } else {
            arcProgress.bottomText = "Mining"
        }
        handler.postDelayed(this, 1000)
    }
}