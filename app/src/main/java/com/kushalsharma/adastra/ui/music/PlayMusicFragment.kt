package com.kushalsharma.adastra.ui.music

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.kushalsharma.adastra.R
import com.kushalsharma.adastra.modals.SongItem

import de.hdodenhof.circleimageview.CircleImageView
import org.w3c.dom.Text
import java.util.concurrent.TimeUnit


class PlayMusicFragment : Fragment() {

    lateinit var currentPosition: TextView
    lateinit var songLength: TextView
    lateinit var songSeekBar: SeekBar
    lateinit var playButton: ImageView
    lateinit var pauseButton: ImageView
    lateinit var nextButton: ImageView
    lateinit var previousButton: ImageView
    lateinit var textSongName: TextView
    lateinit var imageViewSong: CircleImageView
    lateinit var imageViewCancel: ImageView

    lateinit var mediaPlayer: MediaPlayer
    val handler: Handler = Handler()
    lateinit var runnable: Runnable
    lateinit var runnable2: Runnable
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
        val view = inflater.inflate(R.layout.fragment_play_music, container, false)

        currentPosition = view.findViewById(R.id.current_position)
        songLength = view.findViewById(R.id.song_length)
        songSeekBar = view.findViewById(R.id.seek_bar)
        playButton = view.findViewById(R.id.bt_play)
        pauseButton = view.findViewById(R.id.pt_pause)
        nextButton = view.findViewById(R.id.bt_next)
        previousButton = view.findViewById(R.id.bt_prev)
        textSongName = view.findViewById(R.id.text_songName)
        imageViewSong = view.findViewById(R.id.ImageSong)
        imageViewCancel = view.findViewById(R.id.imageViewCancel)



        val imageId = requireArguments().getInt("image")
        val songId = requireArguments().getInt("song")
        val songName = requireArguments().getString("songName")
        var position = requireArguments().getInt("position")
        val songList: ArrayList<SongItem> = requireArguments().getSerializable("songList") as ArrayList<SongItem>



        Log.d("TAG", "onCreateView: Position = $position  songListSize = ${songList.size}")


        imageViewSong.setImageResource(imageId)
        textSongName.text = songName.toString()
        mediaPlayer = MediaPlayer.create(requireContext(), songId)


        imageViewCancel.setOnClickListener {
            mediaPlayer.pause()
            mediaPlayer.seekTo(0)
            handler.removeCallbacks(runnable)
            val musicFragment = MusicFragment()
            val transaction = fragmentManager?.beginTransaction()
            transaction?.replace(R.id.fragment_container_view, musicFragment)
            transaction?.commit()
        }


        runnable = Runnable {
            songSeekBar.progress = mediaPlayer.currentPosition

            handler.postDelayed(runnable, 500)
        }

        val duration = mediaPlayer.duration

        val sDuration = convertFormat(duration)

        songLength.text = sDuration

        playButton.setOnClickListener {

            playButton.visibility = View.GONE
            pauseButton.visibility = View.VISIBLE
            mediaPlayer.start()
            songSeekBar.max = mediaPlayer.duration
            handler.postDelayed(runnable, 0)
        }

        pauseButton.setOnClickListener {

            pauseButton.visibility = View.GONE
            playButton.visibility = View.VISIBLE


            mediaPlayer.pause()

            handler.removeCallbacks(runnable)
        }

        songSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{

            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if(fromUser){
                    mediaPlayer.seekTo(progress)
                }
                currentPosition.text = convertFormat(mediaPlayer.currentPosition)

            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                TODO("Not yet implemented")
            }


            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                TODO("Not yet implemented")
            }
        })

        mediaPlayer.setOnCompletionListener {
            MediaPlayer.OnCompletionListener {

                pauseButton.visibility = View.GONE
                playButton.visibility = View.VISIBLE

                mediaPlayer.seekTo(0)
            }
        }
        return view
    }

    private fun convertFormat(duration: Int): String {
        return String.format(
            "%02d:%02d", TimeUnit.MILLISECONDS.toMinutes(duration.toLong()),
            TimeUnit.MILLISECONDS.toSeconds(duration.toLong()) - TimeUnit.MINUTES.toSeconds(
                TimeUnit.MILLISECONDS.toMinutes(
                    duration.toLong()
                )
            )
        )
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            PlayMusicFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }

    override fun onPause() {
        super.onPause()
        mediaPlayer.pause()
        handler.removeCallbacks(runnable)
        mediaPlayer.seekTo(0)
    }

}