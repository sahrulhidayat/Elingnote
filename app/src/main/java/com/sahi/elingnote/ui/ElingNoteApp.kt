package com.sahi.elingnote.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddTask
import androidx.compose.material.icons.filled.NoteAdd
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import com.sahi.elingnote.navigation.ElingNoteNavHost
import com.sahi.elingnote.navigation.TopLevelDestination
import com.sahi.elingnote.ui.checklist_feature.edit_checklist.navigateToEditChecklist
import com.sahi.elingnote.ui.components.ElingNoteNavigationBar
import com.sahi.elingnote.ui.components.ElingNoteNavigationBarItem
import com.sahi.elingnote.ui.note_feature.edit_note.navigateToEditNote

@Composable
fun ElingNoteApp(
    appState: ElingNoteAppState = rememberElingNoteAppState()
) {
    val destination = appState.currentTopLevelDestination
    Scaffold(
        floatingActionButton = {
            if (destination != null && destination != TopLevelDestination.TRASH) {
                when (destination) {
                    TopLevelDestination.NOTE -> {
                        FloatingActionButton(
                            onClick = { appState.navController.navigateToEditNote() },
                            elevation = FloatingActionButtonDefaults.elevation(0.dp),
                            containerColor = MaterialTheme.colorScheme.primary,
                            content = {
                                Icon(
                                    imageVector = Icons.Default.NoteAdd,
                                    contentDescription = "New note"
                                )
                            }
                        )
                    }

                    TopLevelDestination.CHECKLIST -> {
                        FloatingActionButton(
                            onClick = { appState.navController.navigateToEditChecklist() },
                            elevation = FloatingActionButtonDefaults.elevation(0.dp),
                            containerColor = MaterialTheme.colorScheme.primary,
                            content = {
                                Icon(
                                    imageVector = Icons.Default.AddTask,
                                    contentDescription = "New checklist"
                                )
                            }
                        )
                    }

                    else -> {}
                }
            }
        },
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
