package com.sahi.elingnote.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Assignment
import androidx.compose.material.icons.automirrored.outlined.Assignment
import androidx.compose.material.icons.filled.NoteAlt
import androidx.compose.material.icons.outlined.NoteAlt
import androidx.compose.ui.graphics.vector.ImageVector
import com.sahi.elingnote.R

enum class TopLevelDestination(
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val iconTextId: Int,
    val titleTextId: Int,
) {
    NOTE(
        selectedIcon = Icons.Filled.NoteAlt,
        unselectedIcon = Icons.Outlined.NoteAlt,
        iconTextId = R.string.note,
        titleTextId = R.string.note,
    ),
    CHECKLIST(
        selectedIcon = Icons.AutoMirrored.Filled.Assignment,
        unselectedIcon = Icons.AutoMirrored.Outlined.Assignment,
        iconTextId = R.string.checklist,
        titleTextId = R.string.checklist,
    )
}