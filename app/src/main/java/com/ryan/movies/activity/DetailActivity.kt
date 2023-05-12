package com.ryan.movies.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import androidx.appcompat.app.AppCompatActivity
import com.ryan.movies.R
import com.ryan.movies.constant.Constant
import com.ryan.movies.databinding.ActivityDetailBinding
import com.ryan.movies.model.response.DetailMovieResponse
import com.ryan.movies.model.response.ReviewResponse
import com.ryan.movies.retrofit.ApiService
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.DecimalFormat

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private val TAG: String = "DetailActivity"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(findViewById(R.id.toolbar))
        binding.toolbarLayout.title = ""
        setupView()
        setupListener()
    }

    override fun onStart() {
        super.onStart()
        getMovieDetail()
        getReview()
    }

    private fun setupView() {
        supportActionBar!!.title = ""
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    private fun setupListener() {
        val fab = findViewById<FloatingActionButton>(R.id.fab_play)
        fab.setOnClickListener {
            startActivity(Intent(applicationContext, TrailerActivity::class.java))
        }
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
        val decimalFormat = DecimalFormat("#.#")

        Picasso.get()
            .load(backdropPath)
            .fit()
            .centerCrop()
            .into(imageBackdrop)
        textTitle.text = detailMovie.title
        textVote.text = decimalFormat.format(detailMovie.vote_average)
        textOverview.text = detailMovie.overview

        for (genre in detailMovie.genres!!) {
            textGenre.text = "${genre.name} "
        }
    }

    fun getReview() {
        ApiService().endpoint.getReview(Constant.MOVIE_ID, Constant.API_KEY)
            .enqueue(object : Callback<ReviewResponse> {
                override fun onResponse(
                    call: Call<ReviewResponse>,
                    response: Response<ReviewResponse>
                ) {
                    if (response.isSuccessful) {
                        showReview(response.body()!!)
                    }
                }

                override fun onFailure(call: Call<ReviewResponse>, t: Throwable) {
                    Log.d(TAG, t.toString())
                }

            })
    }

    fun showReview(response: ReviewResponse) {
        val textAuthor = findViewById<TextView>(R.id.textAuthor)
        val textContent = findViewById<TextView>(R.id.textContent)
//        val stringBuilder = StringBuilder() // ketika menggunakan stringBuilder
        for (rs in response.results) {
//            stringBuilder.append("- ").append(rs).append("\n\n")
            textAuthor.text = rs.author
            textContent.text = rs.content
            Log.d(TAG, "showReview: ${rs.author}")
        }
//        textAuthor.text = stringBuilder.toString()
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }
}