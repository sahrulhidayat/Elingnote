package com.sahi.elingnote.ui.checklist_feature.checklist_item
data class ChecklistItemState(
    val itemId: Int = 0,
    val checklistId: Int = 0,
    var label: String = "",
    var checked: Boolean = false,
    val hint: String = "New item",
    val isHintVisible: Boolean = true
)


