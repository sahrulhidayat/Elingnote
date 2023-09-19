package com.sahi.core.notifications.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTimeFilled
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.core.content.ContextCompat
import com.sahi.core.notifications.simpleFormat
import com.sahi.core.notifications.timeFormatter
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZoneOffset

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SetAlarmDialog(
    showDialog: MutableState<Boolean>,
    onSetAlarm: (alarmTime: Long) -> Unit
) {
    val context = LocalContext.current
    var hasNotificationPermission by remember {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            mutableStateOf(
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
            )
        } else mutableStateOf(true)
    }
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            hasNotificationPermission = isGranted
        }
    )

    val currentDate = LocalDate.now().atStartOfDay(ZoneOffset.UTC)
    val showDatePicker = rememberSaveable { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = currentDate.toInstant().toEpochMilli(),
        initialDisplayedMonthMillis = null
    )

    val currentTime = LocalTime.now()
    val showTimePicker = rememberSaveable { mutableStateOf(false) }
    val timePickerState = rememberTimePickerState(
        is24Hour = true,
        initialHour = currentTime.hour,
        initialMinute = currentTime.minute
    )

    val selectedDate =
        Instant.ofEpochMilli(datePickerState.selectedDateMillis!!).atZone(ZoneId.systemDefault())
            .toLocalDate()

    val alarmDateTime =
        selectedDateTimeMilli(selectedDate, timePickerState.hour, timePickerState.minute)

    ElingNoteTimePicker(
        state = timePickerState,
        showDialog = showTimePicker
    )
    ElingNoteDatePicker(
        state = datePickerState,
        showDialog = showDatePicker,
    )
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
                    .padding(16.dp)
            ) {
                Text(
                    text = "Reminder",
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(start = 16.dp, top = 16.dp),
                    color = MaterialTheme.colorScheme.primary
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        modifier = Modifier
                            .clickable { showTimePicker.value = true }
                            .padding(start = 16.dp),
                        text = timeFormatter(timePickerState.hour, timePickerState.minute),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    IconButton(onClick = { showTimePicker.value = true }) {
                        Icon(
                            imageVector = Icons.Default.AccessTimeFilled,
                            contentDescription = "Pick time",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        modifier = Modifier
                            .clickable { showDatePicker.value = true }
                            .padding(start = 16.dp),
                        text = selectedDate.simpleFormat(),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    IconButton(onClick = { showDatePicker.value = true }) {
                        Icon(
                            imageVector = Icons.Default.CalendarMonth,
                            contentDescription = "Pick date",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TextButton(
                        onClick = {
                            if (hasNotificationPermission) {
                                onSetAlarm(alarmDateTime)
                                showDialog.value = false
                            } else {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                    permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                                }
                            }
                        }
                    ) {
                        Text(text = "Set Reminder", textAlign = TextAlign.Center)
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

fun selectedDateTimeMilli(localDate: LocalDate, hour: Int, minute: Int): Long {
    val year: Int = localDate.year
    val month: Int = localDate.monthValue
    val date: Int = localDate.dayOfMonth

    return LocalDateTime.of(year, month, date, hour, minute).atZone(ZoneId.systemDefault())
        .toInstant()
        .toEpochMilli()
}
