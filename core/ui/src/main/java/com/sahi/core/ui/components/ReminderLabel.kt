package com.sahi.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.sahi.utils.simpleDateTimeFormat

@Composable
fun ReminderLabel(modifier: Modifier = Modifier, reminderTime: Long) {
    Box(
        modifier = modifier.background(
            color = MaterialTheme.colorScheme.surfaceVariant,
            shape = RoundedCornerShape(10.dp)
        )
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = if (reminderTime < System.currentTimeMillis()) {
                    Icons.Default.Check
                } else {
                    Icons.Default.Alarm
                },
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                contentDescription = null
            )
            Text(
                modifier = Modifier.padding(start = 4.dp),
                text = reminderTime.simpleDateTimeFormat(),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.labelLarge,
                textDecoration = if (reminderTime < System.currentTimeMillis()) {
                    TextDecoration.LineThrough
                } else {
                    TextDecoration.None
                }
            )
        }
    }
}