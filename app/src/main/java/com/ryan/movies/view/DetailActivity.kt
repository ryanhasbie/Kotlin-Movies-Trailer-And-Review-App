package com.ryan.movies.view

import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import com.ryan.movies.R
import com.ryan.movies.constant.Constant
import com.ryan.movies.databinding.ActivityDetailBinding
import com.ryan.movies.model.response.DetailMovieResponse
import com.ryan.movies.retrofit.ApiService
import com.squareup.picasso.Picasso
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
        binding.toolbarLayout.title = ""
        setupView()
    }

    override fun onStart() {
        super.onStart()
        getMovieDetail()
    }

    private fun setupView() {
        supportActionBar!!.title = ""
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
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
        val backdropPath = Constant.BACKDROP_PATH + detailMovie.backdrop_path

        val imageBackdrop = findViewById<ImageView>(R.id.image_backdrop)
        val textTitle = findViewById<TextView>(R.id.textTitle)
        val textVote = findViewById<TextView>(R.id.textVote)
        val textOverview = findViewById<TextView>(R.id.textOverview)
        val textGenre = findViewById<TextView>(R.id.textGenre)

        Picasso.get()
            .load(backdropPath)
            .fit()
            .centerCrop()
            .into(imageBackdrop)
        textTitle.text = detailMovie.title
        textVote.text = detailMovie.vote_average.toString()
        textOverview.text = detailMovie.overview

        for (genre in detailMovie.genres!!) {
            textGenre.text = "${genre.name} "
        }

    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }
}