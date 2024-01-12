package com.sahi.core.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.pointlessapps.rt_editor.model.RichTextValue
import com.pointlessapps.rt_editor.ui.RichText
import com.pointlessapps.rt_editor.ui.defaultRichTextStyle
import com.sahi.core.model.entity.Note
import com.sahi.utils.darkenColor

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

    var backgroundColor = Color(note.color)
    if (isSystemInDarkTheme()) {
        backgroundColor = Color(darkenColor(note.color))
    }

    Card(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .combinedClickable(
                onClick = onClick,
                onLongClick = onLongClick
            ),
        elevation = CardDefaults.cardElevation(0.dp),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        border = when {
            isSelected -> BorderStroke(width = 2.dp, color = MaterialTheme.colorScheme.primary)
            isWhiteBackground -> BorderStroke(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outlineVariant
            )

            else -> null
        }
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Box(
                modifier = Modifier.heightIn(max = 200.dp)
            ) {
                Column {
                    if (note.title.isNotBlank())
                        Text(
                            text = note.title,
                            style = MaterialTheme.typography.titleMedium
                        )
                    Spacer(modifier = Modifier.height(4.dp))
                    if (note.content.text.isNotBlank())
                        RichText(
                            value = RichTextValue.fromSnapshot(note.content),
                            textStyle = defaultRichTextStyle().copy(
                                textColor = MaterialTheme.colorScheme.onBackground,
                                textStyle = MaterialTheme.typography.bodyMedium,
                            )
                        )
                }
                if (note.isTrash) {
                    Spacer(modifier = Modifier.fillMaxWidth())
                    IconButton(
                        modifier = Modifier
                            .background(
                                color = MaterialTheme.colorScheme.primaryContainer,
                                shape = RoundedCornerShape(10.dp)
                            )
                            .align(Alignment.TopEnd),
                        onClick = onRestore
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Restore,
                            contentDescription = "Restore from trash",
                            tint = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }
            }
            if (note.reminderTime != 0L) {
                Spacer(modifier = Modifier.height(8.dp))
                ReminderLabel(reminderTime = note.reminderTime)
            }
        }
    }
}