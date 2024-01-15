package com.sahi.core.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.pointlessapps.rt_editor.utils.RichTextValueSnapshot

@Entity
data class Note(
    @PrimaryKey
    val id: Int? = null,
    val title: String,
    val content: RichTextValueSnapshot,
    val timestamp: Long,
    val color: Int,
    var isTrash: Boolean = false,
    val reminderTime: Long = 0L
)
