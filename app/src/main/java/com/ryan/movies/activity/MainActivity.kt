package com.ryan.movies.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.navigation.ui.AppBarConfiguration
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import android.widget.ScrollView
import android.widget.Toast
import androidx.core.widget.NestedScrollView
import androidx.core.widget.NestedScrollView.OnScrollChangeListener
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ryan.movies.R
import com.ryan.movies.adapter.MainAdapter
import com.ryan.movies.constant.Constant
import com.ryan.movies.databinding.ActivityMainBinding
import com.ryan.movies.model.Movie
import com.ryan.movies.model.response.MovieResponse
import com.ryan.movies.retrofit.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


const val moviePopular =0
const val movieNowPlaying = 1;

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private val TAG: String = "MainActivity"
    lateinit var mainAdapter: MainAdapter
    private var movieCategory = 0
    private val api = ApiService().endpoint
    private var isScrolling = false
    private var currentPage = 1
    private var totalPages = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        setupView()
        setupRecyclerView()
        setupListener()

//        val navController = findNavController(R.id.nav_host_fragment_content_main)
//        appBarConfiguration = AppBarConfiguration(navController.graph)
//        setupActionBarWithNavController(navController, appBarConfiguration)

//        binding.fab.setOnClickListener { view ->
//            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                .setAnchorView(R.id.fab)
//                .setAction("Action", null).show()
//        }
    }

    override fun onStart() {
        super.onStart()
        getMovie()
        showLoadingNextPage(false)
    }

    private fun setupView() {}

    private fun setupRecyclerView() {
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        mainAdapter = MainAdapter(arrayListOf(), object: MainAdapter.OnAdapterListener {
            override fun onClick(movie: Movie) {
                showMessage(movie.title!!)
                Constant.MOVIE_ID = movie.id!!
                Constant.MOVIE_TITLE = movie.title!!
                startActivity(Intent(applicationContext, DetailActivity::class.java))
            }

        })
        recyclerView.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = mainAdapter
        }
    }

    private fun setupListener() {
        val scrollView = findViewById<NestedScrollView>(R.id.scrollView)
        scrollView.setOnScrollChangeListener(object : OnScrollChangeListener{
            override fun onScrollChange(
                v: NestedScrollView,
                scrollX: Int,
                scrollY: Int,
                oldScrollX: Int,
                oldScrollY: Int
            ) {
                if (scrollY == v!!.getChildAt(0).measuredHeight - v.measuredHeight) {
                    if (!isScrolling) {
                        if (currentPage <= totalPages) {
                            getMovieNextPage()
                        }
                    }
                }
            }

        })
    }

    private fun getMovie() {
        val scrollView = findViewById<NestedScrollView>(R.id.scrollView)
        scrollView.scrollTo(0,0)
        currentPage = 1
        showLoading(true)

        var apiCall: Call<MovieResponse>? = null
        when(movieCategory) {
            moviePopular -> {
                apiCall = api.getMoviePopular(Constant.API_KEY, 1)
            }
            movieNowPlaying -> {
                apiCall = api.getMovieNowPlaying(Constant.API_KEY, 1)
            }
        }


        apiCall!!
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
                    Log.d(TAG, t.toString())
                    showLoading(false)
                }

            })
    }

    fun getMovieNextPage() {
        currentPage += 1
        showLoadingNextPage(true)

        var apiCall: Call<MovieResponse>? = null
        when(movieCategory) {
            moviePopular -> {
                apiCall = api.getMoviePopular(Constant.API_KEY, currentPage)
            }
            movieNowPlaying -> {
                apiCall = api.getMovieNowPlaying(Constant.API_KEY, currentPage)
            }
        }


        apiCall!!
            .enqueue(object : Callback<MovieResponse> {

                override fun onResponse(
                    call: Call<MovieResponse>,
                    response: Response<MovieResponse>
                ) {
                    showLoadingNextPage(false)
                    if (response.isSuccessful) {
                        showMovieNextPage(response.body()!!)
                    }
                }

                override fun onFailure(call: Call<MovieResponse>, t: Throwable) {
                    Log.d(TAG, t.toString())
                    showLoadingNextPage(false)
                }

            })
    }

    fun showLoading(loading: Boolean) {
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)
        when (loading) {
            true -> progressBar.visibility = View.VISIBLE
            false -> progressBar.visibility = View.GONE
        }

    }

    fun showLoadingNextPage(loading: Boolean) {
        val progressBar = findViewById<ProgressBar>(R.id.progressBarNextPage)
        when (loading) {
            true -> {
                isScrolling = true
                progressBar.visibility = View.VISIBLE
            }
            false -> {
                isScrolling = false
                progressBar.visibility = View.GONE
            }
        }

    }

    fun showMovie(response: MovieResponse) {
//        Log.d(TAG, response.toString())
        totalPages = response.total_pages!!.toInt()
        mainAdapter.setData(response.results)
    }

    fun showMovieNextPage(response: MovieResponse) {
//        Log.d(TAG, response.toString())
        totalPages = response.total_pages!!.toInt()
        mainAdapter.setDataNextPage(response.results)
        showMessage("page: $currentPage")
    }

    fun showMessage(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_populars -> {
                showMessage("Movie Populars Selected")
                movieCategory = moviePopular
                getMovie()
                true
            }
            R.id.action_now_playings -> {
                showMessage("Movie Now Playing Selected")
                movieCategory = movieNowPlaying
                getMovie()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

//    override fun onSupportNavigateUp(): Boolean {
//        val navController = findNavController(R.id.nav_host_fragment_content_main)
//        return navController.navigateUp(appBarConfiguration)
//                || super.onSupportNavigateUp()
//    }
}