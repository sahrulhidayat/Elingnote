package com.sahi.elingnote.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import com.sahi.elingnote.navigation.*
import com.sahi.elingnote.navigation.TopLevelDestination.CHECKLIST
import com.sahi.elingnote.navigation.TopLevelDestination.NOTE

@Composable
fun rememberElingNoteAppState(
    navController: NavHostController = rememberNavController(),
): ElingNoteAppState {
    return remember(navController) {
        ElingNoteAppState(navController)
    }
}

class ElingNoteAppState(
    val navController: NavHostController
) {
    val currentDestination: NavDestination?
        @Composable get() = navController
            .currentBackStackEntryAsState().value?.destination

    val currentTopLevelDestination: TopLevelDestination?
        @Composable get() = when (currentDestination?.route) {
            noteRoute -> NOTE
            checklistRoute -> CHECKLIST
            else -> null
        }

    val topLevelDestinations: List<TopLevelDestination> = TopLevelDestination.values().asList()

    fun navigateToTopLevelDestination(topLevelDestination: TopLevelDestination) {
        val topLevelNavOptions = navOptions {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }

        when (topLevelDestination) {
            NOTE -> navController.navigateToNote(topLevelNavOptions)
            CHECKLIST -> navController.navigateToChecklist(topLevelNavOptions)
        }

    }
}



