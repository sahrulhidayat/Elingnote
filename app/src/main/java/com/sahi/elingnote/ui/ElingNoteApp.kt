package com.sahi.elingnote.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Checklist
import androidx.compose.material.icons.filled.Note
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ElingNoteApp(
    appState: ElingNoteAppState = rememberElingNoteAppState()
) {

    val snackbarHostState = remember { SnackbarHostState() }

    var openBottomSheet by rememberSaveable { mutableStateOf(false) }
    val bottomSheetState = rememberModalBottomSheetState()

    val destination = appState.currentTopLevelDestination

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        floatingActionButton = {
            if (destination != null) {
                FloatingActionButton(
                    onClick = { openBottomSheet = !openBottomSheet },
                    containerColor = MaterialTheme.colorScheme.primary,
                    elevation = FloatingActionButtonDefaults.elevation(0.dp),
                ) {
                    Icon(Icons.Default.Add, "Add button")
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
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            ElingNoteNavHost(navController = appState.navController)
        }

        if (openBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = { openBottomSheet = false },
                sheetState = bottomSheetState
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start
                ) {
                    LazyColumn {
                        items(1) {
                            ListItem(
                                modifier = Modifier
                                    .clickable {
                                        appState.navController.navigateToEditNote()
                                        openBottomSheet = !openBottomSheet
                                    },
                                headlineContent = { Text(text = "New note") },
                                leadingContent = {
                                    Icon(Icons.Filled.Note, contentDescription = "New note")
                                },
                            )
                            ListItem(
                                modifier = Modifier
                                    .clickable {
                                        appState.navController.navigateToEditChecklist()
                                        openBottomSheet = !openBottomSheet
                                    },
                                headlineContent = { Text(text = "New checklist") },
                                leadingContent = {
                                    Icon(
                                        Icons.Filled.Checklist,
                                        contentDescription = "New checklist"
                                    )
                                }
                            )
                        }
                    }
                }
            }
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
