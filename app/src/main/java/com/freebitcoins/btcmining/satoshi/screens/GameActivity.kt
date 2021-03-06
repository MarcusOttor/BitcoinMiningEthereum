package com.freebitcoins.btcmining.satoshi.screens

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.ImageView
import butterknife.BindViews
import butterknife.OnClick
import com.freebitcoins.btcmining.satoshi.R
import com.freebitcoins.btcmining.satoshi.core.MyApplication
import com.freebitcoins.btcmining.satoshi.core.advertisements.AdvertisementManager
import com.freebitcoins.btcmining.satoshi.core.managers.PreferencesManager
import kotlinx.android.synthetic.main.activity_game.*
import kotlinx.android.synthetic.main.toolbar_main.*
import java.util.*

class GameActivity : BaseActivity(), Runnable {

    @BindViews(R.id.ticket1, R.id.ticket2, R.id.ticket3,
            R.id.ticket4, R.id.ticket5, R.id.ticket6)
    lateinit var tickets : Array<ImageView>

    @BindViews(R.id.life1, R.id.life2, R.id.life3)
    lateinit var lives : Array<ImageView>

    private var isGameStarted = true
    private var current = -1

    private var handler = Handler()

    private var ads: AdvertisementManager? = null

    private var chances = arrayOf(
            100, 200, 500, 200, 100,
            500, 100, 200, 100,
            500, 1000, 200,
            1000, 100,
            2000)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        bindCoinView()
        bind()

        ads = (application as MyApplication).advertisement

        layoutBack()

        runBasicHandler()

        toolbarText.text = "Tickets"

        updateLives()

        if (preferencesManager.get(PreferencesManager.TICKETS_LIFES, 3) == 0) {
            if (preferencesManager.get(PreferencesManager.TICKETS_TIME, 0L) < System.currentTimeMillis()) {
                preferencesManager.put(PreferencesManager.TICKETS_LIFES, 3)
                preferencesManager.put(PreferencesManager.ADDITIONAL_ATTEMPT, true)
                preferencesManager.put(PreferencesManager.ADDITIONAL_LIFE, false)
                updateLives()
            } else {
                timer.visibility = View.VISIBLE
                handler.post(this)
                scheduleAlarm()
            }
        }

        initBanner()
    }

    @OnClick(R.id.menu)
    fun back() {
        onBackPressed()
    }

    private fun updateLives() {
        lives.forEach { l ->
            l.setImageDrawable(resources.getDrawable(
                    if (lives.indexOf(l) + 1 <= preferencesManager.get(PreferencesManager.TICKETS_LIFES, 3)) {
                R.drawable.heart_active
            } else {
                R.drawable.heart_inactive
            }))
        }
    }

    @OnClick(R.id.ticket1, R.id.ticket2, R.id.ticket3, R.id.ticket4, R.id.ticket5, R.id.ticket6)
    fun play(view: View) {
        if (isGameStarted) {
            if (preferencesManager.get(PreferencesManager.TICKETS_LIFES, 3) > 0) {
                when (view.id) {
                    R.id.ticket1 -> {
                        showItem(0)
                    }
                    R.id.ticket2 -> {
                        showItem(1)
                    }
                    R.id.ticket3 -> {
                        showItem(2)
                    }
                    R.id.ticket4 -> {
                        showItem(3)
                    }
                    R.id.ticket5 -> {
                        showItem(4)
                    }
                    R.id.ticket6 -> {
                        showItem(5)
                    }
                }
            } else {
                dialogsManager.showAlertDialog(supportFragmentManager, "Not available now!", {
                    interstitial?.show()
                })
            }
        }
    }

    private fun showItem(position: Int) {
        current = position

        preferencesManager.put(PreferencesManager.TICKETS_LIFES, preferencesManager.get(PreferencesManager.TICKETS_LIFES, 3) - 1)
        if (preferencesManager.get(PreferencesManager.TICKETS_LIFES, 3) == 0) {
            preferencesManager.put(PreferencesManager.TICKETS_TIME, System.currentTimeMillis() + 3 * 60 * 60 * 1000)
            timer.visibility = View.VISIBLE
            scheduleAlarm()
            handler.post(this)
        }
        updateLives()

        animationsManager.scale(tickets[position], 0.05f, 150L, {}, {

            var chance = chances[(Random().nextInt(15))]

            tickets[position].setImageDrawable(resources.getDrawable(when (chance) {
                100 -> R.drawable.ticket_100
                200 -> R.drawable.ticket_200
                500 -> R.drawable.ticket_500
                1000 -> R.drawable.ticket_1000
                2000 -> R.drawable.ticket_2000
                else -> 0
            }))
            animationsManager.scale(tickets[position], 1f, 150L, {}, {
                isGameStarted = false
                coinsManager.addCoins(chance)
                updateCoins()
                dialogsManager.showAlertDialog(supportFragmentManager, "You got: $chance Satoshi!", {
                    hideItem(current)
                    interstitial?.show()
                    if ((preferencesManager.get(PreferencesManager.TICKETS_LIFES, 3) == 0) and
                            preferencesManager.get(PreferencesManager.ADDITIONAL_ATTEMPT, true)) {
                        dialogsManager.showAdvAlertDialog(supportFragmentManager,
                                "Get one more chance by watching the video?", {
                            preferencesManager.put(PreferencesManager.ADDITIONAL_ATTEMPT, false)
                            if (ads != null) {
                                preferencesManager.put(PreferencesManager.ADDITIONAL_LIFE, true)
                                if (!ads?.unity?.showVideo(this, coinsView)!!) {
                                    if (!ads?.adcolony?.showVideo(coinsView)!!) {
                                        if (!ads?.vungle?.showVideo(coinsView)!!) {
                                                preferencesManager.put(PreferencesManager.ADDITIONAL_LIFE, false)
                                                dialogsManager.showAlertDialog(supportFragmentManager,
                                                        "Sorry, video is unavailable!", {
                                                    interstitial?.show()
                                                })
                                        }
                                    }
                                }
                            } else {
                                dialogsManager.showAlertDialog(supportFragmentManager,
                                        "Sorry, video is unavailable!", {
                                    interstitial?.show()
                                })
                            }
                        }, {
                            preferencesManager.put(PreferencesManager.ADDITIONAL_ATTEMPT, false)
                            interstitial?.show()
                        })
                    }
                })
            })
        })
    }

    override fun onResume() {
        super.onResume()
        updateLives()
    }

    private fun hideItem(position: Int) {
        animationsManager.scale(tickets[position], 0.05f, 150L, {}, {
            tickets[position].setImageDrawable(resources.getDrawable(R.drawable.ticket_empty))
            animationsManager.scale(tickets[position], 1f, 150L, {}, {
                isGameStarted = true
            })
        })
    }

    override fun onBackPressed() {
        startActivity(Intent(this, MiningActivity::class.java)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP))
        finish()
    }

    override fun run() {
        if (preferencesManager.get(PreferencesManager.TICKETS_TIME, 0L) <= System.currentTimeMillis()) {
            preferencesManager.put(PreferencesManager.TICKETS_LIFES, 3)
            timer.visibility = View.GONE
            updateLives()
            handler.removeCallbacks(this)
        } else {
            updateTimer()
            handler.postDelayed(this, 1000)
        }
    }

    override fun onDestroy() {
        handler.removeCallbacks(this)
        super.onDestroy()
    }

    private fun updateTimer() {
        var timeDifference = (preferencesManager.get(PreferencesManager.TICKETS_TIME, 0L) - System.currentTimeMillis()) / 1000
        var hour = (timeDifference / 3600).toInt()
        var minute = ((timeDifference - hour * 3600) / 60).toInt()
        var second = (timeDifference - (hour * 3600).toLong() - (minute * 60).toLong()).toInt()
        timer.text = time(hour.toString()) + ":" + time(minute.toString()) + ":" + time(second.toString())
    }

    private fun time(time: String) : String = if (time.length == 1) { "0" + time } else { time }
}