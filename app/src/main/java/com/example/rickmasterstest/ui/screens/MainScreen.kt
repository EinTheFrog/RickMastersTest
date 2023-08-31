package com.example.rickmasterstest.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.currentRecomposeScope
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.rickmasterstest.R
import com.example.rickmasterstest.ui.screens.cameras.CamerasScreen
import com.example.rickmasterstest.ui.screens.doors.DoorsScreen
import com.example.rickmasterstest.ui.theme.RickMastersTestTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(modifier: Modifier = Modifier, name: String) {
    val pagerState = rememberPagerState(pageCount = { 2 })

    Scaffold(
        topBar = {
            TopBar(title = stringResource(id = R.string.my_house), pagerState = pagerState)
        }
    ) { paddingValues ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(paddingValues)) {
            HorizontalPager(state = pagerState) { page ->
                // Our page content
                when(page) {
                    0 -> CamerasScreen()
                    1 -> DoorsScreen()
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TopBar(title: String, pagerState: PagerState) {
    Surface(
        shadowElevation = 4.dp
    ) {
        Column {
            TopBarTitle(title = title)
            HorizontalPagerBar(modifier = Modifier.fillMaxWidth(), state = pagerState)
        }
    }
}

@Composable
fun TopBarTitle(title: String) {
    Text(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        text = title,
        style = MaterialTheme.typography.titleLarge,
        textAlign = TextAlign.Center
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HorizontalPagerBar(modifier: Modifier = Modifier, state: PagerState) {
    val coroutineScope = rememberCoroutineScope()

    Row(modifier = modifier) {
        for (i in 0 until state.pageCount) {
            val title = when(i) {
                0 -> stringResource(id = R.string.cameras)
                1 -> stringResource(id = R.string.doors)
                else -> "Not Defined"
            }
            val isCurrent = i == state.currentPage
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clickable { coroutineScope.launch { state.scrollToPage(i) } }
            ) {
                PagerBarItem(
                    title = title,
                    isCurrent = isCurrent
                )
            }
        }
    }
}

@Composable
fun PagerBarItem(title: String, isCurrent: Boolean) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val color = if (isCurrent) MaterialTheme.colorScheme.primary else Color.Transparent
        Text(modifier = Modifier.padding(8.dp), text = title)
        Divider(color = color, thickness = 4.dp)
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    RickMastersTestTheme {
        MainScreen(name = "Android")
    }
}