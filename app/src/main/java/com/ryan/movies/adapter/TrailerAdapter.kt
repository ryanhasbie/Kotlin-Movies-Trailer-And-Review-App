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
import com.ryan.movies.model.Trailer
import com.squareup.picasso.Picasso

class TrailerAdapter (private var videos: ArrayList<Trailer>, var listener: OnAdapterListener) : RecyclerView.Adapter<TrailerAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder (
        LayoutInflater.from(parent.context).inflate(R.layout.adapter_trailer, parent, false)
    )

    override fun getItemCount() = videos.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val video = videos[position]
        holder.bind(video)

//        val textVideo = holder.itemView.findViewById<TextView>(R.id.textVideo)
//        textVideo.setOnClickListener {
//            listener.onPlay(video.key!!)
//            Log.d("This Tag", "ShowKey: ${video.key!!}")
//        }
        holder.itemView.setOnClickListener {
            listener.onPlay(video.key!!)
        }
    }

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        private var textVideo = view.findViewById<TextView>(R.id.textVideo)
        fun bind(videos: Trailer) {
            textVideo.text = videos.name
        }
    }

    public fun setData(newVideos: List<Trailer>) {
        videos.clear()
        videos.addAll(newVideos)
        notifyDataSetChanged()
        listener.onLoad(newVideos[0].key!!)
    }

    interface OnAdapterListener {
        fun onLoad (key: String)
        fun onPlay (key: String)
    }
}