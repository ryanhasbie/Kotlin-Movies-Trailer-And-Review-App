package com.ryan.movies.model.response

import com.ryan.movies.model.Movie

data class MovieResponse (
    val results: List<Movie>,
    val total_pages:Int?
)