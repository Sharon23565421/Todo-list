package com.example.myapplication.function.compose

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
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
import androidx.core.app.ActivityCompat
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.ktx.model.cameraPosition
import java.util.Locale

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun LocationPermissionText(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val locationPermissionsState = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    )
    var showPermanentlyDeniedDialog by rememberSaveable { mutableStateOf(false) }
    var locationAccuracy by rememberSaveable { mutableStateOf("You're not open Location yet!") }
    val fineLocationPermission =
        locationPermissionsState.permissions.find { it.permission == Manifest.permission.ACCESS_FINE_LOCATION }
    val coarseLocationPermission =
        locationPermissionsState.permissions.find { it.permission == Manifest.permission.ACCESS_COARSE_LOCATION }
    val hasFineLocation = fineLocationPermission?.status == PermissionStatus.Granted
    val hasCoarseLocation = coarseLocationPermission?.status == PermissionStatus.Granted
    var taiwan by rememberSaveable { mutableStateOf(LatLng(0.0, 0.0)) }
    var cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(taiwan, 15f)
    }
    var isMapLoad by rememberSaveable { mutableStateOf(false) }
    var Alladdress by rememberSaveable { mutableStateOf("") }
//    val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

    fun getAddress(context: Context, latitude: Double, longitude: Double) {
        try {
            val geocoder = Geocoder(context, Locale.getDefault())
            val addresses : List<Address>? = geocoder.getFromLocation(latitude,longitude,1)
            if (addresses != null && addresses.isNotEmpty()){
                Alladdress = addresses[0].getAddressLine(0) ?:"Unknown Address"
            }

        } catch (e: Exception) {
            Log.d("Location", "Error getting address : ${e.message}")
        }
    }

    LaunchedEffect(
        fineLocationPermission?.status, coarseLocationPermission?.status
    ) {
        locationAccuracy = when {
            hasFineLocation && hasCoarseLocation -> "You're open Find Location!"
            !hasFineLocation && hasCoarseLocation -> "You're open Coarse Location!"
            else -> "You're not open Location yet!"
        }
        if (hasFineLocation && hasCoarseLocation) {
            Toast.makeText(context, "Find Location is open!", Toast.LENGTH_SHORT).show()
        } else if (!hasFineLocation && hasCoarseLocation) {
            Toast.makeText(context, "Coarse Location is open!", Toast.LENGTH_SHORT).show()
        }
    }

    DisposableEffect(hasFineLocation, hasCoarseLocation) {
        val fusedLocationClient: FusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(context)
        val locationRequest = LocationRequest.create().apply {
            interval = 600000
            fastestInterval = 60000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
        val locationCallBack = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                locationResult.lastLocation?.let {
                    taiwan = LatLng(it.latitude, it.longitude)
                    Log.d("Location", "locationCallBack-taiwan : $taiwan")
                    cameraPositionState.position = CameraPosition.fromLatLngZoom(taiwan, 15f)
                    getAddress(context, it.latitude, it.longitude)
                    isMapLoad = true
                }
            }
        }

        if (
            ActivityCompat.checkSelfPermission(
                context, Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(
                context, Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient.requestLocationUpdates(
                locationRequest, locationCallBack, null
            )
        }
        onDispose {
            fusedLocationClient.removeLocationUpdates(locationCallBack)
            Log.d("Location", "remove location update")
        }
    }



    fun openAppSettings() {
        val intent =
            android.content.Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        intent.data = android.net.Uri.fromParts("package", context.packageName, null)
        context.startActivity((intent))
    }

    if (showPermanentlyDeniedDialog) {
        AlertDialog(
            onDismissRequest = {
                showPermanentlyDeniedDialog = false
            },
            title = { Text("Location Permission Denied") },
            text = { Text("You need to app setting to open location!") },
            confirmButton = {
                Button(
                    onClick = {
                        showPermanentlyDeniedDialog = false
                        openAppSettings()
                    }
                ) {
                    Text("Open App Setting")
                }
            },
            dismissButton = {
                Button(
                    onClick = {
                        showPermanentlyDeniedDialog = false
                    }
                ) {
                    Text("Cancel")
                }
            }
        )
    }

    Column {
        Row(
            modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = "Add Map",
                    style = MaterialTheme.typography.titleMedium
                )
                if (hasCoarseLocation || hasFineLocation) {
                    Text(
                        text = "($locationAccuracy)",
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            }
            Switch(
                checked = hasCoarseLocation || locationPermissionsState.allPermissionsGranted,
                onCheckedChange = {
                    if (it) {
                        locationPermissionsState.launchMultiplePermissionRequest()
                        showPermanentlyDeniedDialog = false
                    } else {
                        showPermanentlyDeniedDialog = true
                    }
                }
            )
        }

        if (isMapLoad) {
            Column {

                Text(
                    text = "Location address : $Alladdress",
                    style = MaterialTheme.typography.labelMedium
                )

            GoogleMap(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp),
                cameraPositionState = cameraPositionState
            ) {
                Marker(
                    state = MarkerState(position = taiwan),
                )
            }
            }

        }
    }
}