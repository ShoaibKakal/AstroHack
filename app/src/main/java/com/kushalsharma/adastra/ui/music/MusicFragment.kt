package com.kushalsharma.adastra.ui.music

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.kushalsharma.adastra.R
import com.kushalsharma.adastra.adapter.OnItemClickListener
import com.kushalsharma.adastra.adapter.SongAdapter
import com.kushalsharma.adastra.modals.SongItem


class MusicFragment : Fragment(), OnItemClickListener {

    lateinit var imageRelaxSounds: ImageView
    lateinit var songList: ArrayList<SongItem>
    lateinit var songAdapter: SongAdapter
    lateinit var songRecyclerView: RecyclerView
    lateinit var btnPlayNow: MaterialButton



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_music, container, false)
        btnPlayNow = view.findViewById(R.id.btn_PlayNow)

        btnPlayNow.setOnClickListener {
            onItemClicked(songList[0], 0)
        }



        songList = ArrayList()
        songList.add(SongItem(R.drawable.bird,  R.raw.birds_in_jungle, "Birds In Jungle", "85 Listening", "20 Min"))
        songList.add(SongItem(R.drawable.desert, R.raw.evil_strom,  "Desert Strom", "32 Listening", "15 Min"))
        songList.add(SongItem(R.drawable.forest_night, R.raw.night_forest_with_insects, "Forest Night", "68 Listening", "39 Min"))
        songList.add(SongItem(R.drawable.forest_rain, R.raw.jungle_rain_and_birds,  "Forest Rain", "5 Listening", "62 Min"))
        songList.add(SongItem(R.drawable.mountain_wind, R.raw.mountain_wind ,"Mountain Wind", "85 Listening", "20 Min"))
        songList.add(SongItem(R.drawable.farm_animal, R.raw.farm_mini, "Farm Morning", "32 Listening", "15 Min"))

        songAdapter = SongAdapter(songList, this)
        songAdapter.notifyDataSetChanged()

        imageRelaxSounds = view.findViewById(R.id.image_relax_sound)
        imageRelaxSounds.clipToOutline = true

        songRecyclerView = view.findViewById(R.id.song_recyclerview)
        songRecyclerView.adapter = songAdapter
        return view
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MusicFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }

    override fun onItemClicked(songItem: SongItem, position: Int) {
        val playMusicFragment = PlayMusicFragment()
        val transaction = fragmentManager?.beginTransaction()
        val bundle = bundleOf("image" to songItem.image, "song" to songItem.song, "songName" to songItem.songName, "position" to position, "songList" to songList)
        transaction?.replace(R.id.fragment_container_view, playMusicFragment)
        playMusicFragment.arguments = bundle
        transaction?.commit()

    }
}