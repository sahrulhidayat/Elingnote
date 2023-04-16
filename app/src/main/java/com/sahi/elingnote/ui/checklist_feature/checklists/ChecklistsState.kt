package com.sahi.elingnote.ui.checklist_feature.checklists

import com.sahi.elingnote.data.model.ChecklistEntity

data class ChecklistsState(
    val checklists: List<ChecklistEntity> = emptyList()
)
