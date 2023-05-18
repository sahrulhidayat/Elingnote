package com.sahi.elingnote.data.repository

import com.sahi.elingnote.data.model.Checklist
import com.sahi.elingnote.data.model.ChecklistItem
import com.sahi.elingnote.data.model.ChecklistWithItems
import com.sahi.elingnote.data.source.ChecklistDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ChecklistRepositoryImpl @Inject constructor(
    private val checklistDao: ChecklistDao
) : ChecklistRepository {
    override fun getChecklists(): Flow<List<ChecklistWithItems>> {
        return checklistDao.getChecklists()
    }

    override suspend fun getChecklistWithItems(id: Int): ChecklistWithItems {
        return checklistDao.getChecklistWithItems(id)
    }

    override suspend fun addChecklist(checklist: Checklist) {
        return checklistDao.addChecklist(checklist)
    }

    override suspend fun deleteChecklist(checklist: Checklist) {
        return checklistDao.deleteChecklist(checklist)
    }

    override suspend fun addChecklistItem(item: ChecklistItem) {
        return checklistDao.addChecklistItem(item)
    }

    override suspend fun deleteChecklistItem(item: ChecklistItem) {
        return checklistDao.deleteChecklistItem(item)
    }
}