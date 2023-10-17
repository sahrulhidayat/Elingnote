package com.sahi.core.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Notification(
    @PrimaryKey
    val id: Int,
    val title: String,
    val content: String,
    val time: Long
)
