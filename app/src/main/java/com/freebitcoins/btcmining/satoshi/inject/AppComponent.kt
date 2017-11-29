package com.freebitcoins.btcmining.satoshi.inject

import com.freebitcoins.btcmining.satoshi.core.MyApplication
import com.freebitcoins.btcmining.satoshi.core.services.ClaimService
import com.freebitcoins.btcmining.satoshi.core.services.MiningService
import com.freebitcoins.btcmining.satoshi.screens.BaseActivity
import com.freebitcoins.btcmining.satoshi.screens.dialogs.LoginDialog
import com.freebitcoins.btcmining.satoshi.screens.dialogs.PromocodeDialog
import com.freebitcoins.btcmining.satoshi.screens.dialogs.SignupDialog
import dagger.Component

@Component(modules = arrayOf(AppModule::class, MainModule::class))
interface AppComponent {

    fun inject(screen: BaseActivity)
    fun inject(app: MyApplication)
    fun inject(dialog: LoginDialog)
    fun inject(dialog: SignupDialog)
    fun inject(dialog: PromocodeDialog)
    fun inject(service: ClaimService)
    fun inject(service: MiningService)
}