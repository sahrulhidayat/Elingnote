package com.sahi.elingnote.ui.checklist_feature.edit_checklist

import com.sahi.elingnote.data.model.ChecklistItem

data class EditChecklistState(
    val text: String = "",
    val checklistItems: ArrayList<ChecklistItem> = ArrayList(),
    val hint: String = "",
    val isHintVisible: Boolean = true,
)
