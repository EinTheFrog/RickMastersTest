package com.example.rickmasterstest.ui.screens.cameras

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.PullRefreshState
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.rickmasterstest.R
import com.example.rickmasterstest.model.domain.CameraDomain
import com.example.rickmasterstest.model.domain.RoomDomain

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
        viewModel.fetchCameras()
    }

    Box(modifier = Modifier
        .fillMaxSize()
        .pullRefresh(pullRefreshState)) {
        when(state) {
            is CamerasState.Default -> DefaultScreen(state = state)
            is CamerasState.Loading, null -> LoadingScreen(pullRefreshState = pullRefreshState)
            is CamerasState.Error -> ErrorScreen(state = state)
        }
    }
}

@Composable
fun DefaultScreen(state: CamerasState.Default) {
    val rooms = state.roomList
    LazyColumn(modifier = Modifier
        .fillMaxSize()
        .padding(horizontal = 24.dp)
    ) {
        items(rooms.size) { index ->
            RoomItem(room = rooms[index])
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun LoadingScreen(pullRefreshState: PullRefreshState) {
    Box(modifier = Modifier.fillMaxSize()) {
        PullRefreshIndicator(true, pullRefreshState, Modifier.align(Alignment.TopCenter))
    }
}

@Composable
fun ErrorScreen(state: CamerasState.Error) {
    Text(text = state.exception.toString())
}

@Composable
fun RoomItem(room: RoomDomain) {
    Column(modifier = Modifier.padding(horizontal = 8.dp, vertical = 16.dp)) {
        Text(text = room.name)
        room.cameras.forEach {
            CameraItem(camera = it)
        }
    }
}

@Composable
fun CameraItem(camera: CameraDomain) {
    Card(
        elevation = CardDefaults.cardElevation(4.dp),
        modifier = Modifier
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
fun FavoritesIcon(isFavorite: Boolean) {
    Box(modifier = Modifier.padding(24.dp).size(24.dp)) {
        if (isFavorite) {
            Image(
                modifier = Modifier
                    .size(24.dp),
                painter = painterResource(id = R.drawable.star),
                contentDescription = stringResource(id = R.string.favorite_description)
            )
        }
    }
}

@Composable
fun RecIcon(isRecording: Boolean) {
    Box(modifier = Modifier.padding(24.dp).size(24.dp)) {
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