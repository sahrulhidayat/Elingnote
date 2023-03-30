package com.sahi.elingnote.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination
import com.sahi.elingnote.navigation.TopLevelDestination
import com.sahi.elingnote.ui.component.ElingNoteNavigationBar

@Composable
fun ElingNoteApp() {

}

@Composable
fun ElingNoteBottomBar(
    destinations: List<TopLevelDestination>,
    onNavigateToDestination: (TopLevelDestination) -> Unit,
    currentDestination: NavDestination?,
    modifier: Modifier = Modifier,
) {
    ElingNoteNavigationBar(modifier = modifier) {
        destinations.forEach { destination ->
            // TODO: Define onclick destination 
        }
    }
}