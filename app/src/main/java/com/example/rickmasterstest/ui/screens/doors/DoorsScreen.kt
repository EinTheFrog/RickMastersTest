package com.example.rickmasterstest.ui.screens.doors

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.currentRecomposeScope
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.rickmasterstest.R
import com.example.rickmasterstest.model.domain.DoorDomain
import com.example.rickmasterstest.ui.screens.DragAnchors
import com.example.rickmasterstest.ui.screens.FavoritesButton
import com.example.rickmasterstest.ui.screens.FavoritesIcon
import com.example.rickmasterstest.ui.screens.InputAlertDialog
import com.example.rickmasterstest.ui.screens.createAnchorDraggableState
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DoorsScreen() {
    var editingDoor by remember { mutableStateOf<DoorDomain?>(null) }
    val viewModel = hiltViewModel<DoorsViewModel>()
    val state = viewModel.state.observeAsState().value
    val pullRefreshState = rememberPullRefreshState(
        refreshing = state is DoorsState.Loading,
        onRefresh = { viewModel.fetchDoors() }
    )

    LaunchedEffect(true) {
        viewModel.getLocalDoors()
    }

    Box(modifier = Modifier
        .fillMaxSize()
        .pullRefresh(pullRefreshState)) {
        PullRefreshIndicator(true, pullRefreshState, Modifier.align(Alignment.TopCenter))
        when(state) {
            is DoorsState.Default -> DefaultScreen(
                state = state,
                updateDoorFavorites = viewModel::updateDoorFavorites,
                editDoorName = { selectedDoor -> editingDoor = selectedDoor }
            )
            is DoorsState.Loading, null -> EmptyScreen()
            is DoorsState.Error -> ErrorScreen(state = state)
        }
    }
    AnimatedVisibility(visible = editingDoor != null) {
        InputAlertDialog(
            title = stringResource(id = R.string.door_name_dialog_title),
            onConfirm = { newName ->
                editingDoor?.also {
                    viewModel.updateDoorName(it, newName)
                }
                editingDoor = null
            },
            onDismiss = {
                editingDoor = null
            }
        )
    }
}

@Composable
fun DefaultScreen(
    state: DoorsState.Default,
    updateDoorFavorites: (DoorDomain, Boolean) -> Unit,
    editDoorName: (DoorDomain) -> Unit
) {
    val doors = state.doorList
    LazyColumn(modifier = Modifier
        .fillMaxSize()
        .padding(horizontal = 24.dp)
    ) {
        items(doors.size) { index ->
            DraggableDoorItem(
                door = doors[index],
                updateDoorFavorites = updateDoorFavorites,
                editDoorName = editDoorName
            )
        }
    }
}

@Composable
fun EmptyScreen() {

}

@Composable
fun ErrorScreen(state: DoorsState.Error) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(1) {
            Text(modifier = Modifier
                .padding(24.dp), text = state.exception.toString())
        }
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DraggableDoorItem(
    door: DoorDomain,
    updateDoorFavorites: (DoorDomain, Boolean) -> Unit,
    editDoorName: (DoorDomain) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val density = LocalDensity.current
    val anchors = DraggableAnchors {
        DragAnchors.Start at 0f
        DragAnchors.End at -320f
    }
    val state = remember { createAnchorDraggableState(density = density, anchors = anchors) }

    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.CenterEnd
    ) {
        Row {
            EditButton(onClick = {
                coroutineScope.launch {
                    state.anchoredDrag(targetValue = DragAnchors.Start) { _, _ ->
                        this.dragTo(newOffset = 0f)
                    }
                }
                editDoorName(door)
            })
            FavoritesButton(
                isFavorite = door.favorites,
                onClick = {
                    updateDoorFavorites(door, !door.favorites)
                }
            )
        }
        DoorItem(
            modifier = Modifier
                .offset {
                    IntOffset(
                        x = state
                            .requireOffset()
                            .roundToInt(), y = 0
                    )
                }
                .anchoredDraggable(state, Orientation.Horizontal),
            door = door
        )
    }
}

@Composable
fun EditButton(onClick: () -> Unit) {
    ElevatedButton(
        modifier = Modifier
            .padding(8.dp)
            .size(36.dp),
        contentPadding = PaddingValues(0.dp),
        shape = CircleShape,
        onClick = { onClick() }
    ) {
        Image(
            modifier = Modifier.size(20.dp),
            painter = painterResource(id = R.drawable.edit),
            contentDescription = stringResource(id = R.string.edit_description)
        )
    }
}

@Composable
fun DoorItem(
    modifier: Modifier = Modifier,
    door: DoorDomain
) {
    Card(
        elevation = CardDefaults.cardElevation(4.dp),
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 16.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column {
            if (door.snapshot != null) {
                Snapshot(snapshot = door.snapshot, favorites = door.favorites)
            }
            Description(
                name = door.name,
                favorites = door.favorites,
                hasSnapshot = door.snapshot != null
            )
        }
    }
}

@Composable
fun Snapshot(snapshot: String, favorites: Boolean) {
    Box(
        contentAlignment = Alignment.TopEnd
    ) {
        AsyncImage(
            modifier = Modifier.fillMaxWidth(),
            contentScale = ContentScale.Crop,
            model = snapshot,
            error = painterResource(id = R.drawable.ic_launcher_background),
            contentDescription = stringResource(id = R.string.camera_screenshot_description)
        )
        FavoritesIcon(isFavorite = favorites)
    }
}

@Composable
fun Description(name: String, favorites: Boolean, hasSnapshot: Boolean) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier
            .padding(24.dp)
            .weight(1f)) {
            Text(text = name)
            if (hasSnapshot) {
                OnlineLabel()
            }
        }
        if (!hasSnapshot) {
            FavoritesIcon(isFavorite = favorites)
        }
        LockIcon(isLocked = true)
    }
}

@Composable
fun OnlineLabel() {
    Text(
        text = stringResource(id = R.string.online),
        style = MaterialTheme.typography.labelMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
    )
}

@Composable
fun LockIcon(isLocked: Boolean) {
    val painterResourceId = if (isLocked) R.drawable.lock else R.drawable.lock_open
    Image(
        modifier = Modifier
            .padding(top = 24.dp, end = 24.dp, bottom = 24.dp)
            .size(24.dp),
        painter = painterResource(id = painterResourceId),
        contentDescription = stringResource(id = R.string.lock_open_description)
    )
}