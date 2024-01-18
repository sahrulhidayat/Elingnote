package com.sahi.core.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.NotificationAdd
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sahi.utils.simpleDateTimeFormat

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditModeTopAppBar(
    showSetAlarmDialog: MutableState<Boolean>,
    scrollBehavior: TopAppBarScrollBehavior,
    colors: TopAppBarColors = TopAppBarDefaults.topAppBarColors(),
    timeStamp: Long = 0L,
    onBack: () -> Unit
) {
    CenterAlignedTopAppBar(
        title = {
            if (timeStamp != 0L) {
                Text(
                    text = "Edited ${timeStamp.simpleDateTimeFormat()}",
                    style = MaterialTheme.typography.labelMedium
                )
            }
        },
        navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
            }
        },
        colors = colors,
        actions = {
            IconButton(onClick = { showSetAlarmDialog.value = true }) {
                Icon(Icons.Default.NotificationAdd, contentDescription = "Add reminder")
            }
        },
        scrollBehavior = scrollBehavior
    )
}