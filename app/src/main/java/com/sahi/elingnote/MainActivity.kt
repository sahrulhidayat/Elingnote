package com.sahi.elingnote

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.sahi.elingnote.ui.note_feature.NoteNavHost
import com.sahi.elingnote.ui.theme.ElingNoteTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ElingNoteTheme {
                val navController = rememberNavController()
                NoteNavHost(navController = navController)
            }
        }
    }
}

