package com.ryan.movies.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.ryan.movies.R
import com.ryan.movies.constant.Constant
import com.ryan.movies.model.response.TrailerResponse
import com.ryan.movies.retrofit.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TrailerActivity : AppCompatActivity() {
    private val TAG: String = "TrailerActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trailer)
        setupView()
        setupListener()
    }

    override fun onStart() {
        super.onStart()
        getTrailer()
    }

    private fun setupView() {}


    private fun setupListener() {}


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
        when (loading) {
            true -> {

            }
            false -> {

            }
        }
    }

    private fun showTrailer(trailer: TrailerResponse) {
        for (res in trailer.results) {
            Log.d(TAG, "nameVideo: ${res.name}")
        }
    }
}