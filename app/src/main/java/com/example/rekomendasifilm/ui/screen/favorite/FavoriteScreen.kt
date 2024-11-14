package com.example.rekomendasifilm.ui.screen.favorite

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.rekomendasifilm.ui.screen.home.ListMovie
import com.example.rekomendasifilm.R
import com.example.rekomendasifilm.ui.common.UiState
import com.example.rekomendasifilm.ui.component.EmptyItem
import com.example.rekomendasifilm.ViewModelFactory
import com.example.rekomendasifilm.di.Injection
import com.example.rekomendasifilm.model.Movie

@Composable
fun FavoriteScreen(
    navigateToDetail: (Int) -> Unit,
    viewModel: FavoriteViewModel = viewModel(
        factory = ViewModelFactory(Injection.provideRepository())
    )
) {
    viewModel.uiState.collectAsState(initial = UiState.Loading).value.let { uiState ->
        when (uiState) {
            is UiState.Loading -> {
                viewModel.getFavoriteMovie()
            }
            is UiState.Success -> {
                FavoriteScreen(
                    listMovie = uiState.data,
                    navigateToDetail = navigateToDetail,
                    onFavoriteIconClicked = { id, newState ->
                        viewModel.updateMovie(id, newState)
                    }
                )
            }
            is UiState.Error -> {}
        }
    }
}

@Composable
fun FavoriteScreen(
    listMovie: List<Movie>,
    navigateToDetail: (Int) -> Unit,
    onFavoriteIconClicked: (id: Int, newState: Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
    ) {
        if (listMovie.isNotEmpty()) {
            ListMovie(
                listMovie = listMovie,
                onFavoriteIconClicked = onFavoriteIconClicked,
                contentPaddingTop = 16.dp,
                navigateToDetail = navigateToDetail
            )
        } else {
            EmptyItem(
                warning = stringResource(R.string.empty_favorite)
            )
        }
    }
}