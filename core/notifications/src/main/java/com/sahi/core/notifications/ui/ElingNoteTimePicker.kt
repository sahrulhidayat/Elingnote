package com.sahi.core.notifications.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerLayoutType
import androidx.compose.material3.TimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ElingNoteTimePicker(
    state: TimePickerState,
    showDialog: MutableState<Boolean>
) {
    if (showDialog.value) {
        Dialog(
            onDismissRequest = { showDialog.value = false },
        ) {
            Column(
                modifier = Modifier
                    .background(
                        color = MaterialTheme.colorScheme.background,
                        shape = RoundedCornerShape(20.dp)
                    ),
                horizontalAlignment = Alignment.End
            ) {
                TimePicker(
                    state = state,
                    modifier = Modifier.padding(16.dp),
                    layoutType = TimePickerLayoutType.Vertical
                )
                TextButton(
                    onClick = {
                        showDialog.value = false
                    }
                ) {
                    Text("Ok")
                }
            }
        }
    }
}