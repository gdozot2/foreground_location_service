package com.test.mylocationtracking

import android.annotation.SuppressLint
import android.content.Context
import android.location.LocationManager
import com.google.android.gms.location.DeviceOrientation
import com.google.android.gms.location.DeviceOrientationListener
import com.google.android.gms.location.DeviceOrientationRequest
import com.google.android.gms.location.FusedOrientationProviderClient
import com.test.mylocationtracking.interfaces.MyOrientationClient
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import java.util.concurrent.Executors

class MyDefaultOrientationClient(
    private val context: Context,
    private val client: FusedOrientationProviderClient,
) : MyOrientationClient {

    @SuppressLint("MissingPermission")
    override fun getOrientationUpdates(interval: Long): Flow<DeviceOrientation> {
        return callbackFlow {
            if (!context.hasLocationPermissions()) {
                throw MyOrientationClient.AnyException("Missing Permissions for Orientation")
            }

            val locationManager =
                context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            val isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
            val isNetworkEnabled =
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
            if (!isGpsEnabled && !isNetworkEnabled) {
                throw MyOrientationClient.AnyException("GPS is disabled")
            }

            val orientationCallback =
                DeviceOrientationListener { orientation: DeviceOrientation ->
                    orientation.let {
                        launch {
                            send(it)
                        }
                    }
                }

            val requestOrientation =
                DeviceOrientationRequest.Builder(DeviceOrientationRequest.OUTPUT_PERIOD_DEFAULT)
                    .build()

            val executor = Executors.newSingleThreadExecutor()
            client.requestOrientationUpdates(requestOrientation,
                executor,
                orientationCallback
            )

            awaitClose {
                client.removeOrientationUpdates(orientationCallback)
            }
        }
    }

}