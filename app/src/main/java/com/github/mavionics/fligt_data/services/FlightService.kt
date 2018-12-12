package com.github.mavionics.fligt_data.services

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.app.PendingIntent
import android.support.v4.app.NotificationCompat
import android.util.Log
import com.github.mavionics.fligt_data.R
import com.github.mavionics.fligt_data.activities.FlightActivity
import android.content.Context.NOTIFICATION_SERVICE
import android.support.v4.content.ContextCompat.getSystemService
import android.app.NotificationManager
import android.content.Context
import android.media.RingtoneManager
import com.github.mavionics.fligt_data.activities.MainActivity




class FlightService : Service() {

    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")
    }

    private val TAG: String? = "FlightService"

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        Log.d(TAG, "onStartCommand, action: " + intent?.action.toString())

        var NOTIFICATION_ID: String = getString(R.string.FOREGROUND_SERVICE_NOTIFICATION_ID)

        var ACTION_STARTFOREGROUND: String = getString(R.string.STARTFOREGROUND_ACTION)

        var ACTION_STOPFOREGROUND: String = getString(R.string.STOPFOREGROUND_ACTION)

        var ACTION_MAIN: String = getString(R.string.MAIN_ACTION)

        var ACTION_PREV: String = getString(R.string.PREV_ACTION)

        var ACTION_NEXT: String = getString(R.string.NEXT_ACTION)

        var ACTION_PLAY: String = getString(R.string.PLAY_ACTION)

        //return super.onStartCommand(intent, flags, startId)
        if (intent?.getAction().equals(ACTION_STARTFOREGROUND)) {
            Log.i(TAG, "Received Start Foreground Intent " + ACTION_STARTFOREGROUND)
            val notificationIntent = Intent(this, FlightActivity::class.java)
            val pendingIntent = PendingIntent.getActivity(this, 0,
                    notificationIntent, 0)

            //val icon = BitmapFactory.decodeResource(resources,
            //        R.drawable.ic_mavionics_logo)

            /*val notification = NotificationCompat.Builder(this)
                    .setContentTitle("Truiton Music Player")
                    .setTicker("Truiton Music Player")
                    .setContentText("My Music")
                    //.setSmallIcon(R.drawable.ic_mavionics_logo)
                    //.setLargeIcon(
                     //       Bitmap.createScaledBitmap(icon, 128, 128, false))
                    .setContentIntent(pendingIntent)
                    .setOngoing(true)
                    .addAction(android.R.drawable.ic_media_previous,
                            "Previous", ppreviousIntent)
                    .addAction(android.R.drawable.ic_media_play, "Play",
                            pplayIntent)
                    .addAction(android.R.drawable.ic_media_next, "Next",
                            pnextIntent).build()*/

            Log.d(TAG, NOTIFICATION_ID.toString())




            val i = Intent(this, MainActivity::class.java)
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            val pi = PendingIntent.getActivity(this,
                    0 /* Request code */,
                    i,
                    PendingIntent.FLAG_ONE_SHOT)

            val sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

            val builder = NotificationCompat.Builder(this,
                    getString(R.string.default_notification_channel_id))
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle("title")
                    .setContentText("body")
                    .setOngoing(true)
                    .setContentIntent(pendingIntent)

            startForeground(NOTIFICATION_ID.toInt(),
                    builder.build())


        } else if (intent?.getAction().equals(ACTION_PREV)) {
            Log.i(TAG, "Clicked Previous")
        } else if (intent?.getAction().equals(ACTION_PLAY)) {
            Log.i(TAG, "Clicked Play")
        } else if (intent?.getAction().equals(ACTION_NEXT)) {
            Log.i(TAG, "Clicked Next")
        } else if (intent?.getAction().equals(
                        ACTION_STOPFOREGROUND)) {
            Log.i(TAG, "Received Stop Foreground Intent")
            stopForeground(true)
            stopSelf()
        }
        return Service.START_STICKY

    }
}
