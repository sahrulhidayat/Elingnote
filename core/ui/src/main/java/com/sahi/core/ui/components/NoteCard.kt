package com.sahi.core.ui.components

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NoteCard(
    modifier: Modifier = Modifier,
    isSelected: Boolean = false,
    note: com.sahi.core.model.Entity.Note,
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