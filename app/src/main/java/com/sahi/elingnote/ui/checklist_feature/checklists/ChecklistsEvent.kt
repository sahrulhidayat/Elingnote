package com.sahi.elingnote.ui.checklist_feature.checklists

import com.sahi.elingnote.data.model.ChecklistEntity
import com.sahi.elingnote.data.model.ChecklistItem

sealed class ChecklistsEvent {
    data class DeleteChecklist(val checklist: ChecklistEntity): ChecklistsEvent()
    object RestoreChecklist: ChecklistsEvent()
    data class ChangeItemChecked(val checklistId: Int, val item: ChecklistItem, val checked: Boolean): ChecklistsEvent()
}
