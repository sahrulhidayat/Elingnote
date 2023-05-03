package com.sahi.elingnote.data.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity(tableName = "checklist")
data class ChecklistEntity(
    @PrimaryKey
    val id: Int,
    val title: String,
    val timestamp: Long
)

@Entity(tableName = "checklist_item")
data class ChecklistItem(
    @PrimaryKey
    val itemId: Int,
    val checklistId: Int,
    val label: String,
    var checked: Boolean = false
)

data class ChecklistWithItem(
    @Embedded
    val checklist: ChecklistEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "checklistId"
    )
    val checklistItem: List<ChecklistItem>
)