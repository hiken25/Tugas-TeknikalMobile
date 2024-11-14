package com.example.rekomendasifilm.model

data class Movie(
    val id: Int,
    val title: String,
    val synopsis: String,
    val image: Int,
    val sutradara: String,
    val tanggalRilis: String,
    val genre: String,
    var isFavorite: Boolean = false
)