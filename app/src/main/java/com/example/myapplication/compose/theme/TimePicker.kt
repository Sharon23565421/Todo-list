package com.example.myapplication.compose.theme

import android.app.TimePickerDialog
import android.icu.util.Calendar
import android.util.Log
import android.widget.TimePicker
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import java.text.SimpleDateFormat
import java.util.Locale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePickerArea(
    allDayChk: Boolean = false,
    selectedTime: (String) -> Unit = {},
    currentSelectedTime: String = "",
    modifier: Modifier = Modifier
) {
    var showDialog by rememberSaveable { mutableStateOf(false) }
    val calendar = Calendar.getInstance()
    val context = LocalContext.current
    val hour = calendar.get(Calendar.HOUR_OF_DAY)
    val minute = calendar.get(Calendar.MINUTE)

    val timePickerDialog = remember {
        TimePickerDialog(
            context, { _: TimePicker, hourOfDay: Int, minuteOfHour: Int ->
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                calendar.set(Calendar.MINUTE, minuteOfHour)
                val formatter = SimpleDateFormat("HH:mm", Locale.getDefault()).format(calendar.time)
                selectedTime(formatter)
                Log.d("TimePicker", "Selected time: $formatter")
                showDialog = false
            }, hour, minute, true
        ).apply {
            setOnCancelListener {
                showDialog = false
            }
            setOnDismissListener {
                showDialog = false
            }
        }
    }
    Row(
        modifier
            .fillMaxWidth()
            .padding(start = 4.dp)
    ) {
        OutlinedTextField(
            value = currentSelectedTime,
            onValueChange = {},
            label = {
                Text(
                    text = "Pick Time"
                )
            },
            readOnly = true,
            trailingIcon = {
                IconButton(
                    onClick = { showDialog = true }
                ) {
                    Icon(
                        imageVector = Icons.Filled.AccessTime,
                        contentDescription = null
                    )
                }
            }
        )
        Spacer(Modifier.height(16.dp))
        if (showDialog) {
            timePickerDialog.show()
        }
    }

}


@Preview(showBackground = true)
@Composable
fun TimePickerAreaPreview() {
    MaterialTheme {
        TimePickerArea()
    }
}