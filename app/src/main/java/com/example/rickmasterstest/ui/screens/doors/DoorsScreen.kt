package com.example.rickmasterstest.ui.screens.doors

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import com.example.rickmasterstest.model.domain.DoorDomain
import com.example.rickmasterstest.ui.screens.cameras.LoadingScreen

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DoorsScreen() {
    val viewModel = hiltViewModel<DoorsViewModel>()
    val state = viewModel.state.observeAsState().value
    val pullRefreshState = rememberPullRefreshState(
        refreshing = state is DoorsState.Loading,
        onRefresh = { viewModel.fetchDoors() }
    )

    LaunchedEffect(true) {
        viewModel.fetchDoors()
    }

    Box(modifier = Modifier
        .fillMaxSize()
        .pullRefresh(pullRefreshState)) {
        when(state) {
            is DoorsState.Default -> DefaultScreen(state = state)
            is DoorsState.Loading, null -> LoadingScreen(pullRefreshState = pullRefreshState)
            is DoorsState.Error -> ErrorScreen(state = state)
        }
    }
}

@Composable
fun DefaultScreen(state: DoorsState.Default) {
    val doors = state.doorList
    LazyColumn(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {
        items(doors.size) { index ->
            DoorItem(door = doors[index])
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
fun ErrorScreen(state: DoorsState.Error) {
    Text(text = state.exception.toString())
}

@Composable
fun DoorItem(door: DoorDomain) {
    Card(
        elevation = CardDefaults.cardElevation(10.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column {
            Box(
                contentAlignment = Alignment.TopEnd
            ) {
                if (door.snapshot != null) {
                    AsyncImage(
                        modifier = Modifier.fillMaxWidth(),
                        contentScale = ContentScale.Crop,
                        model = door.snapshot,
                        contentDescription = stringResource(id = R.string.camera_screenshot_description)
                    )
                }
                if (door.favorites) {
                    Image(
                        modifier = Modifier.padding(16.dp).size(32.dp),
                        painter = painterResource(id = R.drawable.star),
                        contentDescription = stringResource(id = R.string.camera_favorite_description)
                    )
                }
            }
            Text(
                modifier = Modifier.padding(16.dp),
                text = door.name
            )
        }

    }
}