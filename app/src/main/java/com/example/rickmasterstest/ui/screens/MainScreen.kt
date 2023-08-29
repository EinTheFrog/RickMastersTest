package com.example.rickmasterstest.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.rickmasterstest.R
import com.example.rickmasterstest.ui.theme.RickMastersTestTheme

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(modifier: Modifier = Modifier, name: String) {
    val pagerState = rememberPagerState(pageCount = { 2 })

    Scaffold(
        topBar = { TopBar(stringResource(id = R.string.my_house)) }
    ) { paddingValues ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(paddingValues)) {
            HorizontalPagerBar(modifier = Modifier.fillMaxWidth(), state = pagerState)
            HorizontalPager(state = pagerState) { page ->
                // Our page content
                Text(
                    text = "Page: $page",
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(title: String) {
    TopAppBar(title = {
        Text(modifier = Modifier.fillMaxWidth(), text = title, textAlign = TextAlign.Center)
    })
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HorizontalPagerBar(modifier: Modifier = Modifier, state: PagerState) {
    Row(modifier = modifier) {
        for (i in 0 until state.pageCount) {
            val title = when(i) {
                0 -> stringResource(id = R.string.cameras)
                1 -> stringResource(id = R.string.doors)
                else -> "Not Defined"
            }
            val isCurrent = i == state.currentPage
            Box(modifier = Modifier.weight(1f)) {
                PagerBarItem(title = title, isCurrent = isCurrent)
            }
        }
    }
}

@Composable
fun PagerBarItem(title: String, isCurrent: Boolean) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val color =
            if (isCurrent) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.background

        Text(title)
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