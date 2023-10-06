package com.sahi.core.model.entity

data class Notification(
    val requestCode: Int,
    val title: String,
    val content: String,
    val time: Long
)
