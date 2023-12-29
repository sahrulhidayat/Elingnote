package com.sahi.feature.note

import androidx.compose.material3.DrawerValue
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.sahi.core.model.entity.Note
import com.sahi.feature.note.notes.NotesScreen
import com.sahi.feature.note.notes.NotesState
import org.junit.Rule
import org.junit.Test

class NotesScreenTest {

    @get:Rule
    val rule = createComposeRule()

    private val notes = listOf(
        Note(
            title = "Note 1",
            content = "Note content 1",
            timestamp = 0L,
            color = 0
        ),
        Note(
            title = "Note 2",
            content = "Note content 2",
            timestamp = 0L,
            color = 0
        )
    )
    private val selectedIndexes = mutableStateListOf(false, false)
    private val notesTag = "Notes"

    @Test
    fun showNotes() {
        rule.setContent {
            NotesScreen(
                notesState = NotesState(notes = notes),
                drawerState = rememberDrawerState(initialValue = DrawerValue.Closed),
                selectedIndexes = selectedIndexes,
                onEvent = {},
                onClickItem = {}
            )
        }

        rule.onNodeWithTag(notesTag).assertExists()
        rule.onNodeWithTag(notesTag).onChildren().assertCountEquals(2)
    }

    @Test
    fun clickNote_showDetails() {
        rule.setContent {
            NotesScreen(
                notesState = NotesState(notes = notes),
                drawerState = rememberDrawerState(initialValue = DrawerValue.Closed),
                selectedIndexes = selectedIndexes,
                onEvent = {},
                onClickItem = {}
            )
        }

        rule.onNodeWithTag(notesTag).onChildren()[0].performClick()
        rule.onNodeWithText(notes[0].title).assertExists()
        rule.onNodeWithText(notes[0].content).assertExists()
    }
}