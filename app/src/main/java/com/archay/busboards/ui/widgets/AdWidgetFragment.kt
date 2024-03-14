package com.archay.busboards.ui.widgets

import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.common.util.Util
import androidx.media3.datasource.AssetDataSource
import androidx.media3.datasource.DataSpec
import androidx.media3.datasource.DefaultDataSourceFactory
import androidx.media3.datasource.RawResourceDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.MediaSource
import androidx.media3.exoplayer.source.ProgressiveMediaSource
import androidx.media3.extractor.DefaultExtractorsFactory
import com.archay.busboards.R
import com.archay.busboards.app.BusApplication
import com.archay.busboards.base.BaseFragment
import com.archay.busboards.data.dto.AdsDto
import com.archay.busboards.databinding.WidgetAdBinding
import com.archay.busboards.local.BusPreferences
import com.archay.busboards.ui.BusViewModel
import com.archay.busboards.util.exoplayer.ExoplayerUtil
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject


@UnstableApi
@AndroidEntryPoint
class AdWidgetFragment() : BaseFragment<WidgetAdBinding>(WidgetAdBinding::inflate) {

    @Inject
    lateinit var preferences: BusPreferences

    private val viewModel by viewModels<BusViewModel>()
    private lateinit var exoPlayer: ExoPlayer
    private var isPlayerPlaying = true
    private var currentWindow = 0
    private var playbackPositionMs: Long = 0
    private var lastVideoPosition: Int = 0
    private var job: Job? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.window?.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

    }

    private fun initPlayer(context: Context) {
        exoPlayer = ExoPlayer.Builder(context)
            /*.setMediaSourceFactory(
                DefaultMediaSourceFactory(
                    ExoplayerUtil.cacheDataSourceFactory(
                        context
                    )
                )
            )*/.build()
            .also { exo ->

                exo.playWhenReady = isPlayerPlaying
                exo.seekTo(currentWindow, playbackPositionMs)
                exo.repeatMode = Player.REPEAT_MODE_ALL
            }

        exoPlayer.addListener(object : Player.Listener {
            override fun onPlayerError(error: PlaybackException) {
                super.onPlayerError(error)

                Log.i("TTTT", "error.errorCode ${error.errorCode}")
                Log.i("TTTT", "error.message ${error.message}")
                Log.i("TTTT", "error.cause?.message ${error.cause?.message}")
                Log.i("TTTT", "error.localizedMessage ${error.localizedMessage}")
                Log.i("TTTT", "error.errorCodeName ${error.errorCodeName}")
            }


            override fun onPlaybackStateChanged(playbackState: Int) {
                super.onPlaybackStateChanged(playbackState)

                when (playbackState) {
                    Player.STATE_IDLE -> {
                        Log.i("TTTT", "PlayerState: STATE_IDLE")
                    }

                    Player.STATE_READY -> {
                        updateTimer(exoPlayer.duration)
                        Log.i("TTTT", "PlayerState: STATE_READY")
                    }

                    Player.STATE_ENDED -> {
                        Log.i("TTTT", "PlayerState: STATE_ENDED")
                    }

                    Player.STATE_BUFFERING -> {
                        Log.i("TTTT", "PlayerState: STATE_BUFFERING")
                    }
                }
            }

        })
        binding.player.hideController()
        binding.player.player = exoPlayer

        increaseSlide()
    }

    private fun increaseSlide() {
        lastVideoPosition++
        preferences.lastVideoPosition = lastVideoPosition

        if (lastVideoPosition >= videoList.size) {
            lastVideoPosition = 0
            preferences.lastVideoPosition = lastVideoPosition
        }

//        if (videoList.isEmpty())
//            return

//        updateTimer(10000)

        exoPlayer.seekTo(0)
        exoPlayer.playWhenReady = false


        val dataSpec =
            DataSpec(Uri.parse("asset:///ad.mp4"))

        val assetDataSource = AssetDataSource(requireContext())
        try {
            assetDataSource.open(dataSpec)
        } catch (e: AssetDataSource.AssetDataSourceException) {
            e.printStackTrace()
        }

        val videoUri = Uri.parse("https://smartcastuz.storage.yandexcloud.net/Asgardia/VIDEO/ismoilxusanov_-466750d0-c78e-4d46-95e0-1fedf0a4381d.mp4")

        val mediaItem = MediaItem.fromUri(videoUri)
        val mediaSource =
            ProgressiveMediaSource.Factory(
                ExoplayerUtil.cacheDataSourceFactory(
                    requireContext(),
                    BusApplication.cache
                )
            )
                .createMediaSource(mediaItem)

        exoPlayer.setMediaSource(mediaSource, true)
        exoPlayer.prepare()
        exoPlayer.playWhenReady = true
        binding.player.setKeepContentOnPlayerReset(true) // TODO("I have to learn this method)
    }

    private fun updateTimer(durationInMill: Long) {
        job?.cancel()
        job = lifecycleScope.launch {
            delay(durationInMill)

            increaseSlide()
        }
    }


    private fun releasePlayer() {
        exoPlayer.let { exoPlayer ->
            job?.cancel()
            job = null
            isPlayerPlaying = exoPlayer.playWhenReady
            playbackPositionMs = exoPlayer.currentPosition
            currentWindow = exoPlayer.currentMediaItemIndex
            exoPlayer.release()
        }
    }

    override fun onStart() {
        super.onStart()
        if (Util.SDK_INT > 23) {
            initPlayer(requireContext())
        }
    }

    override fun onResume() {
        super.onResume()
        if (Build.VERSION.SDK_INT <= 23) {
            initPlayer(requireContext())
            binding.player.onResume()
        }
    }

    override fun onPause() {
        super.onPause()
        if (Build.VERSION.SDK_INT <= 23) {
            binding.player.onPause()
            releasePlayer()
        }
    }

    override fun onStop() {
//        playerViewModel.stopFlow()
        super.onStop()
        if (Build.VERSION.SDK_INT > 23) {
            binding.player.onPause()
            releasePlayer()
        }
    }

    companion object {
        private val videoList = ArrayList<AdsDto>()
        fun getInstance(list: List<AdsDto>?) {
            if (list != null) {
                videoList.clear()
                videoList.addAll(list)
            }
        }

    }

}