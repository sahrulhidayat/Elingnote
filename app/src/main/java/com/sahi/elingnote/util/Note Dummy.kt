package com.sahi.elingnote.util

import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import com.sahi.elingnote.data.model.NoteEntity

val noteDummy = NoteEntity(
    id = 0,
    title = "Note title dummy",
    content = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Integer sodaleslaoreet commodo. Phasellus a purus eu risus elementum consequat. Aenean eu elit ut nunc convallis laoreet non ut libero.",
    timestamp = 12345L
)