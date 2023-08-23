package com.sahi.elingnote.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import com.sahi.elingnote.navigation.ElingNoteNavHost
import com.sahi.elingnote.navigation.TopLevelDestination
import com.sahi.core.ui.components.ElingNoteNavigationBar
import com.sahi.core.ui.components.ElingNoteNavigationBarItem

@Composable
fun ElingNoteApp(
    appState: ElingNoteAppState = rememberElingNoteAppState()
) {
    val destination = appState.currentTopLevelDestination
    Scaffold(
        bottomBar = {
            if (destination != null) {
                BottomAppBar(
                    actions = {
                        ElingNoteBottomBar(
                            destinations = appState.topLevelDestinations,
                            onNavigateToDestination = appState::navigateToTopLevelDestination,
                            currentDestination = appState.currentDestination,
                        )
                    }
                )
            }
        },
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            ElingNoteNavHost(navController = appState.navController)
        }
    }
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
            val selected = currentDestination.isTopLevelDestinationInHierarchy(destination)
            ElingNoteNavigationBarItem(
                selected = selected,
                onClick = { onNavigateToDestination(destination) },
                icon = {
                    val icon = if (selected) {
                        destination.selectedIcon
                    } else {
                        destination.unselectedIcon
                    }
                    Icon(imageVector = icon, contentDescription = null)
                },
                label = { Text(text = stringResource(destination.iconTextId)) }
            )
        }
    }
}

private fun NavDestination?.isTopLevelDestinationInHierarchy(destination: TopLevelDestination) =
    this?.hierarchy?.any {
        it.route?.contains(destination.name, ignoreCase = true) ?: false
    } ?: false
