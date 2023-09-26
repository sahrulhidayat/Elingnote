package com.sahi.usecase

import com.sahi.core.model.entity.Checklist
import com.sahi.core.model.entity.ChecklistItem
import com.sahi.core.model.entity.ChecklistWithItems
import com.sahi.usecase.repository.ChecklistRepository
import kotlinx.coroutines.flow.Flow

class ChecklistUseCaseImpl(private val repository: ChecklistRepository): ChecklistUseCase {
    override fun getAllChecklists(): Flow<List<ChecklistWithItems>> {
        return repository.getChecklists()
    }

    override suspend fun getChecklistWithItems(id: Int): ChecklistWithItems {
        return repository.getChecklistWithItems(id)
    }

    override suspend fun addOrUpdateChecklist(checklist: Checklist): Long {
        return repository.addChecklist(checklist)
    }

    override suspend fun deleteChecklist(checklist: Checklist) {
        return repository.deleteChecklist(checklist)
    }

    override suspend fun deleteTrashChecklists() {
        return repository.deleteTrashChecklists()
    }

    override suspend fun addChecklistItem(item: ChecklistItem): Long {
        return repository.addChecklistItem(item)
    }

    override suspend fun updateChecklistItem(item: ChecklistItem) {
        return repository.updateChecklistItem(item)
    }

    override suspend fun deleteChecklistItem(item: ChecklistItem) {
        return repository.deleteChecklistItem(item)
    }

}
interface ChecklistUseCase {
    fun getAllChecklists(): Flow<List<ChecklistWithItems>>
    suspend fun getChecklistWithItems(id: Int): ChecklistWithItems
    suspend fun addOrUpdateChecklist(checklist: Checklist): Long
    suspend fun deleteChecklist(checklist: Checklist)
    suspend fun deleteTrashChecklists()
    suspend fun addChecklistItem(item: ChecklistItem): Long
    suspend fun updateChecklistItem(item: ChecklistItem)
    suspend fun deleteChecklistItem(item: ChecklistItem)
}