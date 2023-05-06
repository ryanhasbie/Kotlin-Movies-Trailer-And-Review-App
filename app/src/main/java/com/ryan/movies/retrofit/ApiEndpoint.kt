package com.ryan.movies.retrofit

import com.ryan.movies.model.response.DetailMovieResponse
import com.ryan.movies.model.response.MovieResponse
import com.ryan.movies.model.response.TrailerResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiEndpoint {
    @GET("movie/now_playing")
    fun getMovieNowPlaying(
        @Query("api_key") api_key:String,
        @Query("page") page: Int
    ): Call<MovieResponse>

    @GET("movie/popular")
    fun getMoviePopular(
        @Query("api_key") api_key:String,
        @Query("page") page: Int
    ): Call<MovieResponse>

    @GET("movie/{movieId}")
    fun getDetailMovie (
        @Path("movieId") movieId: Int,
        @Query("api_key") api_key:String,
    ): Call<DetailMovieResponse>

    @GET("movie/{movieId}/videos")
    fun getTrailer (
        @Path("movieId") movieId: Int,
        @Query("api_key") api_key:String,
    ): Call<TrailerResponse>


}