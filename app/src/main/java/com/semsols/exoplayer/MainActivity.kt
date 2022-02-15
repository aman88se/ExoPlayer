package com.semsols.exoplayer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.ui.PlayerView

class MainActivity : AppCompatActivity() {


    private lateinit var constrainLayout: ConstraintLayout
    private lateinit var exoPlayer: PlayerView
    private lateinit var simpleExoPlayer: SimpleExoPlayer
    private lateinit var mediaSource: MediaSource
    private lateinit var urlType: URLType

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        initPlayer()

    }

    private fun initPlayer(){

        simpleExoPlayer = SimpleExoPlayer.Builder(this).build()

        exoPlayer.player = simpleExoPlayer

        //MediaSource
        createMediaSource()

        simpleExoPlayer.setMediaSource(mediaSource)
        simpleExoPlayer.prepare()
    }

    private fun createMediaSource(){

        urlType = URLType.MP4
        urlType.url = "https://www.youtube.com/shorts/z_nmqUEDDHA"

    }

    override fun onDestroy() {
        super.onDestroy()

        window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }
}

enum class URLType(var url:String) {

    MP4(""),HLS("")

}
