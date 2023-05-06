package com.ryan.movies.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ryan.movies.R
import com.ryan.movies.constant.Constant
import com.ryan.movies.model.Movie
import com.squareup.picasso.Picasso

class MainAdapter (var movies: ArrayList<Movie>, var listener: OnAdapterListener) : RecyclerView.Adapter<MainAdapter.ViewHolder>() {
    private val TAG: String = "MainActivity"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder (
        LayoutInflater.from(parent.context).inflate(R.layout.adapter_main, parent, false)
    )

    override fun getItemCount() = movies.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val movie = movies[position]
        holder.bind(movie)
        val posterPath = Constant.POSTER_PATH + movie.poster_path
        val imageView = holder.itemView.findViewById<ImageView>(R.id.img_poster)
        Picasso.get().load(posterPath).into(imageView)
        imageView.setOnClickListener {
            listener.onClick(movie)
        }
    }

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        private var textTitle = view.findViewById<TextView>(R.id.text_title)
        fun bind(movies: Movie) {
            textTitle.text = movies.title
        }
    }

    public fun setData(newMovies: List<Movie>) {
        movies.clear()
        movies.addAll(newMovies)
        notifyDataSetChanged()
    }

    interface OnAdapterListener {
        fun onClick (movie: Movie)
    }
}