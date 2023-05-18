package com.sahi.elingnote.ui.checklist_feature.checklists

import com.sahi.elingnote.data.model.Checklist

sealed class ChecklistsEvent {
    data class DeleteChecklist(val checklist: Checklist): ChecklistsEvent()
    object RestoreChecklist: ChecklistsEvent()
}
