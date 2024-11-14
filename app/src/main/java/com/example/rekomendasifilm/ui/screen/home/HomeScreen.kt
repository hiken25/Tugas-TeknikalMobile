package com.example.rekomendasifilm.ui.screen.home

import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.rekomendasifilm.R
import com.example.rekomendasifilm.ViewModelFactory
import com.example.rekomendasifilm.di.Injection
import com.example.rekomendasifilm.model.Movie
import com.example.rekomendasifilm.ui.common.UiState
import com.example.rekomendasifilm.ui.component.EmptyItem
import com.example.rekomendasifilm.ui.component.MovieItem
import com.example.rekomendasifilm.ui.component.SearchButton

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = viewModel(
        factory = ViewModelFactory(Injection.provideRepository())
    ),
    navigateToDetail: (Int) -> Unit,
) {
    val query by viewModel.query
    viewModel.uiState.collectAsState(initial = UiState.Loading).value.let { uiState ->
        when (uiState) {
            is UiState.Loading -> {
                viewModel.search(query)
            }
            is UiState.Success -> {
                HomeContent(
                    query = query,
                    onQueryChange = viewModel::search,
                    listMovie = uiState.data,
                    onFavoriteIconClicked = { id, newState ->
                        viewModel.updateMovie(id, newState)
                    },
                    navigateToDetail = navigateToDetail
                )
            }
            is UiState.Error -> {}
        }
    }
}

@Composable
fun HomeContent(
    query: String,
    onQueryChange: (String) -> Unit,
    listMovie: List<Movie>,
    onFavoriteIconClicked: (id: Int, newState: Boolean) -> Unit,
    navigateToDetail: (Int) -> Unit,
) {
    Column {
        SearchButton(
            query = query,
            onQueryChange = onQueryChange
        )
        if (listMovie.isNotEmpty()) {
            ListMovie(
                listMovie = listMovie,
                onFavoriteIconClicked = onFavoriteIconClicked,
                navigateToDetail = navigateToDetail
            )
        } else {
            EmptyItem(
                warning = stringResource(R.string.empty_item),
                modifier = Modifier
                    .testTag("emptyItem")
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ListMovie(
    listMovie: List<Movie>,
    onFavoriteIconClicked: (id: Int, newState: Boolean) -> Unit,
    modifier: Modifier = Modifier,
    navigateToDetail: (Int) -> Unit,
    contentPaddingTop: Dp = 0.dp,
) {
    LazyColumn(
        contentPadding = PaddingValues(
            start = 16.dp,
            end = 16.dp,
            bottom = 16.dp,
            top = contentPaddingTop
        ),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier
            .testTag("lazy_list_column")
    ) {
        items(listMovie, key = { it.id }) { movie ->
            MovieItem(
                id = movie.id,
                title = movie.title,
                image = movie.image,
                sutradara = movie.sutradara,
                tanggalRilis = movie.tanggalRilis,
                genre = movie.genre,
                isFavorite = movie.isFavorite,
                onFavoriteIconClicked = onFavoriteIconClicked,
                modifier = Modifier
                    .animateItemPlacement(tween(durationMillis = 200))
                    .clickable { navigateToDetail(movie.id) }
            )
        }
    }
}