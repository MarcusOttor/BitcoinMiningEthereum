package com.freebitcoins.btcmining.satoshi.screens

import android.content.Intent
import android.os.Bundle
import android.view.View
import butterknife.OnClick
import com.freebitcoins.btcmining.satoshi.R
import com.freebitcoins.btcmining.satoshi.core.managers.PreferencesManager

class LoginSignupActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_signup)

        if (preferencesManager.get(PreferencesManager.USERNAME, "").isEmpty() and
                preferencesManager.get(PreferencesManager.PASSWORD, "").isEmpty()) {
            bind()
        } else {
            goMine()
        }
    }

    @OnClick(R.id.login, R.id.signup)
    fun control(v: View) {
        when (v.id) {
            R.id.login -> {
                dialogsManager.showLoginDialog(supportFragmentManager, {
                    dialogsManager.showAlertDialog(supportFragmentManager, "You've been sucessfully logged in!", {
                        goMine()
                    })
                })
            }
            R.id.signup -> {
                dialogsManager.showSignupDialog(supportFragmentManager, {
                    dialogsManager.showAlertDialog(supportFragmentManager, "You've been successfully signed up!", {
                        dialogsManager.showPromoDialog(supportFragmentManager, {
                            goMine()
                        })
                    })
                })
            }
        }
    }

    private fun goMine() {
        startActivity(Intent(this, MiningActivity::class.java)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP))
        finish()
    }
}