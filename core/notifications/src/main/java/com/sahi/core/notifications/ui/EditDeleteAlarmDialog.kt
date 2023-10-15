package com.sahi.core.notifications.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.sahi.utils.simpleFormat
import com.sahi.utils.timeFormatter
import java.time.Instant
import java.time.ZoneId

@Composable
fun EditDeleteAlarmDialog(
    reminderTime: Long,
    showDialog: MutableState<Boolean>,
    onEditAlarm: () -> Unit,
    onDeleteAlarm: () -> Unit,
) {
    val dateTime = Instant.ofEpochMilli(reminderTime).atZone(ZoneId.systemDefault()).toLocalDateTime()

    val date = dateTime.toLocalDate()
    val time = dateTime.toLocalTime()

    if (showDialog.value) {
        Dialog(
            onDismissRequest = { showDialog.value = false }
        ) {
            Column(
                modifier = Modifier
                    .background(
                        color = MaterialTheme.colorScheme.background,
                        shape = RoundedCornerShape(20.dp)
                    )
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Scheduled at:",
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(top = 16.dp),
                    color = MaterialTheme.colorScheme.primary
                )
                Row {
                    Text(
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        text = timeFormatter(time.hour, time.minute),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
                Row {
                    Text(
                        modifier = Modifier,
                        text = date.simpleFormat(),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TextButton(
                        onClick = onEditAlarm
                    ) {
                        Text(text = "Edit", textAlign = TextAlign.Center)
                    }
                    TextButton(
                        onClick = onDeleteAlarm
                    ) {
                        Text(text = "Delete", textAlign = TextAlign.Center)
                    }
                    TextButton(
                        onClick = { showDialog.value = false }
                    ) {
                        Text(text = "Cancel", textAlign = TextAlign.Center)
                    }
                }
            }
        }

    }
}