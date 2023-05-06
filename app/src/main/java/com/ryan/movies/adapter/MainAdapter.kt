package com.ryan.movies.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ryan.movies.R
import com.ryan.movies.model.Movie

class MainAdapter (var movies: ArrayList<Movie>) : RecyclerView.Adapter<MainAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder (
        LayoutInflater.from(parent.context).inflate(R.layout.adapter_main, parent, false)
    )

    override fun getItemCount() = movies.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(movies[position])
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
}