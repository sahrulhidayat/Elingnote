package com.sahi.elingnote.ui.checklist_feature.checklists

import com.sahi.elingnote.data.model.ChecklistWithItems

sealed class ChecklistsEvent {
    data class DeleteChecklist(val checklistWithItems: ChecklistWithItems): ChecklistsEvent()
    object RestoreChecklist: ChecklistsEvent()
}
