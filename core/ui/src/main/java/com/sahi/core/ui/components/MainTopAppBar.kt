package com.sahi.core.ui.components

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainTopAppBar(
    modifier: Modifier = Modifier,
    title: String = "",
    actions: @Composable RowScope.() -> Unit = {},
    scrollBehavior: TopAppBarScrollBehavior,
    isSelectMode: Boolean = false,
    selectedIndexes: SnapshotStateList<Boolean> = mutableStateListOf(),
    onMenuClick: () -> Unit,
    onResetSelect: () -> Unit = {},
) {

    BackHandler(enabled = isSelectMode) {
        onResetSelect()
    }

    if (!selectedIndexes.contains(true)) {
        onResetSelect()
    }

    TopAppBar(
        modifier = modifier,
        navigationIcon = {
            if (!isSelectMode) {
                IconButton(
                    onClick = onMenuClick
                ) {
                    Icon(
                        imageVector = Icons.Default.Menu,
                        contentDescription = "Menu button"
                    )
                }
            } else {
                IconButton(
                    onClick = onResetSelect
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Unselect"
                    )
                }
            }
        },
        actions = actions,
        title = {
            val selected = selectedIndexes.filter { value -> value }
            if (isSelectMode)
                Text(text = "${selected.size} Selected")
            else
                Text(title)
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent,
            scrolledContainerColor = Color.Transparent,
            titleContentColor = MaterialTheme.colorScheme.onSurface,
            actionIconContentColor = MaterialTheme.colorScheme.onSurface
        ),
        scrollBehavior = scrollBehavior
    )
}