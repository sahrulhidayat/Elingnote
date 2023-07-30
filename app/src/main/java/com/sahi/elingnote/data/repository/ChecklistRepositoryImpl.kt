package com.sahi.elingnote.data.repository

import com.sahi.elingnote.data.model.Checklist
import com.sahi.elingnote.data.model.ChecklistItem
import com.sahi.elingnote.data.model.ChecklistWithItems
import com.sahi.elingnote.data.source.ChecklistDao
import kotlinx.coroutines.flow.Flow

class ChecklistRepositoryImpl(
    private val checklistDao: ChecklistDao
) : ChecklistRepository {
    override fun getChecklists(): Flow<List<ChecklistWithItems>> {
        return checklistDao.getChecklists()
    }

    override suspend fun getChecklistWithItems(id: Int): ChecklistWithItems {
        return checklistDao.getChecklistWithItems(id)
    }

    override suspend fun addChecklist(checklist: Checklist): Long {
        return checklistDao.addChecklist(checklist)
    }

    override suspend fun deleteTrashChecklists() {
        return checklistDao.deleteTrashChecklists()
    }

    override suspend fun addChecklistItem(item: ChecklistItem): Long {
        return checklistDao.addChecklistItem(item)
    }

    override suspend fun updateChecklistItem(item: ChecklistItem) {
        return checklistDao.updateChecklistItem(item)
    }

    override suspend fun deleteChecklistItem(item: ChecklistItem) {
        return checklistDao.deleteChecklistItem(item)
    }
}