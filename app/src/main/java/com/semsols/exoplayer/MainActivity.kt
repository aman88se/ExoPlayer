package com.semsols.exoplayer

import android.content.res.Configuration
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.PlaybackException
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {


//    private lateinit var constrainLayout: ConstraintLayout
//    private lateinit var exoPlayer: PlayerView
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
        simpleExoPlayer.addListener(playerListener)

        exoPlayer.player = simpleExoPlayer

        //MediaSource
        createMediaSource()

        simpleExoPlayer.setMediaSource(mediaSource)
        simpleExoPlayer.prepare()
    }

    private fun createMediaSource(){

        urlType = URLType.MP4
        urlType.url = "https://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4 "

        simpleExoPlayer.seekTo(0)

        when(urlType){

            URLType.MP4 -> {

                val dataSourceFactory: DataSource.Factory = DefaultDataSourceFactory(
                    this,
                    Util.getUserAgent(this,applicationInfo.name)
                )

                mediaSource = ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(
                    MediaItem.fromUri(Uri.parse(urlType.url)))


            }

            URLType.HLS ->{

                val dataSourceFactory: DataSource.Factory = DefaultDataSourceFactory(
                    this,
                    Util.getUserAgent(this,applicationInfo.name)
                )

                mediaSource = HlsMediaSource.Factory(dataSourceFactory).createMediaSource(
                    MediaItem.fromUri(Uri.parse(urlType.url))
                )

            }

        }

    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)

        val constraintSet = ConstraintSet()
        constraintSet.connect(exoPlayer.id,ConstraintSet.TOP,ConstraintSet.PARENT_ID,ConstraintSet.TOP,0)
        constraintSet.connect(exoPlayer.id,ConstraintSet.BOTTOM,ConstraintSet.PARENT_ID,ConstraintSet.BOTTOM,0)
        constraintSet.connect(exoPlayer.id,ConstraintSet.START,ConstraintSet.PARENT_ID,ConstraintSet.START,0)
        constraintSet.connect(exoPlayer.id,ConstraintSet.END,ConstraintSet.PARENT_ID,ConstraintSet.END,0)

        constraintSet.applyTo(constraintLayout)

        if(newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE){

            hideSystemUI()
        }else{

            showSystemUI()

             val layoutParams = exoPlayer.layoutParams as ConstraintLayout.LayoutParams
            layoutParams.dimensionRatio = "16:9"
        }

        window.decorView.requestLayout()
    }

    //For landscape mode
    private fun hideSystemUI(){

        actionBar?.hide()

        window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_IMMERSIVE
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN

                )


    }

    private fun showSystemUI(){

        actionBar?.show()

        window.decorView.systemUiVisibility = (

                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN

                )
    }

    override fun onResume() {
        super.onResume()

        simpleExoPlayer.playWhenReady = true
        simpleExoPlayer.play()
    }

    override fun onPause() {
        super.onPause()

        simpleExoPlayer.pause()
        simpleExoPlayer.playWhenReady =  false
    }


    override fun onDestroy() {
        super.onDestroy()

        simpleExoPlayer.removeListener(playerListener)
        simpleExoPlayer.stop()
        simpleExoPlayer.clearMediaItems()

        window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }

    private var playerListener = object : Player.Listener{

        override fun onRenderedFirstFrame() {
            super.onRenderedFirstFrame()

            if(urlType == URLType.HLS){

                exoPlayer.useController = false
            }

            if(urlType == URLType.MP4){
                exoPlayer.useController = true
            }

        }

        override fun onPlayerError(error: PlaybackException) {
            super.onPlayerError(error)

            Toast.makeText(this@MainActivity,"${error.message}",Toast.LENGTH_LONG).show()
        }


    }
}

enum class URLType(var url:String) {

    MP4(""),HLS("")

}
