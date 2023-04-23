package com.sahi.elingnote.ui.checklist_feature.edit_checklist

import com.sahi.elingnote.data.model.ChecklistItem

data class EditChecklistState(
    val text: String = "",
    val content: MutableList<ChecklistItem>? = null,
    val hint: String = "",
    val isHintVisible: Boolean = true,
)
