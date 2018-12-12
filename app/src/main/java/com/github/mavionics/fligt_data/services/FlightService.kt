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
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.RingtoneManager
import android.os.Binder
import com.github.mavionics.fligt_data.activities.MainActivity
import io.fabric.sdk.android.services.settings.IconRequest.build
import android.widget.Toast
import com.github.mavionics.fligt_data.services.FlightService.LocalBinder










class FlightService : Service() {

    // This is the object that receives interactions from clients.  See
    // RemoteService for a more complete example.
    private val mBinder = LocalBinder()

    override fun onBind(intent: Intent): IBinder {
        return mBinder;
    }

    private val TAG: String? = "FlightService"
    private lateinit var mNM: NotificationManager
    internal var mNOTIFICATION_ID: Int = 0

    /**
     * Class for clients to access.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with
     * IPC.
     */
    inner class LocalBinder : Binder() {
        fun getService() : FlightService {
            return this@FlightService
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "onStartCommand")

        mNOTIFICATION_ID = resources.getInteger(R.integer.FOREGROUND_SERVICE_NOTIFICATION_ID)
        return Service.START_STICKY
    }



    override fun onCreate() {
        Log.d(TAG, "onCreate")
        mNM = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Display a notification about us starting.  We put an icon in the status bar.
        showNotification()
    }

    override fun onDestroy() {
        // Cancel the persistent notification.
        mNM.cancel(mNOTIFICATION_ID)

        // Tell the user we stopped.
        Toast.makeText(this, "Flight finished", Toast.LENGTH_SHORT).show()
    }

    /**
     * Show a notification while this service is running.
     */
    private fun showNotification() {
        // The PendingIntent to launch our activity if the user selects this notification
        val notificationIntent = Intent(this, FlightActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, 0)

        // Set the info for the views that show in the notification panel.
        val builder = NotificationCompat.Builder(this,
                getString(R.string.default_notification_channel_id))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Mavionics")
                .setContentText("Flight in progress")
                .setOngoing(true)
                .setSmallIcon(R.drawable.ic_mavionics_logo)
                .setContentIntent(pendingIntent)

        // Send the notification.
        mNM.notify(mNOTIFICATION_ID, builder.build())
    }
}
