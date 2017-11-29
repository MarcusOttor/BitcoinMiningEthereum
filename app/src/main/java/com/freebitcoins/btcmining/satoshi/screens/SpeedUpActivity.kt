package com.freebitcoins.btcmining.satoshi.screens

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.TextView
import butterknife.OnClick
import com.freebitcoins.btcmining.satoshi.R
import com.freebitcoins.btcmining.satoshi.core.data.Booster
import com.vicpin.krealmextensions.queryAll
import com.vicpin.krealmextensions.save
import kotlinx.android.synthetic.main.activity_speedup.*
import kotlinx.android.synthetic.main.toolbar_main.*

class SpeedUpActivity : BaseActivity(), Runnable {

    private var handler: Handler = Handler()

    private lateinit var boosters: List<Booster>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_speedup)

        bindCoinView()
        layoutBack()

        bind()

        boosters = Booster().queryAll()

        runBasicHandler()

        disableTemporaryBoosters()
        handler.post(this)

        updateCounts()

        toolbarText.text = "Speed Up"

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

    private fun updateCounts() {
        satoshi3text.text = if (boosters.count { c -> (c.satoshi == 3) and (c.time == 0L) } != 0) {
            boosters.count { c ->(c.satoshi == 3) and (c.time == 0L) }.toString() + "X"
        } else {""}
        satoshi5text.text = if (boosters.count { c -> (c.satoshi == 5) and (c.time == 0L) } != 0) {
            boosters.count { c -> (c.satoshi == 5) and (c.time == 0L) }.toString() + "X"
        } else {""}
        satoshi7text.text = if (boosters.count { c -> (c.satoshi == 7) and (c.time == 0L) } != 0) {
            boosters.count { c -> (c.satoshi == 7) and (c.time == 0L) }.toString() + "X"
        } else {""}
        satoshi9text.text = if (boosters.count { c -> (c.satoshi == 9) and (c.time == 0L) } != 0) {
            boosters.count { c -> (c.satoshi == 9) and (c.time == 0L) }.toString() + "X"
        } else {""}

        satoshi30text.text = if (boosters.count { c -> (c.satoshi == 30) and (c.time == 0L) } != 0) {
            boosters.count { c -> (c.satoshi == 30) and (c.time == 0L) }.toString() + "X"
        } else {""}
        satoshi50text.text = if (boosters.count { c -> (c.satoshi == 50) and (c.time == 0L) } != 0) {
            boosters.count { c -> (c.satoshi == 50) and (c.time == 0L) }.toString() + "X"
        } else {""}
        satoshi70text.text = if (boosters.count { c -> (c.satoshi == 70) and (c.time == 0L) } != 0) {
            boosters.count { c -> (c.satoshi == 70) and (c.time == 0L) }.toString() + "X"
        } else {""}
        satoshi100text.text = if (boosters.count { c -> (c.satoshi == 100) and (c.time == 0L) } != 0) {
            boosters.count { c -> (c.satoshi == 100) and (c.time == 0L) }.toString() + "X"
        } else {""}
        satoshi150text.text = if (boosters.count { c -> (c.satoshi == 150) and (c.time == 0L) } != 0) {
            boosters.count { c -> (c.satoshi == 150) and (c.time == 0L) }.toString() + "X"
        } else {""}
        satoshi200text.text = if (boosters.count { c -> (c.satoshi == 200) and (c.time == 0L) } != 0) {
            boosters.count { c -> (c.satoshi == 200) and (c.time == 0L) }.toString() + "X"
        } else {""}
    }

    private fun disableTemporaryBoosters() {
        setTempVisibility(1, boosters.filter { f -> (f.satoshi == 1) and (f.time > 0) }.isEmpty())
        setTempVisibility(2, boosters.filter { f -> (f.satoshi == 2) and (f.time > 0) }.isEmpty())
        setTempVisibility(3, boosters.filter { f -> (f.satoshi == 3) and (f.time > 0) }.isEmpty())
        setTempVisibility(4, boosters.filter { f -> (f.satoshi == 4) and (f.time > 0) }.isEmpty())
    }

    private fun setTempVisibility(boost: Int, vis: Boolean) {
        when (boost) {
            1 -> {
                satoshi1temp.visibility = if (vis) {View.VISIBLE} else {View.GONE}
                satoshi1temptext.visibility = if (vis) {View.GONE} else {View.VISIBLE}
            }
            2 -> {
                satoshi2temp.visibility = if (vis) {View.VISIBLE} else {View.GONE}
                satoshi2temptext.visibility = if (vis) {View.GONE} else {View.VISIBLE}
            }
            3 -> {
                satoshi3temp.visibility = if (vis) {View.VISIBLE} else {View.GONE}
                satoshi3temptext.visibility = if (vis) {View.GONE} else {View.VISIBLE}
            }
            4 -> {
                satoshi4temp.visibility = if (vis) {View.VISIBLE} else {View.GONE}
                satoshi4temptext.visibility = if (vis) {View.GONE} else {View.VISIBLE}
            }
        }
    }

    @OnClick(R.id.satoshi1temptext, R.id.satoshi2temptext, R.id.satoshi3temptext, R.id.satoshi4temptext)
    fun textClick(view: View) {
        dialogsManager.showAlertDialog(supportFragmentManager, "Already purchased!", {
            interstitial?.show()
        })
    }

    @OnClick(R.id.satoshi3booster, R.id.satoshi5booster, R.id.satoshi7booster, R.id.satoshi10booster)
    fun permanent(v: View) {
        when (v.id) {
            R.id.satoshi3booster -> { checkMoney(5000, { Booster(3, 0).save()}) }
            R.id.satoshi5booster -> { checkMoney(6500, { Booster(5, 0).save()}) }
            R.id.satoshi7booster -> { checkMoney(9000, { Booster(7, 0).save()}) }
            R.id.satoshi10booster -> { checkMoney(12000, { Booster(9, 0).save()}) }
        }
    }

    @OnClick(R.id.satoshi1temp, R.id.satoshi2temp, R.id.satoshi3temp, R.id.satoshi4temp)
    fun temp(v: View) {
        when (v.id) {
            R.id.satoshi1temp -> { checkMoney(50, { Booster(1, (System.currentTimeMillis() + (60 * 60 * 1000))).save()}) }
            R.id.satoshi2temp -> { checkMoney(110, { Booster(2, (System.currentTimeMillis() + (60 * 60 * 1000))).save()}) }
            R.id.satoshi3temp -> { checkMoney(170, { Booster(3, (System.currentTimeMillis() + (60 * 60 * 1000))).save()}) }
            R.id.satoshi4temp -> { checkMoney(230, { Booster(4, (System.currentTimeMillis() + (60 * 60 * 1000))).save()}) }
        }
    }

    @OnClick(R.id.satoshi30pool, R.id.satoshi50pool, R.id.satoshi70pool,
            R.id.satoshi100pool, R.id.satoshi150pool ,R.id.satoshi200pool)
    fun pool(v: View) {
        when (v.id) {
            R.id.satoshi30pool -> { checkMoney(40000, { Booster(30, 0).save()}) }
            R.id.satoshi50pool -> { checkMoney(55000, { Booster(50, 0).save()}) }
            R.id.satoshi70pool -> { checkMoney(80000, { Booster(70, 0).save()}) }
            R.id.satoshi100pool -> { checkMoney(130000, { Booster(100, 0).save()}) }
            R.id.satoshi150pool -> { checkMoney(170000, { Booster(150, 0).save()}) }
            R.id.satoshi200pool -> { checkMoney(230000, { Booster(200, 0).save()}) }
        }
    }

    private fun checkMoney(price: Int, ok: () -> Unit) {
        if (coinsManager.getCoins() >= price) {
            coinsManager.subtractCoins(price)
            updateCoins()
            ok()
            boosters = Booster().queryAll()
            updateCounts()
            disableTemporaryBoosters()
            dialogsManager.showAlertDialog(supportFragmentManager, "Booster successfully purchased!", {
                interstitial?.show()
            })
        } else {
            dialogsManager.showAlertDialog(supportFragmentManager, "Not enough Satoshi!", {
                interstitial?.show()
            })
        }
    }

    override fun run() {
        boosters = Booster().queryAll()
        if (boosters.firstOrNull { f -> (f.satoshi == 1) and (f.time > 0) } != null) {
            updateTimer((boosters.first { f -> (f.satoshi == 1) and (f.time > 0) }.time - System.currentTimeMillis()), satoshi1temptext)
        }
        if (boosters.firstOrNull { f -> (f.satoshi == 2) and (f.time > 0) } != null) {
            updateTimer((boosters.first { f -> (f.satoshi == 2) and (f.time > 0) }.time - System.currentTimeMillis()), satoshi2temptext)
        }
        if (boosters.firstOrNull { f -> (f.satoshi == 3) and (f.time > 0) } != null) {
            updateTimer((boosters.first { f -> (f.satoshi == 3) and (f.time > 0) }.time - System.currentTimeMillis()), satoshi3temptext)
        }
        if (boosters.firstOrNull { f -> (f.satoshi == 4) and (f.time > 0) } != null) {
            updateTimer((boosters.first { f -> (f.satoshi == 4) and (f.time > 0) }.time - System.currentTimeMillis()), satoshi4temptext)
        }
        handler.postDelayed(this, 1000)
    }

    private fun updateTimer(timeDifference: Long, view: TextView) {
        var diff = timeDifference / 1000
        var hour = (diff / 3600).toInt()
        var minute = ((diff - hour * 3600) / 60).toInt()
        var second = (diff - (hour * 3600).toLong() - (minute * 60).toLong()).toInt()
        view.text = time(minute.toString()) + ":" + time(second.toString())
    }

    override fun onDestroy() {
        handler.removeCallbacks(this)
        super.onDestroy()
    }

    private fun time(time: String) : String = if (time.length == 1) { "0" + time } else { time }
}