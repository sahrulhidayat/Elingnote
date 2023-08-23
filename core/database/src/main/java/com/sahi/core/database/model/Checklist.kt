package com.sahi.core.database.model

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.Relation
import com.sahi.elingnote.ui.theme.AmaranthPink
import com.sahi.elingnote.ui.theme.Celadon
import com.sahi.elingnote.ui.theme.Celeste
import com.sahi.elingnote.ui.theme.LightRed
import com.sahi.elingnote.ui.theme.Mindaro
import com.sahi.elingnote.ui.theme.PaleAzure
import com.sahi.elingnote.ui.theme.RosyBrown
import com.sahi.elingnote.ui.theme.SandyBrown
import com.sahi.elingnote.ui.theme.White
import com.sahi.elingnote.ui.theme.Wisteria

@Entity
data class Checklist(
    @PrimaryKey
    val id: Int? = null,
    val title: String,
    val timestamp: Long,
    val color: Int,
    var isTrash: Boolean = false,
) {
    companion object {
        val checklistColors = listOf(
            White,
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

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = Checklist::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("checklistId"),
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class ChecklistItem(
    @PrimaryKey
    val itemId: Int? = null,
    @ColumnInfo(index = true)
    val checklistId: Int,
    val label: String,
    var checked: Boolean
)

data class ChecklistWithItems(
    @Embedded
    val checklist: Checklist,
    @Relation(
        parentColumn = "id",
        entityColumn = "checklistId",
    )
    val checklistItems: List<ChecklistItem>
)