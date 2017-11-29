package com.freebitcoins.btcmining.satoshi.core.receiver

import android.app.Notification
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.app.PendingIntent
import android.app.NotificationManager
import android.support.v7.app.NotificationCompat
import com.freebitcoins.btcmining.satoshi.R
import com.freebitcoins.btcmining.satoshi.screens.ClaimActivity

class ClaimTimerFinishReceiver : BroadcastReceiver() {

    override fun onReceive(p0: Context?, p1: Intent?) {

            var nm = p0?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            var intent = Intent(p0, ClaimActivity::class.java)
            var content = PendingIntent.getActivity(p0, 0, intent, 0)

            nm.notify(0, NotificationCompat.Builder(p0).setContentTitle(p0.getString(R.string.app_name))
                            .setContentText("Claim your free Satoshi!!!").setSmallIcon(R.drawable.btc_white)
                            .setLargeIcon(BitmapFactory.decodeResource(p0.getResources(), R.mipmap.ic_launcher))
                            .setContentIntent(content).setAutoCancel(true)
                            .setDefaults(Notification.DEFAULT_SOUND).build())

    }
}