package com.sahi.core.database.repository

import com.sahi.core.model.entity.Checklist
import com.sahi.core.model.entity.ChecklistItem
import com.sahi.core.model.entity.ChecklistWithItems
import com.sahi.core.database.ChecklistDao
import com.sahi.usecase.repository.ChecklistRepository
import kotlinx.coroutines.flow.Flow

class ChecklistRepositoryImpl(
    private val checklistDao: ChecklistDao
) : ChecklistRepository {
    override fun getChecklists(): Flow<List<ChecklistWithItems>> {
        return checklistDao.getChecklists()
    }

    override fun getScheduledChecklists(defaultTime: Long): List<ChecklistWithItems> {
        return checklistDao.getScheduledChecklists(defaultTime)
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
    override suspend fun deleteChecklist(checklist: Checklist) {
        return checklistDao.deleteChecklist(checklist)
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