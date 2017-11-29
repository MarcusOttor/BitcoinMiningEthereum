package com.freebitcoins.btcmining.satoshi.core.receiver

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.support.v7.app.NotificationCompat
import com.freebitcoins.btcmining.satoshi.R
import com.freebitcoins.btcmining.satoshi.screens.MiningActivity

class NoNetworkReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        var nm = context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        var intent = Intent(context, MiningActivity::class.java)
        var content = PendingIntent.getActivity(context, 0, intent, 0)

        nm.notify(0, NotificationCompat.Builder(context).setContentTitle(context.getString(R.string.app_name))
                .setContentText("No internet connection! Mining stopped!").setSmallIcon(R.drawable.btc_white)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher))
                .setContentIntent(content).setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_SOUND).build())
    }
}