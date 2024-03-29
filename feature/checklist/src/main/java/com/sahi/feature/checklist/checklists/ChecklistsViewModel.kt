package com.sahi.feature.checklist.checklists

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sahi.core.model.entity.Checklist
import com.sahi.core.model.entity.ChecklistWithItems
import com.sahi.core.notifications.NotificationScheduler
import com.sahi.usecase.ChecklistUseCase
import com.sahi.usecase.NotificationUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

data class ChecklistsState(
    val checklists: List<ChecklistWithItems> = emptyList(),
)

class ChecklistsViewModel(
    private val checklistUseCase: ChecklistUseCase,
    private val notificationUseCase: NotificationUseCase,
    private val notificationScheduler: NotificationScheduler,
) : ViewModel() {
    var checklistsState = MutableStateFlow(ChecklistsState())
        private set
    val selectedIndexes = mutableStateListOf(false)

    var recentlyDeletedChecklists = mutableListOf<Checklist>()

    private var getChecklistsJob: Job? = null
    var eventFlow = MutableSharedFlow<UiEvent>()
        private set

    init {
        getChecklists()
    }

    fun onEvent(event: ChecklistsEvent) {
        when (event) {
            is ChecklistsEvent.DeleteChecklists -> {
                viewModelScope.launch {
                    recentlyDeletedChecklists.clear()
                    event.checklists.forEach { checklist ->
                        recentlyDeletedChecklists.add(checklist)
                        checklistUseCase.addOrUpdateChecklist(
                            checklist.copy(
                                isTrash = true,
                                reminderTime = 0L
                            )
                        )
                        val checklistId = "2${checklist.id}".toInt()
                        notificationUseCase.deleteReminder(checklistId)
                        notificationScheduler.cancel(checklistId)
                    }
                    eventFlow.emit(
                        UiEvent.ShowSnackBar(
                            message = "Checklists are moved to the trash",
                            actionLabel = "Undo"
                        )
                    )
                }
            }

            is ChecklistsEvent.RestoreChecklist -> {
                viewModelScope.launch {
                    recentlyDeletedChecklists.forEach { checklist ->
                        checklistUseCase.addOrUpdateChecklist(checklist.copy(reminderTime = 0L))
                    }
                    recentlyDeletedChecklists.clear()
                    eventFlow.emit(
                        UiEvent.ShowSnackBar(
                            message = "Checklists restored"
                        )
                    )
                }
            }
        }
    }

    private fun getChecklists() {
        getChecklistsJob?.cancel()
        getChecklistsJob = checklistUseCase.getAllChecklists()
            .onEach { checklists ->
                selectedIndexes.clear()
                while (selectedIndexes.size < checklists.size) {
                    selectedIndexes.add(false)
                }

                checklistsState.value = checklistsState.value.copy(
                    checklists = checklists
                )
            }
            .launchIn(viewModelScope)
    }
}

sealed class UiEvent {
    data class ShowSnackBar(val message: String, val actionLabel: String? = null) : UiEvent()
}

sealed class ChecklistsEvent {
    data class DeleteChecklists(val checklists: List<Checklist>) : ChecklistsEvent()
    data object RestoreChecklist : ChecklistsEvent()
}
