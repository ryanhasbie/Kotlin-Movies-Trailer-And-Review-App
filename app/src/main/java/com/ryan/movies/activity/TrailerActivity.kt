package com.ryan.movies.activity


import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import com.ryan.movies.R
import com.ryan.movies.adapter.TrailerAdapter
import com.ryan.movies.constant.Constant
import com.ryan.movies.model.response.TrailerResponse
import com.ryan.movies.retrofit.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class TrailerActivity : AppCompatActivity() {
    private val TAG: String = "TrailerActivity"

    lateinit var trailerAdapter: TrailerAdapter
    lateinit var youTubePlayer: YouTubePlayer
    private var youTubeKey: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trailer)
        setupView()
        setupRecyclerView()
    }

    override fun onStart() {
        super.onStart()
        getTrailer()
    }

    private fun setupView() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val youTubePlayerView = findViewById<YouTubePlayerView>(R.id.youtube_player_view)
        lifecycle.addObserver(youTubePlayerView)

        youTubePlayerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
            override fun onReady(player: YouTubePlayer) {
                youTubePlayer = player
                youTubeKey?.let {
                    youTubePlayer.cueVideo(it, 0f)
                }
            }
        })
    }

    private fun setupRecyclerView() {
        val listVideo = findViewById<RecyclerView>(R.id.list_video)
        trailerAdapter = TrailerAdapter(arrayListOf(), object: TrailerAdapter.OnAdapterListener {
            override fun onLoad(key: String) {
                youTubeKey = key
                Log.d(TAG, "EEEE: $youTubeKey")
            }

            override fun onPlay(key: String) {
                youTubePlayer.loadVideo(key, 0f)
            }


        })
        listVideo.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = trailerAdapter
        }
    }


    private fun getTrailer() {
        showLoading(true)
        ApiService().endpoint.getTrailer(Constant.MOVIE_ID, Constant.API_KEY)
            .enqueue(object : Callback<TrailerResponse> {
                override fun onResponse(
                    call: Call<TrailerResponse>,
                    response: Response<TrailerResponse>
                ) {
                    showLoading(false)
                    if (response.isSuccessful) {
                        showTrailer(response.body()!!)
                    }
                }

                override fun onFailure(call: Call<TrailerResponse>, t: Throwable) {
                    showLoading(false)
                }

            })


    }

    private fun showLoading(loading: Boolean) {
        val progressBarVideo = findViewById<ProgressBar>(R.id.progress_video)
        when (loading) {
            true -> {
                progressBarVideo.visibility = View.VISIBLE
            }
            false -> {
                progressBarVideo.visibility = View.GONE
            }
        }
    }

    private fun showTrailer(trailer: TrailerResponse) {
        trailerAdapter.setData(trailer.results)
//        Log.d(TAG, "ShowResult: ${trailer.results}")
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }
}