package com.sahi.elingnote

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.DisposableEffect
import com.sahi.core.ui.theme.ElingNoteTheme
import com.sahi.elingnote.ui.ElingNoteApp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            val darkTheme = isSystemInDarkTheme()

            DisposableEffect(darkTheme) {
                enableEdgeToEdge(
                    statusBarStyle = SystemBarStyle.light(
                        scrim = Color.TRANSPARENT,
                        darkScrim = Color.TRANSPARENT
                    ),
                    navigationBarStyle = SystemBarStyle.light(
                        scrim = Color.TRANSPARENT,
                        darkScrim = Color.TRANSPARENT
                    )
                )
                onDispose {}
            }
            
            ElingNoteTheme {
                ElingNoteApp()
            }
        }
    }
}

