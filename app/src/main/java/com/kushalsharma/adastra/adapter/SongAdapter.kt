package com.kushalsharma.adastra.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kushalsharma.adastra.R
import com.kushalsharma.adastra.modals.SongItem


class SongAdapter(private val songList: ArrayList<SongItem>, val onItemClickListener: OnItemClickListener): RecyclerView.Adapter<SongAdapter.ViewHolder>() {

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        var imageSong = itemView.findViewById<ImageView>(R.id.image_song)
        var textSongName = itemView.findViewById<TextView>(R.id.text_songName)
        var textSongListening = itemView.findViewById<TextView>(R.id.text_songListen)
        var textSongLength = itemView.findViewById<TextView>(R.id.text_songLength)


        fun bindView(songItem: SongItem, clickListener: OnItemClickListener){
            imageSong.setImageResource(songItem.image)
            textSongName.text = songItem.songName
            textSongListening.text = songItem.songListening
            textSongLength.text = songItem.songLength

            itemView.setOnClickListener{
                clickListener.onItemClicked(songItem, layoutPosition)
            }

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.item_song_layout,
            parent,
            false
        )
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindView(songList[position], onItemClickListener)

    }

    override fun getItemCount(): Int {
        return songList.size
    }
}

interface OnItemClickListener{
    fun onItemClicked(songItem: SongItem, position: Int)
}