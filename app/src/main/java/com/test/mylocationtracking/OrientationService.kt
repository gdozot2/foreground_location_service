package com.test.mylocationtracking

import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import okhttp3.WebSocket
import kotlin.math.abs

class OrientationService: Service() {
    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private lateinit var  myOrientationClient: MyDefaultOrientationClient
    private lateinit var webserviceServer: String
    private lateinit var webSocket: WebSocket
    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        myOrientationClient = MyDefaultOrientationClient(
            applicationContext,
            LocationServices.getFusedOrientationProviderClient(applicationContext)
        )
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when(intent?.action){
            ACTION_START_ORIENTATION -> start()
            ACTION_STOP_ORIENTATION -> stop()
        }
        webserviceServer = intent?.extras?.getString("websocket").toString()
        webSocket = WebSocketUtils.webSocketConnection(webserviceServer) {}!!
        return super.onStartCommand(intent, flags, startId)
    }

    private fun start(){
        val notification = NotificationCompat.Builder(this, "location")
            .setContentTitle("Tracking Orientation")
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setOngoing(true)
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        var currentHeadingDegrees = 0.0F
        myOrientationClient.getOrientationUpdates(10L)
            .catch { e ->
                val updateNotification = notification.setContentText(e.printStackTrace().toString())
                notificationManager.notify(1, updateNotification.build())
            }
            .onEach {
                if (abs(currentHeadingDegrees - it.headingDegrees) > 0.1) {
                    currentHeadingDegrees = it.headingDegrees
                    webSocket.send(Parser.parseOrientation(it))
                }
            }
            .launchIn(serviceScope)

        startForeground(2, notification.build())
    }

    private fun stop(){
        stopForeground(STOP_FOREGROUND_DETACH)
        stopSelf()
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
    }

    companion object{
        const val ACTION_START_ORIENTATION = "ACTION_START_ORIENTATION"
        const val ACTION_STOP_ORIENTATION = "ACTION_STOP_ORIENTATION"
    }
}