package com.sahi.elingnote.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Restore
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.sahi.elingnote.data.model.ChecklistWithItems
import com.sahi.elingnote.data.model.Note
import com.sahi.elingnote.ui.checklist_feature.edit_checklist.ChecklistItemState

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NoteCard(
    modifier: Modifier = Modifier,
    isSelected: Boolean = false,
    note: Note,
    onClick: () -> Unit = { },
    onLongClick: (() -> Unit)? = null,
    onRestore: () -> Unit = { }
) {
    val isWhiteBackground = Color(note.color) == Color.White
    Card(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .combinedClickable(
                onClick = onClick,
                onLongClick = onLongClick
            ),
        elevation = CardDefaults.cardElevation(0.dp),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = Color(note.color)),
        border = when {
            isSelected -> BorderStroke(width = 2.dp, color = MaterialTheme.colorScheme.primary)
            isWhiteBackground -> BorderStroke(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outlineVariant
            )

            else -> null
        }
    ) {
        Box(
            modifier = Modifier
                .padding(8.dp)
                .heightIn(max = 200.dp)
        ) {
            Column {
                if (note.title.isNotBlank())
                    Text(
                        text = note.title,
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.Black
                    )
                Spacer(modifier = Modifier.height(4.dp))
                if (note.content.isNotBlank())
                    Text(
                        text = note.content,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Black,
                        overflow = TextOverflow.Ellipsis
                    )
            }
            if (note.isTrash) {
                Spacer(modifier = Modifier.fillMaxWidth())
                IconButton(
                    modifier = Modifier.align(Alignment.TopEnd),
                    onClick = onRestore
                ) {
                    Icon(
                        imageVector = Icons.Filled.Restore,
                        contentDescription = "Restore from trash",
                        tint = Color.Black
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ChecklistCard(
    modifier: Modifier = Modifier,
    checklistWithItems: ChecklistWithItems,
    isSelected: Boolean = false,
    onClick: () -> Unit = { },
    onLongClick: (() -> Unit)? = null,
    onRestore: () -> Unit = { }
) {
    var itemCount by remember { mutableIntStateOf(0) }
    val checklistColor = checklistWithItems.checklist.color
    val isWhiteBackground = Color(checklistColor) == Color.White

    Card(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .combinedClickable(
                onClick = onClick,
                onLongClick = onLongClick
            ),
        elevation = CardDefaults.cardElevation(0.dp),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = Color(checklistColor)),
        border = when {
            isSelected -> BorderStroke(width = 2.dp, color = MaterialTheme.colorScheme.primary)
            isWhiteBackground -> BorderStroke(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outlineVariant
            )

            else -> null
        }
    ) {
        Box(
            modifier = Modifier
                .padding(8.dp)
                .heightIn(max = 200.dp)
        ) {
            Column {
                val title = checklistWithItems.checklist.title
                if (title.isNotBlank()) {
                    Text(
                        text = checklistWithItems.checklist.title,
                        style = MaterialTheme.typography.titleMedium.copy(
                            color = Color.Black
                        )
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                if (checklistWithItems.checklistItems.isNotEmpty()) {
                    ChecklistItems(
                        onPlacementComplete = { itemCount = it }
                    ) {
                        checklistWithItems.checklistItems.forEach { item ->
                            ChecklistItem(
                                state = ChecklistItemState(
                                    checklistId = item.checklistId,
                                    label = item.label,
                                    checked = item.checked
                                )
                            )
                        }
                    }
                }
            }
            if (checklistWithItems.checklist.isTrash) {
                Spacer(modifier = Modifier.fillMaxWidth())
                IconButton(
                    modifier = Modifier.align(Alignment.TopEnd),
                    onClick = onRestore
                ) {
                    Icon(
                        imageVector = Icons.Filled.Restore,
                        contentDescription = "Restore from trash",
                        tint = Color.Black
                    )
                }
            }
        }
        val overflowItems = checklistWithItems.checklistItems.size - itemCount
        if (overflowItems > 0) {
            Box(modifier = Modifier.padding(start = 8.dp, bottom = 8.dp)) {
                Text(
                    text = "+ $overflowItems items",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = Color.Black
                    )
                )
            }
        }
    }
}