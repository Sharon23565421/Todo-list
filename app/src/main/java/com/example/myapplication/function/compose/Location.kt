package com.example.myapplication.function.compose

import androidx.core.content.ContextCompat
import android.Manifest
import android.content.pm.PackageManager
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.google.accompanist.permissions.ExperimentalPermissionsApi

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun LocationPermission(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    var hasFindLocalPermission by rememberSaveable {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        )
    }
    var hasCoarseLocalPermission by rememberSaveable {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        )
    }
    var currentRequestPermission by rememberSaveable { mutableStateOf("") }

    val locationPermissionResultLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = {
            if (it) {
                Log.d("Location", "it true")
                when (currentRequestPermission) {
                    "ACCESS_FINE_LOCATION" -> {
                        Log.d(
                            "Location",
                            "currentRequestPermission == ACCESS_FINE_LOCATION"
                        )
                        if (ContextCompat.checkSelfPermission(
                                context,
                                Manifest.permission.ACCESS_FINE_LOCATION
                            ) == PackageManager.PERMISSION_GRANTED
                        ) {
                            Log.d("Location", "ACCESS_FINE_LOCATION")
                            hasFindLocalPermission = true
                            hasCoarseLocalPermission = true
                            Toast.makeText(
                                context,
                                "Find location permission is open!",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            Log.d("Location", "else")
                            hasFindLocalPermission = false
                            hasCoarseLocalPermission = true
                            Toast.makeText(
                                context,
                                "Coarse location permission is open!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    "ACCESS_COARSE_LOCATION" -> {
                        Log.d("Location", "ACCESS_COARSE_LOCATION")
                        hasFindLocalPermission = false
                        hasCoarseLocalPermission = true
                        Toast.makeText(
                            context,
                            "Coarse location permission is open!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    "" -> {
                        Log.d("Location", "it  '' ")
                        if (hasCoarseLocalPermission) {
                            currentRequestPermission = "ACCESS_COARSE_LOCATION"
                            hasFindLocalPermission = false
                            hasCoarseLocalPermission = true
                            Toast.makeText(
                                context,
                                "Coarse location permission is open!",
                                Toast.LENGTH_SHORT
                            ).show()
                            Log.d("Location", "it  !hasFindLocalPermission ")
                        } else {
                            currentRequestPermission = "ACCESS_FINE_LOCATION"
                            hasFindLocalPermission = true
                            hasCoarseLocalPermission = true
                            Toast.makeText(
                                context,
                                "Find location permission is open!",
                                Toast.LENGTH_SHORT
                            ).show()
                            Log.d("Location", "it  hasFindLocalPermission ")
                        }
                    }
                }
            } else {
                Log.d("Location", "it else")
                when (currentRequestPermission) {
                    "ACCESS_FINE_LOCATION" -> {
                        Log.d("Location", "it else currentRequestPermission = find")
                        if (currentRequestPermission == "ACCESS_COARSE_LOCATION") {
                            Log.d("Location", "it else ACCESS_COARSE_LOCATION")
                            hasFindLocalPermission = false
                            hasCoarseLocalPermission = true
                            Toast.makeText(
                                context,
                                "Coarse location permission is open!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    "ACCESS_COARSE_LOCATION" -> {
                        Log.d("Location", "it else currentRequestPermission = coarse")
                        hasCoarseLocalPermission = false
                    }

                    "" -> {
                        Log.d("Location", "it else '' ")
                        if (hasFindLocalPermission) {
                            currentRequestPermission = "ACCESS_FINE_LOCATION"
                            hasFindLocalPermission = true
                            hasCoarseLocalPermission = true
                            Toast.makeText(
                                context,
                                "Find location permission is open!",
                                Toast.LENGTH_SHORT
                            ).show()
                            Log.d("Location", "it else hasFindLocalPermission ")
                        } else {
                            currentRequestPermission = "ACCESS_COARSE_LOCATION"
                            hasFindLocalPermission = false
                            hasCoarseLocalPermission = true
                            Toast.makeText(
                                context,
                                "Coarse location permission is open!",
                                Toast.LENGTH_SHORT
                            ).show()
                            Log.d("Location", "it else !hasFindLocalPermission ")
                        }
                    }
                }
                Toast.makeText(context, "Location permission is close!", Toast.LENGTH_SHORT)
                    .show()
            }

        })

    fun updatePermissionState(){
        hasFindLocalPermission = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        hasCoarseLocalPermission = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        Log.d("location", "$hasFindLocalPermission")
        Log.d("location", "$hasCoarseLocalPermission")
        Log.d("location", "$currentRequestPermission")
    }

    LaunchedEffect(Unit) {
        updatePermissionState()
    }

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {

                Log.d("location", "re-checking")

                updatePermissionState()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }


    Column {
        Row(
            modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Map",
                modifier = Modifier.padding(end = 16.dp)
            )

            Switch(
                checked = hasFindLocalPermission || hasCoarseLocalPermission,
                onCheckedChange = {
                    if (it) {
                        if (hasCoarseLocalPermission && !hasFindLocalPermission) {
                            currentRequestPermission = "ACCESS_COARSE_LOCATION"
                            locationPermissionResultLauncher.launch(Manifest.permission.ACCESS_COARSE_LOCATION)
                            Log.d("Location", "😍")
                        } else if (hasCoarseLocalPermission && hasFindLocalPermission) {
                            currentRequestPermission = "ACCESS_FINE_LOCATION"
                            locationPermissionResultLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                            Log.d("Location", "😄")
                        } else {
                            currentRequestPermission = ""
                            locationPermissionResultLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                            Log.d("Location", "😂")
                        }
                    } else {
                        currentRequestPermission = ""
                        hasFindLocalPermission = false
                        hasCoarseLocalPermission = false
                        Toast.makeText(
                            context,
                            "Location permission is close!",
                            Toast.LENGTH_SHORT
                        ).show()
                        Log.d("Location", "😭")
                    }

                }
            )
        }

        if (
            currentRequestPermission != ""
        ) {
            var local: String = ""
            if (currentRequestPermission == "ACCESS_COARSE_LOCATION") {
                local = "Your opened coarse location"
            } else {
                local = "Your opened find location"
            }
            Text(
                text = "($local)",
                style = MaterialTheme.typography.labelSmall
            )
        }
    }

}
