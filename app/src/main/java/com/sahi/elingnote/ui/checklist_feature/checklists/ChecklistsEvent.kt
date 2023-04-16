package com.sahi.elingnote.ui.checklist_feature.checklists

import com.sahi.elingnote.data.model.ChecklistEntity

sealed class ChecklistsEvent {
    data class DeleteChecklist(val checklist: ChecklistEntity): ChecklistsEvent()
    object RestoreChecklist: ChecklistsEvent()
}
