package com.sahi.elingnote.ui.components

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ElingNoteTopAppBar(
    modifier: Modifier = Modifier,
    title: String,
    actions: @Composable RowScope.() -> Unit = {},
    enterSelectMode: Boolean,
    selectedIndexes: SnapshotStateList<Boolean>,
    onResetSelect: () -> Unit,
) {

    BackHandler(enabled = enterSelectMode) {
        onResetSelect()
    }

    if (!selectedIndexes.contains(true)) {
        onResetSelect()
    }

    TopAppBar(
        modifier = modifier,
        navigationIcon = {
            if (enterSelectMode)
                IconButton(
                    onClick = onResetSelect
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        tint = MaterialTheme.colorScheme.onPrimary,
                        contentDescription = "Unselect"
                    )
                }
        },
        actions = actions,
        title = {
            val selected = selectedIndexes.filter { value -> value }
            if (enterSelectMode)
                Text(text = "${selected.size} Selected")
            else
                Text(title)
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary,
            actionIconContentColor = MaterialTheme.colorScheme.onPrimary
        )
    )
}