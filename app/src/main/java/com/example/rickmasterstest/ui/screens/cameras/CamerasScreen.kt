package com.example.rickmasterstest.ui.screens.cameras

import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.rickmasterstest.R
import com.example.rickmasterstest.model.domain.CameraDomain
import com.example.rickmasterstest.model.domain.RoomDomain
import com.example.rickmasterstest.ui.screens.DragAnchors
import com.example.rickmasterstest.ui.screens.FavoritesButton
import com.example.rickmasterstest.ui.screens.FavoritesIcon
import com.example.rickmasterstest.ui.screens.createAnchorDraggableState
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CamerasScreen() {
    val viewModel = hiltViewModel<CamerasViewModel>()
    val state = viewModel.state.observeAsState().value
    val pullRefreshState = rememberPullRefreshState(
        refreshing = state is CamerasState.Loading,
        onRefresh = { viewModel.fetchCameras() }
    )

    LaunchedEffect(true) {
        viewModel.getLocalCameras()
    }

    Box(modifier = Modifier
        .fillMaxSize()
        .pullRefresh(pullRefreshState)) {
        PullRefreshIndicator(true, pullRefreshState, Modifier.align(Alignment.TopCenter))
        when(state) {
            is CamerasState.Default -> DefaultScreen(
                state = state,
                updateCameraFavorites = viewModel::updateCameraFavorites
            )
            is CamerasState.Loading, null -> EmptyScreen()
            is CamerasState.Error -> ErrorScreen(state = state)
        }
    }
}

@Composable
fun DefaultScreen(
    state: CamerasState.Default,
    updateCameraFavorites: (CameraDomain, Boolean) -> Unit
) {
    val rooms = state.roomList
    LazyColumn(modifier = Modifier
        .fillMaxSize()
        .padding(horizontal = 24.dp)
    ) {
        items(rooms.size) { index ->
            RoomItem(
                room = rooms[index],
                updateCameraFavorites = updateCameraFavorites
            )
        }
    }
}

@Composable
fun EmptyScreen() {

}

@Composable
fun ErrorScreen(state: CamerasState.Error) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(1) {
            Text(modifier = Modifier
                .padding(24.dp), text = state.exception.toString())
        }
    }
}

@Composable
fun RoomItem(
    room: RoomDomain,
    updateCameraFavorites: (CameraDomain, Boolean) -> Unit
) {
    Column(modifier = Modifier.padding(horizontal = 8.dp, vertical = 16.dp)) {
        Text(text = room.name)
        room.cameras.forEach {
            DraggableCameraItem(
                camera = it,
                updateCameraFavorites = updateCameraFavorites
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DraggableCameraItem(
    camera: CameraDomain,
    updateCameraFavorites: (CameraDomain, Boolean) -> Unit
) {
    val density = LocalDensity.current
    val anchors = DraggableAnchors {
        DragAnchors.Start at 0f
        DragAnchors.End at -160f
    }
    val state = remember { createAnchorDraggableState(density = density, anchors = anchors) }
    
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.CenterEnd
    ) {
        FavoritesButton(
            isFavorite = camera.favorites,
            onClick = {
                updateCameraFavorites(camera, !camera.favorites)
            }
        )
        CameraItem(
            modifier = Modifier
                .offset {
                    IntOffset(x = state.requireOffset().roundToInt(), y = 0)
                }
                .anchoredDraggable(state, Orientation.Horizontal),
            camera = camera
        )
    }
}

@Composable
fun CameraItem(
    modifier: Modifier = Modifier,
    camera: CameraDomain
) {
    Card(
        elevation = CardDefaults.cardElevation(4.dp),
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 16.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column {
            Snapshot(
                snapshot = camera.snapshot,
                favorites = camera.favorites,
                rec = camera.rec
            )
            Text(
                modifier = Modifier.padding(24.dp),
                text = camera.name
            )
        }
    }
}

@Composable
fun Snapshot(snapshot: String, favorites: Boolean, rec: Boolean) {
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
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            RecIcon(isRecording = rec)
            FavoritesIcon(isFavorite = favorites)
        }
    }
}

@Composable
fun RecIcon(isRecording: Boolean) {
    Box(modifier = Modifier
        .padding(24.dp)
        .size(24.dp)) {
        if (isRecording) {
            Image(
                modifier = Modifier
                    .size(24.dp),
                painter = painterResource(id = R.drawable.rec),
                contentDescription = stringResource(id = R.string.favorite_description)
            )
        }
    }
}