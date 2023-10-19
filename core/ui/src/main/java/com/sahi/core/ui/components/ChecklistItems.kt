package com.sahi.core.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

@Composable
fun ChecklistItems(
    modifier: Modifier = Modifier,
    onPlacementComplete: (Int) -> Unit,
    content: @Composable () -> Unit
) {
    Layout(
        modifier = modifier,
        content = content
    ) { measurables, constraints ->
        val placeables = measurables.map { it.measure(constraints) }

        data class Item(val placeable: Placeable, val yPosition: Int)

        val items = mutableListOf<Item>()
        var yPosition = 0
        for (placeable in placeables) {
            if (yPosition + placeable.height > constraints.maxHeight) break
            items.add(Item(placeable, yPosition))
            yPosition += placeable.height
        }

        layout(
            width = items.maxOf { it.placeable.width },
            height = items.last().let { it.yPosition + it.placeable.height },
        ) {
            items.forEach {
                it.placeable.place(0, it.yPosition)
            }
            onPlacementComplete(items.count())
        }
    }
}

@Composable
fun ChecklistItem(
    modifier: Modifier = Modifier,
    checked: Boolean = false,
    label: String = "",
) {
    Row(
        modifier = modifier.padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Card(
            modifier = Modifier,
            elevation = CardDefaults.cardElevation(0.dp),
            shape = RoundedCornerShape(2.dp),
            colors = if (checked) {
                CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary)
            } else {
                CardDefaults.cardColors(containerColor = Color.Transparent)
            },
            border = if (checked) {
                BorderStroke(1.3.dp, color = MaterialTheme.colorScheme.primary)
            } else {
                BorderStroke(1.3.dp, color = MaterialTheme.colorScheme.onBackground)
            }
        ) {
            Box(
                modifier = Modifier
                    .size(16.dp),
                contentAlignment = Alignment.Center
            ) {
                if (checked)
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
            }
        }
        Spacer(modifier = Modifier.width(8.dp))
        Box(modifier = modifier) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium,
                textDecoration = if (checked) TextDecoration.LineThrough else TextDecoration.None,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun EditChecklistItem(
    modifier: Modifier = Modifier,
    checked: Boolean = false,
    isHintVisible: Boolean = true,
    isFocused: Boolean = false,
    label: String = "",
    hint: String = "",
    onCheckedChange: ((Boolean) -> Unit)?,
    onValueChange: (String) -> Unit,
    onFocusChange: (FocusState) -> Unit,
    onAdd: () -> Unit,
    onDelete: () -> Unit,
) {
    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }

    SideEffect {
        if (isFocused)
            focusRequester.requestFocus()
    }

    Row(
        modifier = modifier,
    ) {
        Checkbox(
            checked = checked,
            colors = CheckboxDefaults.colors(
                uncheckedColor = MaterialTheme.colorScheme.onBackground
            ),
            onCheckedChange = onCheckedChange
        )
        Box(
            modifier = Modifier
                .padding(top = 11.dp)
                .weight(1f)
        ) {
            TransparentHintTextField(
                text = label,
                hint = hint,
                textStyle =
                if (checked)
                    MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.onBackground,
                        textDecoration = TextDecoration.LineThrough
                    )
                else
                    MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.onBackground,
                        textDecoration = TextDecoration.None
                    ),
                isHintVisible = isHintVisible,
                onValueChange = onValueChange,
                onFocusChange = onFocusChange,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions { onAdd() },
                modifier = modifier
                    .focusRequester(focusRequester)
            )
        }
        if (isFocused) {
            IconButton(
                colors = IconButtonDefaults
                    .iconButtonColors(contentColor = MaterialTheme.colorScheme.onBackground),
                onClick = {
                    focusManager.moveFocus(FocusDirection.Up)
                    onDelete()
                },
            ) {
                Icon(
                    imageVector = Icons.Outlined.Close,
                    contentDescription = "Delete item"
                )
            }
        }
    }
}
