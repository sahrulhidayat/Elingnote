package com.sahi.elingnote.navigation

import androidx.compose.ui.graphics.vector.ImageVector

data class ElingNoteMenuItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val badgeCount: Int? = null
)