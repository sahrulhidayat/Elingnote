package com.sahi.elingnote.data.repository

import com.sahi.elingnote.data.model.Checklist
import com.sahi.elingnote.data.model.ChecklistItem
import com.sahi.elingnote.data.model.ChecklistWithItems
import kotlinx.coroutines.flow.Flow

interface ChecklistRepository {

    fun getChecklists(): Flow<List<ChecklistWithItems>>

    suspend fun getChecklistWithItems(id: Int): ChecklistWithItems

    suspend fun addChecklist(checklist: Checklist): Long

    suspend fun deleteChecklist(checklist: Checklist)

    suspend fun addChecklistItem(item: ChecklistItem)

    suspend fun deleteChecklistItem(item: ChecklistItem)
}