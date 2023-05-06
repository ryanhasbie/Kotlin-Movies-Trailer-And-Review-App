package com.ryan.movies.retrofit

import com.ryan.movies.model.response.MovieResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiEndpoint {
    @GET("now_playing")
    fun getMovieNowPlaying(
        @Query("api_key") api_key:String,
        @Query("page") page: Int
    ): Call<MovieResponse>

    @GET("popular")
    fun getMoviePopular(
        @Query("api_key") api_key:String,
        @Query("page") page: Int
    ): Call<MovieResponse>


}