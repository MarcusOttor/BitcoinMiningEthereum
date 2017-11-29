package com.freebitcoins.btcmining.satoshi.screens

import android.content.Intent
import android.os.Bundle
import android.view.View
import butterknife.OnClick
import com.freebitcoins.btcmining.satoshi.AppTools
import com.freebitcoins.btcmining.satoshi.R
import com.freebitcoins.btcmining.satoshi.core.analytics.Analytics
import kotlinx.android.synthetic.main.activity_withdraw.*
import kotlinx.android.synthetic.main.toolbar_main.*
import kotlin.concurrent.thread

class WithdrawActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_withdraw)

        bindCoinView()
        bind()

        layoutBack()

        runBasicHandler()

        toolbarText.text = "Redeem"

        initBanner()
    }

    @OnClick(R.id.menu)
    fun back(view: View) {
        onBackPressed()
    }

    override fun onBackPressed() {
        startActivity(Intent(this, MiningActivity::class.java)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP))
        finish()
    }

    @OnClick(R.id.dropDownCurrencies)
    fun dropDown(view: View) {
        dialogsManager.showCurrenciesDialog(supportFragmentManager, { currency ->
            currencyName.text = currency
            walletText.hint = currency + " wallet"
        })
    }

    @OnClick(R.id.redeem)
    fun redeem() {
        if (!satoshiCountText.text.isEmpty() and !walletText.text.isEmpty()) {
            if ((coinsManager.getCoins() >= 1500000) and (coinsManager.getCoins() >= satoshiCountText.text.toString().toInt())) {
                if (satoshiCountText.text.toString().toInt() >= 1500000) {
                    if (isWalletValid(walletText.text.toString())) {
                        if (AppTools.isNetworkAvaliable(this)) {
                            var dialog = dialogsManager.showProgressDialog(supportFragmentManager)
                            Analytics.report(Analytics.WITHDRAW, Analytics.AMOUNT, satoshiCountText.text.toString())
                            thread {
                                Thread.sleep(2000)
                                runOnUiThread {
                                    coinsManager.subtractCoins(satoshiCountText.text.toString().toInt())
                                    updateCoins()
                                    dialog.dismiss()
                                    dialogsManager.showAlertDialog(supportFragmentManager,
                                            "Thanks! You will receive your Satoshi in 3 - 7 days!", {
                                        interstitial?.show()
                                    })
                                }
                            }
                        } else {
                            dialogsManager.showAlertDialog(supportFragmentManager, "No internet connection!", {
                                interstitial?.show()
                            })
                        }
                    } else {
                        dialogsManager.showAlertDialog(supportFragmentManager, "Invalid wallet number!", {
                            interstitial?.show()
                        })
                    }
                } else {
                    dialogsManager.showAlertDialog(supportFragmentManager, "Min 1 500 000 Satoshi!", {
                        interstitial?.show()
                    })
                }
            } else {
                dialogsManager.showAlertDialog(supportFragmentManager, "Not enough Satoshi!", {
                    interstitial?.show()
                })
            }
        } else {
            dialogsManager.showAlertDialog(supportFragmentManager, "Please, do not leave empty fields!", {
                interstitial?.show()
            })
        }
    }

    private fun isWalletValid(wallet: String) : Boolean {
        return wallet.length > 15
    }
}