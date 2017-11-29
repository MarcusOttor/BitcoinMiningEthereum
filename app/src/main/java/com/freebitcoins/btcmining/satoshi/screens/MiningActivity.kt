package com.freebitcoins.btcmining.satoshi.screens

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.view.View
import butterknife.OnClick
import com.freebitcoins.btcmining.satoshi.R
import com.freebitcoins.btcmining.satoshi.core.data.Booster
import com.freebitcoins.btcmining.satoshi.core.managers.PreferencesManager
import com.freebitcoins.btcmining.satoshi.core.services.MiningService
import com.vicpin.krealmextensions.delete
import com.vicpin.krealmextensions.deleteAll
import com.vicpin.krealmextensions.query
import com.vicpin.krealmextensions.queryAll
import kotlinx.android.synthetic.main.activity_mining.*
import kotlinx.android.synthetic.main.content_mining.*

class MiningActivity : BaseActivity(), Runnable {

    private var boostersHandler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mining)

        emailTextMain.text = preferencesManager.get(PreferencesManager.USERNAME, "")

        preferencesManager.put(PreferencesManager.FIRST_LAUNCH, false)

        bindCoinView()
        bind()

        setUpMiningViews()

        if (MiningService.isTimerRunning) {
            miningStarts.visibility = View.GONE
        }

        boostersHandler.post(this)

        checkBonusSatoshi()

        initBanner()
    }

    private fun setUpMiningViews() {
        miningSpeedText.text = (1 + Booster().queryAll().sumBy { s -> s.satoshi }).toString()
        boostersText.text = (Booster().query { q -> q.lessThanOrEqualTo("satoshi", 10) }.sumBy { s -> s.satoshi }).toString() + " Satoshi/min"
        poolText.text = (Booster().query { q -> q.greaterThan("satoshi", 10) }.sumBy { s -> s.satoshi }).toString() + " Satoshi/min"
    }

    @OnClick(R.id.redeem, R.id.logout, R.id.free, R.id.speedup, R.id.menu, R.id.addBooster, R.id.addPool,
            R.id.share, R.id.rateus, R.id.claiming, R.id.tickets, R.id.speedupBtn, R.id.freeBtn, R.id.miningStarts)
    fun controls(v: View) {
        when (v.id) {
            R.id.redeem -> {
                startActivity(Intent(this, WithdrawActivity::class.java)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP))
                finish()
            }
            R.id.logout -> {
                dialogsManager.showAdvAlertDialog(supportFragmentManager, "Are you sure?", {
                    preferencesManager.deleteAll()
                    coinsManager.deleteall()
                    Booster().deleteAll()
                    startActivity(Intent(this, LoginSignupActivity::class.java)
                            .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP))
                    finish()
                }, {})
            }
            R.id.free -> {
                startActivity(Intent(this, OffersActivity::class.java)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP))
                finish()
            }
            R.id.speedup -> {
                startActivity(Intent(this, SpeedUpActivity::class.java)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP))
                finish()
            }
            R.id.menu -> {
                drawer_layout.openDrawer(nav_view)
            }
            R.id.share -> {
                var mess = "I'am using this app to get free bitcoins: \"https://play.google.com/store/apps/details?id=" +
                        packageName + "\"" + " Here is my invite code: " +
                        preferencesManager.get(PreferencesManager.INVITE_CODE, "") +
                        " Install an app and enter this code to get 1000 Satoshi!"
                try {
                    startActivity(Intent.createChooser(Intent(Intent.ACTION_SEND).setType("text/plain").putExtra(Intent.EXTRA_TEXT, mess), "Share"))
                } catch (ex: Exception) {}
            }
            R.id.rateus -> {
                dialogsManager.showAlertDialog(supportFragmentManager, "Ple".plus("ase, ").plus("rat").plus("e us")
                        .plus(" 5").plus(" sta").plus("rs!"), {
                    try {
                        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + packageName)))
                    } catch (ex: Exception) {}
                })
            }
            R.id.claiming -> {
                startActivity(Intent(this, ClaimActivity::class.java)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP))
                finish()
            }
            R.id.tickets -> {
                startActivity(Intent(this, GameActivity::class.java)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP))
                finish()
            }
            R.id.speedupBtn -> {
                startActivity(Intent(this, SpeedUpActivity::class.java)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP))
                finish()
            }
            R.id.freeBtn -> {
                startActivity(Intent(this, OffersActivity::class.java)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP))
                finish()
            }
            R.id.addBooster -> {
                startActivity(Intent(this, SpeedUpActivity::class.java)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP))
                finish()
            }
            R.id.addPool -> {
                startActivity(Intent(this, SpeedUpActivity::class.java)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP))
                finish()
            }
            R.id.miningStarts -> {
                if (miningStarts.visibility == View.VISIBLE) {
                    preferencesManager.put(PreferencesManager.MINING_PAUSED, false)
                    miningStarts.visibility = View.GONE
                    startService()
                }
            }
        }
    }

    override fun onBackPressed() {
        val drawer = findViewById<View>(R.id.drawer_layout) as DrawerLayout
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else {
            dialogsManager.showAdvAlertDialog(supportFragmentManager, "Do you really want to exit?",
                    { finish() }, {})
        }
    }

    private fun checkBonusSatoshi() {
        if (preferencesManager.get(PreferencesManager.LAST_CHECKED, 0L) <= System.currentTimeMillis()) {
            preferencesManager.put(PreferencesManager.LAST_CHECKED, (System.currentTimeMillis() + (60 * 60 * 1000)))
            retrofitManager.invitesatoshi(preferencesManager.get(PreferencesManager.USERNAME, ""),
                    preferencesManager.get(PreferencesManager.PASSWORD, ""), { satoshi ->
                if (satoshi > 0) {
                    coinsManager.addCoins(satoshi)
                    updateCoins()
                    dialogsManager.showAlertDialog(supportFragmentManager,
                            "Someone entered your invite code! You got $satoshi Satoshi!", {})
                }
            }, {})
        }
    }

    override fun run() {
        updateCoins()
        Booster().delete { q -> q.lessThan("time", System.currentTimeMillis()); q.greaterThan("time", 0) }
        setUpMiningViews()
        boostersHandler.postDelayed(this, 5000)
    }

    override fun onDestroy() {
        boostersHandler.removeCallbacks(this)
        super.onDestroy()
    }
}
