package com.example.rekomendasifilm.di

import com.example.rekomendasifilm.data.MovieRepository

object Injection {
    fun provideRepository(): MovieRepository {
        return MovieRepository.getInstance()
    }
}