package com.sahi.elingnote.ui.checklist_feature.checklists

import com.sahi.elingnote.data.model.ChecklistWithItems

data class ChecklistsState(
    val checklists: List<ChecklistWithItems> = emptyList(),
)
