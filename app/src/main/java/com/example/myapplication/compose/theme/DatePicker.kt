package com.example.myapplication.compose.theme

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerArea(modifier: Modifier = Modifier) {
    // 日期選擇器狀態
    val datePickerState = rememberDatePickerState()
    // 是否要顯示日期選擇器
    var showDialog by rememberSaveable { mutableStateOf(false) }
    val formatter = SimpleDateFormat("yyyy / MM / dd", Locale.getDefault())
    var selectDate by rememberSaveable { mutableStateOf(formatter.format(Date())) }

    Row(modifier
        .fillMaxWidth()) {

        OutlinedTextField(
            value = "$selectDate",
            onValueChange = {
            },
            label = {
                Text(
                    text = "Pick Date"
                )
            },
            readOnly = true,
            trailingIcon = {
                IconButton(
                    onClick = { showDialog = true }
                ) {
                    Icon(
                        imageVector = Icons.Filled.DateRange,
                        contentDescription = "DateRange"
                    )
                }
            },
            modifier = Modifier.fillMaxWidth()
        )
    }


    if (showDialog) {
        DatePickerDialog(
            onDismissRequest = { showDialog = false },
            confirmButton = {},
            dismissButton = {}
        ) {
            DatePicker(
                state = datePickerState,

                )
        }
    }

    LaunchedEffect(datePickerState.selectedDateMillis) {
        if (datePickerState.selectedDateMillis != null) {
            val select = Date(datePickerState.selectedDateMillis!!)
            val date = formatter.format(select)
            selectDate = date
            showDialog = false
        }
    }

}


@Preview(showBackground = true)
@Composable
fun DatePickerPreview() {
    MaterialTheme {
        DatePickerArea()
    }

}