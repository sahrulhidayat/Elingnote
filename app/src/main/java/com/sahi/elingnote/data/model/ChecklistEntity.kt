package com.sahi.elingnote.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "checklists")
data class ChecklistEntity(
    @PrimaryKey
    val id: Int? = null,
    val title: String,
    val content: List<String>,
    val timestamp: Long
)
