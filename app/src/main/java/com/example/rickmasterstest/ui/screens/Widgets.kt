package com.example.rickmasterstest.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.rickmasterstest.R

enum class DragAnchors {
    Start,
    End,
}

@OptIn(ExperimentalFoundationApi::class)
fun createAnchorDraggableState(
    density: Density,
    anchors: DraggableAnchors<DragAnchors>
): AnchoredDraggableState<DragAnchors> {
    return AnchoredDraggableState(
        initialValue = DragAnchors.Start,
        positionalThreshold = { distance: Float -> distance * 0.5f },
        velocityThreshold = { with(density) { 100.dp.toPx() } },
        animationSpec = tween(),
    ).apply {
        updateAnchors(anchors)
    }
}

@Composable
fun FavoritesButton(isFavorite: Boolean, onClick: () -> Unit) {
    ElevatedButton(
        modifier = Modifier
            .padding(8.dp)
            .size(36.dp),
        contentPadding = PaddingValues(0.dp),
        shape = CircleShape,
        onClick = { onClick() }
    ) {
        Crossfade(targetState = isFavorite) { isFavorite ->
            if (isFavorite) {
                Image(
                    modifier = Modifier.size(20.dp),
                    painter = painterResource(id = R.drawable.star),
                    contentDescription = stringResource(id = R.string.favorite_description)
                )
            } else {
                Image(
                    modifier = Modifier.size(20.dp),
                    painter = painterResource(id = R.drawable.star_outline),
                    contentDescription = stringResource(id = R.string.favorite_description)
                )
            }
        }

    }
}

@Composable
fun FavoritesIcon(isFavorite: Boolean) {
    AnimatedVisibility (
        visible = isFavorite,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        Image(
            modifier = Modifier
                .padding(24.dp)
                .size(24.dp),
            painter = painterResource(id = R.drawable.star),
            contentDescription = stringResource(id = R.string.favorite_description)
        )
    }
}

@Composable
fun InputAlertDialog(
    title: String,
    onConfirm: (String) -> Unit,
    onDismiss: () -> Unit
) {
    var textFieldValue by remember { mutableStateOf("") }
    CustomAlertDialog(
        title = { Text(text = title) },
        content = {
            OutlinedTextField(
                value = textFieldValue,
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    cursorColor = MaterialTheme.colorScheme.primary
                ),
                onValueChange = { newValue -> textFieldValue = newValue }
            )
        },
        dismissButton = {
            ElevatedButton(onClick = { onDismiss() }) {
                Text(stringResource(id = R.string.dismiss))
            }
        },
        confirmButton = {
            ElevatedButton(onClick = { onConfirm(textFieldValue) }) {
                Text(stringResource(id = R.string.confirm))
            }
        },
        onDismiss = onDismiss
    )
}

@Composable
fun CustomAlertDialog(
    title: @Composable () -> Unit,
    content: @Composable () -> Unit,
    dismissButton: @Composable () -> Unit,
    confirmButton: @Composable () -> Unit,
    onDismiss: () -> Unit,
) {
    Dialog(onDismiss) {
        Surface(shape = MaterialTheme.shapes.medium) {
            Column {
                Column(Modifier.padding(24.dp)) {
                    title.invoke()
                    Spacer(Modifier.size(8.dp))
                    content.invoke()
                }
                Spacer(Modifier.size(4.dp))
                Row(
                    Modifier
                        .padding(8.dp)
                        .fillMaxWidth(),
                    Arrangement.spacedBy(8.dp, Alignment.End),
                ) {
                    dismissButton.invoke()
                    confirmButton.invoke()
                }
            }
        }
    }
}