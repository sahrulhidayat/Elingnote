package com.sahi.core.model.entity

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity
data class Checklist(
    @PrimaryKey
    val id: Int? = null,
    val title: String,
    val timestamp: Long,
    val color: Int,
    var isTrash: Boolean = false,
    var hasReminder: Boolean = false,
    val reminderTime: Long? = null
)

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