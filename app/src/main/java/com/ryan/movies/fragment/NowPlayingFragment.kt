package com.ryan.movies.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ryan.movies.R
import com.ryan.movies.activity.DetailActivity
import com.ryan.movies.adapter.MainAdapter
import com.ryan.movies.constant.Constant
import com.ryan.movies.model.Movie
import com.ryan.movies.model.response.MovieResponse
import com.ryan.movies.retrofit.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NowPlayingFragment : Fragment() {

    private lateinit var mainAdapter: MainAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_now_playing, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.recyclerViewFragmentNp)
        progressBar = view.findViewById(R.id.progressBarFragmentNp)

        setupRecyclerView()
        getMovieNowPlaying()
    }

    private fun setupRecyclerView() {
        mainAdapter = MainAdapter(arrayListOf(), object: MainAdapter.OnAdapterListener {
            override fun onClick(movie: Movie) {
                Constant.MOVIE_ID = movie.id!!
                Constant.MOVIE_TITLE = movie.title!!
                startActivity(Intent(requireContext(), DetailActivity::class.java))
            }
        })

        recyclerView.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = mainAdapter
        }
    }

    private fun getMovieNowPlaying() {
        showLoading(true)
        ApiService().endpoint.getMovieNowPlaying(Constant.API_KEY, 1)
            .enqueue(object : Callback<MovieResponse> {

                override fun onResponse(
                    call: Call<MovieResponse>,
                    response: Response<MovieResponse>
                ) {
                    showLoading(false)
                    if (response.isSuccessful) {
                        showMovie(response.body()!!)
                    }
                }

                override fun onFailure(call: Call<MovieResponse>, t: Throwable) {
                    showLoading(false)
                }

            })
    }

    private fun showLoading(loading: Boolean) {
        when (loading) {
            true -> progressBar.visibility = View.VISIBLE
            false -> progressBar.visibility = View.GONE
        }
    }

    private fun showMovie(response: MovieResponse) {
        mainAdapter.setData(response.results)
    }
}