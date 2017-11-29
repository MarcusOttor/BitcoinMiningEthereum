package com.freebitcoins.btcmining.satoshi

import android.content.Context
import android.net.ConnectivityManager
import android.provider.Settings

object AppTools {

    fun isNetworkAvaliable(context: Context): Boolean {
        var connectivity = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        var info = connectivity.activeNetworkInfo

        return info != null && info.isConnectedOrConnecting
    }

    fun uniqueId(context: Context): String {
        return Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
    }
}