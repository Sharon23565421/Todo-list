package com.example.myapplication.compose.theme

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun CardArea(
    text: String,
    checked: Boolean = false,
    onCheckedChange: (Boolean) -> Unit,
    btnClose: () -> Unit = {},
    modifier: Modifier = Modifier.padding(8.dp).fillMaxWidth()
) {
    Surface(
        modifier
            .shadow(8.dp, shape = RoundedCornerShape(8.dp))
    ) {
        Row(
            modifier
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = text
            )
            Row(verticalAlignment = Alignment.CenterVertically,){

            Checkbox(
                checked = checked,
                onCheckedChange = onCheckedChange
            )
            IconButton(onClick = btnClose) {
                Icon(
                    Icons.Filled.Close,
                    contentDescription = null,
                )
            }
            }
        }
    }

}

@Preview(showBackground = true)
@Composable
fun CardPreview() {
    MaterialTheme {
        CardArea("drinke Water", onCheckedChange = {})
    }

}