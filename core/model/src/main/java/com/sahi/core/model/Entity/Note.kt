package com.sahi.core.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Note(
    @PrimaryKey
    val id: Int? = null,
    val title: String,
    val content: String,
    val timestamp: Long,
    val color: Int,
    var isTrash: Boolean = false,
    var hasReminder: Boolean = false,
    val reminderTime: Long? = null
)
