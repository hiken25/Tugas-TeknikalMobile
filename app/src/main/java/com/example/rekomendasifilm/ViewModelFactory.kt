package com.example.rekomendasifilm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.rekomendasifilm.ui.screen.detail.DetailViewModel
import com.example.rekomendasifilm.ui.screen.home.HomeViewModel
import com.example.rekomendasifilm.data.MovieRepository
import com.example.rekomendasifilm.ui.screen.favorite.FavoriteViewModel
import com.example.rekomendasifilm.ui.screen.lazyrow.MovieViewModel

class ViewModelFactory(private val repository: MovieRepository) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(repository) as T
        } else if (modelClass.isAssignableFrom(MovieViewModel::class.java)) {
            return MovieViewModel(repository) as T
        } else if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
            return DetailViewModel(repository) as T
        } else if (modelClass.isAssignableFrom(FavoriteViewModel::class.java)) {
            return FavoriteViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }
}