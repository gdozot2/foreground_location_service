package com.test.mylocationtracking

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import com.test.mylocationtracking.ui.theme.MyLocationTrackingTheme

class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.ACCESS_COARSE_LOCATION,
                android.Manifest.permission.POST_NOTIFICATIONS,
                ),
            111
            )
        setContent {
            var text by remember { mutableStateOf("") }
            MyLocationTrackingTheme {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center) {

                    TextField(
                        value = text,
                        onValueChange = { text = it },
                        label = { Text("Label") }
                    )

                    Button(onClick = {
                        Intent(applicationContext, OrientationService::class.java)
                            .putExtra("websocket", text)
                            .apply {
                            action = OrientationService.ACTION_START_ORIENTATION
                            startService(this)
                        }
                        Intent(applicationContext, LocationService::class.java)
                            .putExtra("websocket", text)
                            .apply {
                           action = LocationService.ACTION_START
                           startService(this)
                        }
                    }, enabled = text != "") {
                        Text(text = "Start")
                    }

                    Button(onClick = {
                        Intent(applicationContext, LocationService::class.java).apply {
                            action = LocationService.ACTION_STOP
                            startService(this)
                        }
                        Intent(applicationContext, OrientationService::class.java).apply {
                            action = OrientationService.ACTION_STOP_ORIENTATION
                            startService(this)
                        }
                    }, enabled = text != "") {
                        Text(text = "Stop")
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MyLocationTrackingTheme {
        Greeting("Android")
    }
}