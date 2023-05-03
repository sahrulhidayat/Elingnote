package com.sahi.elingnote.ui.checklist_feature.checklists

import com.sahi.elingnote.data.model.ChecklistItem
import com.sahi.elingnote.data.model.ChecklistWithItem

data class ChecklistsState(
    val checklists: List<ChecklistWithItem> = emptyList(),
    val checklistItems: List<ChecklistItem> = emptyList()
)
