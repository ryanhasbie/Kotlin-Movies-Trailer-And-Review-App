package com.ryan.movies.model.response

import com.ryan.movies.model.Genre
import com.ryan.movies.model.Review

data class DetailMovieResponse (
    val id: Int?,
    val title: String?,
    val backdrop_path: String?,
    val poster_path: String?,
    val overview: String?,
    val release_date: String?,
    val genres: List<Genre>?,
    val vote_average: Double?,
)