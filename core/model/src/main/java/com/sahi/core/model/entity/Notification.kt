package com.sahi.core.model.entity

data class Notification(
    val time: Long,
    val title: String,
    val content: String,
    val itemType: ItemType,
) {
    enum class ItemType {
        NOTE,
        CHECKLIST
    }
}

