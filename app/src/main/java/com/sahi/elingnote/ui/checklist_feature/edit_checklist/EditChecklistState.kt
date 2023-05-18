package com.sahi.elingnote.ui.checklist_feature.edit_checklist

import com.sahi.elingnote.data.model.ChecklistItem

data class EditChecklistState(
    val title: String = "",
    val hint: String = "",
    val isHintVisible: Boolean = true,
)
