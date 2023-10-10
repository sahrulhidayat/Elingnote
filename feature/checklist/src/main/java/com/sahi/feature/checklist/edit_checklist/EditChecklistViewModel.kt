package com.sahi.feature.checklist.edit_checklist

import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sahi.core.model.entity.Checklist
import com.sahi.core.model.entity.ChecklistItem
import com.sahi.core.model.entity.Notification
import com.sahi.core.notifications.NotificationScheduler
import com.sahi.core.ui.theme.itemColors
import com.sahi.usecase.ChecklistUseCase
import com.sahi.usecase.NotificationUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

data class EditChecklistState(
    val title: String = "",
    val hint: String = "",
    val isHintVisible: Boolean = true,
)

data class ChecklistItemState(
    val itemId: Int = 0,
    val checklistId: Int = 0,
    var label: String = "",
    var checked: Boolean = false,
    val hint: String = "New item",
    val isHintVisible: Boolean = true,
    val isFocused: Boolean = false
)

class EditChecklistViewModel(
    private val checklistUseCase: ChecklistUseCase,
    private val notificationUseCase: NotificationUseCase,
    private val notificationScheduler: NotificationScheduler,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    var checklistTitle = mutableStateOf(EditChecklistState(hint = "Checklist title"))
        private set

    private val items = mutableStateListOf<ChecklistItemState>()
    var itemsFlow = MutableStateFlow(items)
        private set
    var reminderTime = mutableLongStateOf(0L)
        private set
    private var checklistColor = mutableIntStateOf(itemColors[0].toArgb())

    var eventFlow = MutableSharedFlow<UiEvent>()
        private set

    private var currentChecklistId: Int = 0

    init {
        savedStateHandle.get<Int>("checklistId")?.let { checklistId ->
            if (checklistId != -1) {
                viewModelScope.launch {
                    checklistUseCase.getChecklistWithItems(checklistId)
                        .also { checklistWithItems ->
                            currentChecklistId = checklistId
                            checklistTitle.value = checklistTitle.value.copy(
                                title = checklistWithItems.checklist.title,
                                isHintVisible = checklistWithItems.checklist.title.isBlank()
                            )
                            checklistWithItems.checklistItems.forEach { item ->
                                items.add(
                                    ChecklistItemState(
                                        itemId = item.itemId ?: 0,
                                        label = item.label,
                                        checked = item.checked,
                                        checklistId = item.checklistId
                                    )
                                )
                            }
                            reminderTime.longValue = checklistWithItems.checklist.reminderTime
                            checklistColor.intValue = checklistWithItems.checklist.color
                        }
                }
            } else {
                viewModelScope.launch {
                    checklistUseCase.addOrUpdateChecklist(
                        Checklist(
                            title = checklistTitle.value.title,
                            timestamp = System.currentTimeMillis(),
                            color = checklistColor.intValue
                        )
                    ).also { checklistId ->
                        currentChecklistId = checklistId.toInt()
                    }
                }
            }
        }
    }

    fun onEvent(event: EditChecklistEvent) {
        val notificationId: Int = "2$currentChecklistId".toInt()
        val checklist = Checklist(
            id = currentChecklistId,
            title = checklistTitle.value.title,
            timestamp = System.currentTimeMillis(),
            color = checklistColor.intValue,
            reminderTime = reminderTime.longValue
        )
        when (event) {
            is EditChecklistEvent.EnteredTitle -> {
                checklistTitle.value = checklistTitle.value.copy(
                    title = event.value
                )
            }

            is EditChecklistEvent.ChangeTitleFocus -> {
                checklistTitle.value = checklistTitle.value.copy(
                    isHintVisible = !event.focusState.isFocused &&
                            checklistTitle.value.title.isBlank()
                )
            }

            is EditChecklistEvent.SaveChecklist -> {
                val notification = Notification(
                    id = notificationId,
                    title = checklistTitle.value.title,
                    content = items.joinToString("\n") { it.label },
                    time = reminderTime.longValue
                )
                viewModelScope.launch {
                    if (checklistTitle.value.title.isNotBlank() || items.isNotEmpty()) {
                        checklistUseCase.addOrUpdateChecklist(checklist)
                        items.map {
                            ChecklistItem(
                                itemId = it.itemId,
                                checklistId = it.checklistId,
                                label = it.label,
                                checked = it.checked
                            )
                        }.forEach {
                            checklistUseCase.updateChecklistItem(it)
                        }
                        if (reminderTime.longValue > System.currentTimeMillis()) {
                            notificationScheduler.schedule(notification)
                            notificationUseCase.addReminder(notification)
                        }
                    } else {
                        checklistUseCase.deleteChecklist(checklist)
                    }
                }
            }

            is EditChecklistEvent.ChangeColor -> {
                checklistColor.intValue = event.color
            }

            is EditChecklistEvent.SetReminder -> {
                val notification = Notification(
                    id = notificationId,
                    title = checklistTitle.value.title,
                    content = items.joinToString("\n") { it.label },
                    time = event.time
                )
                viewModelScope.launch {
                    if (event.time > System.currentTimeMillis()) {
                        reminderTime.longValue = event.time
                        notificationUseCase.addReminder(notification)
                        notificationScheduler.schedule(notification)
                        checklistUseCase.addOrUpdateChecklist(checklist)
                        eventFlow.emit(UiEvent.ShowToast(message = "Reminder has been set"))
                    } else {
                        eventFlow.emit(UiEvent.ShowToast(message = "Reminder time has passed"))
                    }
                }
            }
        }
    }

    fun itemEvent(event: ChecklistItemEvent) {
        when (event) {
            is ChecklistItemEvent.ChangeChecked -> {
                items[event.index] = items[event.index].copy(checked = event.checked)
            }

            is ChecklistItemEvent.ChangeLabelFocus -> {
                items[event.index] = items[event.index]
                    .copy(
                        isHintVisible = !event.focusState.isFocused &&
                                items[event.index].label.isBlank(),
                        isFocused = event.focusState.isFocused
                    )
            }

            is ChecklistItemEvent.EnteredLabel -> {
                items[event.index] = items[event.index].copy(label = event.label)
            }

            is ChecklistItemEvent.DeleteItem -> {
                items.removeAt(event.index).also {
                    viewModelScope.launch {
                        checklistUseCase.deleteChecklistItem(
                            ChecklistItem(
                                itemId = it.itemId,
                                checklistId = it.checklistId,
                                label = it.label,
                                checked = it.checked
                            )
                        )
                    }
                }

            }

            is ChecklistItemEvent.AddItem -> {
                viewModelScope.launch {
                    checklistUseCase.addChecklistItem(
                        ChecklistItem(
                            checklistId = currentChecklistId,
                            label = "",
                            checked = false
                        )
                    ).also { itemId ->
                        items.add(
                            ChecklistItemState(
                                itemId = itemId.toInt(),
                                checklistId = currentChecklistId,
                                label = "",
                                checked = false,
                                isFocused = true,
                            )
                        )
                    }
                }

            }
        }
    }
}

sealed class UiEvent {
    data class ShowToast(val message: String) : UiEvent()
}

sealed class EditChecklistEvent {
    data class EnteredTitle(val value: String) : EditChecklistEvent()
    data class ChangeTitleFocus(val focusState: FocusState) : EditChecklistEvent()
    data class ChangeColor(val color: Int) : EditChecklistEvent()
    data class SetReminder(val time: Long) : EditChecklistEvent()
    data object SaveChecklist : EditChecklistEvent()
}

sealed class ChecklistItemEvent {
    data class EnteredLabel(val index: Int, val label: String) : ChecklistItemEvent()
    data class ChangeLabelFocus(val index: Int, val focusState: FocusState) : ChecklistItemEvent()
    data class ChangeChecked(val index: Int, val checked: Boolean) : ChecklistItemEvent()
    data class DeleteItem(val index: Int) : ChecklistItemEvent()
    data object AddItem : ChecklistItemEvent()
}
