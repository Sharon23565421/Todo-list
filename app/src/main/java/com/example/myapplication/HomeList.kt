package com.example.myapplication

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.example.myapplication.compose.theme.DatePickerArea
import com.example.myapplication.compose.theme.TimePickerArea
import com.example.myapplication.function.compose.LocationPermission
import com.example.myapplication.function.compose.LocationPermissionText

@Composable
fun HomeList(
    modifier: Modifier = Modifier
        .fillMaxSize()
        .safeContentPadding()
) {
    var inputValue by rememberSaveable { mutableStateOf("") }
    var allDayChk by rememberSaveable { mutableStateOf(false) }
    var selectedTime by rememberSaveable { mutableStateOf(" ") }
    Column(modifier) {
        Text(
            text = "TO DO LIST",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        Spacer(Modifier.height(8.dp))
        PickersArea(allDayChk, { selectedTime = it }, selectedTime, { selectedTime = " " })
        AllDay(allDayChk, { allDayChk = it }, modifier)
        Spacer(Modifier.height(8.dp))
        DescribeArea(inputValue, { inputValue = it })
//        LocationPermission()
        LocationPermissionText()
    }
}

@Composable
fun PickersArea(
    allDayChk: Boolean,
    onChangAllDayChk: (String) -> Unit,
    selectedTime: String,
    onChangeSelectedTime: (String) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp)
    ) {
        val pickerWidth by animateFloatAsState(
            targetValue = if (allDayChk) 1f else 0.6f,
            animationSpec = tween(durationMillis = 300)
        )
        DatePickerArea(
            modifier = Modifier
                .fillMaxWidth(pickerWidth)
                .padding(end = if (allDayChk) 0.dp else 4.dp)
        )

        AnimatedVisibility(
            visible = !allDayChk,
            enter = fadeIn(
                initialAlpha = 0f,
                animationSpec = tween(durationMillis = 300)
            ),
            exit = fadeOut(
                animationSpec = tween(durationMillis = 300)
            )
        ) {
            TimePickerArea(
                allDayChk,
                onChangAllDayChk,
                selectedTime,
                modifier = Modifier.fillMaxWidth(1f)
            )
        }
        if (allDayChk) {
            onChangeSelectedTime(" ")
        }
    }
}

@Composable
fun DescribeArea(
    inputValue: String,
    onInputValueChange: (String) -> Unit = {},
) {
    OutlinedTextField(
        value = inputValue,
        onValueChange = {
            onInputValueChange(it)
        },
        label = {
            Text(text = "Description")
        },
        singleLine = true,
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun AllDay(checked: Boolean, onCheckedChange: (Boolean) -> Unit, modifier: Modifier = Modifier) {
    Row(
        Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = "Doing all day 😭",
            style = MaterialTheme.typography.labelLarge,
        )
        Checkbox(
            checked = checked,
            onCheckedChange = onCheckedChange
        )
    }
}

@Preview(showBackground = true)
@Composable
fun HomeListPreview() {
    MaterialTheme {
        HomeList()
    }
}