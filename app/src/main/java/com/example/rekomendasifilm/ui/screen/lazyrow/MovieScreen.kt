package com.example.rekomendasifilm.ui.screen.lazyrow

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.rekomendasifilm.R
import com.example.rekomendasifilm.ViewModelFactory
import com.example.rekomendasifilm.di.Injection
import com.example.rekomendasifilm.model.Movie
import com.example.rekomendasifilm.ui.common.UiState
import com.example.rekomendasifilm.ui.component.EmptyItem
import com.example.rekomendasifilm.ui.component.MovieCardItem
import com.example.rekomendasifilm.ui.component.SearchButton

@Composable
fun MovieScreen(
    viewModel: MovieViewModel = viewModel(
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
                LazyColumnContent(
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
fun LazyColumnContent(
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
            LazyRowContent(
                listMovie = listMovie,
                onFavoriteIconClicked = onFavoriteIconClicked,
                navigateToDetail = navigateToDetail
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "On Premiere",
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(16.dp),
                style = MaterialTheme.typography.h5
            )


            Spacer(modifier = Modifier.height(8.dp))

            LazyVerticalGridContent(
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

@Composable
fun LazyRowContent(
    listMovie: List<Movie>,
    onFavoriteIconClicked: (id: Int, newState: Boolean) -> Unit,
    modifier: Modifier = Modifier,
    navigateToDetail: (Int) -> Unit,
) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier
            .testTag("lazy_list_row")
    ) {
        items(listMovie, key = { it.id }) { movie ->
            Column(
                modifier = Modifier
                    .clickable { navigateToDetail(movie.id) }
            ) {
                MovieCardItem(
                    id = movie.id,
                    title = movie.title,
                    image = movie.image,
                    sutradara = movie.sutradara,
                    tanggalRilis = movie.tanggalRilis,
                    genre = movie.genre,
                    isFavorite = movie.isFavorite,
                    onFavoriteIconClicked = onFavoriteIconClicked,
                )
            }
        }
    }
}

@Composable
fun LazyVerticalGridContent(
    listMovie: List<Movie>,
    onFavoriteIconClicked: (id: Int, newState: Boolean) -> Unit,
    modifier: Modifier = Modifier,
    navigateToDetail: (Int) -> Unit,
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        modifier = modifier
            .testTag("lazy_grid")
    ) {
        items(listMovie, key = { it.id }) { movie ->
            Column(
                modifier = Modifier
                    .padding(8.dp)
                    .clickable { navigateToDetail(movie.id) }
            ) {
                MovieCardItem(
                    id = movie.id,
                    title = movie.title,
                    image = movie.image,
                    sutradara = movie.sutradara,
                    tanggalRilis = movie.tanggalRilis,
                    genre = movie.genre,
                    isFavorite = movie.isFavorite,
                    onFavoriteIconClicked = onFavoriteIconClicked,
                )
            }
        }
    }
}
