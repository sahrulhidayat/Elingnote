package com.sahi.elingnote

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.sahi.core.ui.theme.ElingNoteTheme
import com.sahi.elingnote.ui.ElingNoteApp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ElingNoteTheme {
                ElingNoteApp()
            }
        }
    }
}

