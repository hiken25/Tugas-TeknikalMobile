package com.example.rekomendasifilm.ui.screen.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rekomendasifilm.data.MovieRepository
import com.example.rekomendasifilm.model.Movie
import com.example.rekomendasifilm.ui.common.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DetailViewModel(
    private val repository: MovieRepository
) : ViewModel() {
    private val _uiState: MutableStateFlow<UiState<Movie>> =
        MutableStateFlow(UiState.Loading)
    val uiState: StateFlow<UiState<Movie>>
        get() = _uiState

    fun getMovieById(id: Int) = viewModelScope.launch {
        _uiState.value = UiState.Loading
        _uiState.value = UiState.Success(repository.getMovieById(id))
    }


    fun updateMovie(id: Int, newState: Boolean) = viewModelScope.launch {
        repository.updateMovie(id, !newState)
            .collect { isUpdated ->
                if (isUpdated) getMovieById(id)
            }
    }

}