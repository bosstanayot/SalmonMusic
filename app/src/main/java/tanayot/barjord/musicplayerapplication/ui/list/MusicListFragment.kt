package tanayot.barjord.musicplayerapplication.ui.list

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.media.MediaDescriptionCompat
import android.support.v4.media.session.MediaSessionCompat
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.paging.PagedList
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.ext.mediasession.MediaSessionConnector
import com.google.android.exoplayer2.ext.mediasession.TimelineQueueNavigator
import com.google.android.exoplayer2.source.ConcatenatingMediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.source.TrackGroupArray
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.trackselection.TrackSelectionArray
import com.google.android.exoplayer2.ui.PlayerNotificationManager
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import com.sothree.slidinguppanel.SlidingUpPanelLayout
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import tanayot.barjord.musicplayerapplication.ui.DescriptionAdapter
import tanayot.barjord.musicplayerapplication.service.MusicPlayerService
import tanayot.barjord.musicplayerapplication.R
import tanayot.barjord.musicplayerapplication.databinding.MusicListFragmentBinding
import tanayot.barjord.musicplayerapplication.model.Music
import tanayot.barjord.musicplayerapplication.viewmodels.MusicListViewModel

class MusicListFragment : Fragment(), MusicListListener {
    var player: SimpleExoPlayer? = null
    private val viewModel: MusicListViewModel by viewModel()
    private lateinit var binding: MusicListFragmentBinding
    private lateinit var playerNotificationManager:PlayerNotificationManager
    private val descriptionAdapter: DescriptionAdapter by inject { parametersOf(viewModel) }
    private lateinit var mediaSession:  MediaSessionCompat
    private lateinit var mediaSessionConnector: MediaSessionConnector

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding =  MusicListFragmentBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.adapter = MusicListViewAdapter(this)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val intent = Intent(context, MusicPlayerService::class.java)
        context?.startService(intent)
        viewModel.musicList.observe(this, Observer<PagedList<Music>> {
            binding.adapter?.submitList(it)
        })
    }

    override fun onMusicClicked(music: Music?, position: Int) {
        if(player == null){
            initPlayer()
            binding.slidingLayout.panelState = SlidingUpPanelLayout.PanelState.COLLAPSED
            binding.slidingLayout.addPanelSlideListener(object :SlidingUpPanelLayout.PanelSlideListener{
                override fun onPanelStateChanged(panel: View?, previousState: SlidingUpPanelLayout.PanelState?, newState: SlidingUpPanelLayout.PanelState?) {}

                override fun onPanelSlide(panel: View?, slideOffset: Float) {
                    binding.playerView.collapesLayout.alpha = 1-slideOffset
                    binding.playerView.expandLayout.alpha = slideOffset
                    binding.playerView.dragMe.alpha = slideOffset
                    binding.playerView.collapesLayout.visibility =when(slideOffset) {
                        1f -> View.GONE
                        else -> View.VISIBLE
                    }
                }
            })
        }
        player?.seekTo(position, 0)
        player?.playWhenReady = true
    }

    private fun initPlayer(){
        player = ExoPlayerFactory.newSimpleInstance(context, DefaultTrackSelector())
        binding.playerView.playerController.player = player
        binding.playerView.collapesPlayerView.player = player
        val dataSourceFactory = DefaultDataSourceFactory(
            context,
            Util.getUserAgent(context, getString(R.string.app_name))
        )

        val concatenatingMediaSource = ConcatenatingMediaSource()
        for (music in viewModel.musicList.value!!) {
            val mediaSource = ProgressiveMediaSource.Factory(dataSourceFactory)
                .createMediaSource(Uri.parse(music.streamUrl))
            concatenatingMediaSource.addMediaSource(mediaSource)
        }

        player?.apply {
            prepare(concatenatingMediaSource)
            addListener(object :Player.EventListener{
                override fun onTracksChanged(trackGroups: TrackGroupArray?, trackSelections: TrackSelectionArray?) {
                    binding.playerView.viewModel = viewModel.apply {
                        currentSong.value = musicList.value?.get(player?.currentWindowIndex!!)
                    }
                }
            })
        }

        playerNotificationManager = PlayerNotificationManager.createWithNotificationChannel(
            context,
            "playback_channel",
            R.string.playback_channel_name,
            R.string.playback_channel_description,
            1,
            descriptionAdapter)
        playerNotificationManager.setFastForwardIncrementMs(0)
        playerNotificationManager.setRewindIncrementMs(0)
        playerNotificationManager.setUseNavigationActionsInCompactView(true)
        playerNotificationManager.setPlayer(player)


        mediaSession = MediaSessionCompat(context, "audio_demo")
        mediaSession.isActive  =  true
        playerNotificationManager.setMediaSessionToken(mediaSession.sessionToken)

        mediaSessionConnector  = MediaSessionConnector(mediaSession)
        mediaSessionConnector.setQueueNavigator(object :TimelineQueueNavigator(mediaSession){
            override fun getMediaDescription(
                player: Player?,
                windowIndex: Int
            ): MediaDescriptionCompat {
                return Music.getMediaDescription(viewModel.musicList.value?.get(windowIndex))
            }

        })
        mediaSessionConnector.setPlayer(player, null)
    }

}
