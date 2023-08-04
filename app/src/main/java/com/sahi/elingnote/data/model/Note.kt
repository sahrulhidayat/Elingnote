package com.sahi.elingnote.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.sahi.elingnote.ui.theme.AmaranthPink
import com.sahi.elingnote.ui.theme.Celadon
import com.sahi.elingnote.ui.theme.Celeste
import com.sahi.elingnote.ui.theme.HoneyDew
import com.sahi.elingnote.ui.theme.LightRed
import com.sahi.elingnote.ui.theme.Mindaro
import com.sahi.elingnote.ui.theme.PaleAzure
import com.sahi.elingnote.ui.theme.RosyBrown
import com.sahi.elingnote.ui.theme.SandyBrown
import com.sahi.elingnote.ui.theme.Wisteria

@Entity
data class Note(
    @PrimaryKey
    val id: Int? = null,
    val title: String,
    val content: String,
    val timestamp: Long,
    val color: Int,
    var isTrash: Boolean = false,
) {
    companion object {
        val noteColors = listOf(
            HoneyDew,
            LightRed,
            SandyBrown,
            Celadon,
            Celeste,
            AmaranthPink,
            PaleAzure,
            Mindaro,
            Wisteria,
            RosyBrown
        )
    }
}
