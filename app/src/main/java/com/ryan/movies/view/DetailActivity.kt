package com.ryan.movies.view

import android.os.Bundle
import android.util.Log
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import com.ryan.movies.R
import com.ryan.movies.constant.Constant
import com.ryan.movies.databinding.ActivityDetailBinding
import com.ryan.movies.model.response.DetailMovieResponse
import com.ryan.movies.retrofit.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private val TAG: String = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(findViewById(R.id.toolbar))
        binding.toolbarLayout.title = title
        binding.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
    }

    override fun onStart() {
        super.onStart()
        getMovieDetail()
    }

    private fun getMovieDetail() {
        ApiService().endpoint.getDetailMovie(Constant.MOVIE_ID, Constant.API_KEY)
            .enqueue(object : Callback<DetailMovieResponse> {
                override fun onResponse(
                    call: Call<DetailMovieResponse>,
                    response: Response<DetailMovieResponse>
                ) {
                    if (response.isSuccessful) {
                        showMovie(response.body()!!)
                    }
                }

                override fun onFailure(call: Call<DetailMovieResponse>, t: Throwable) {
                    Log.d(TAG, t.toString())
                }

            })
    }

    fun showMovie(detailMovie: DetailMovieResponse) {
        Log.d(TAG, "overViewResponse: ${detailMovie.overview}")
    }
}