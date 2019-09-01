package tanayot.barjord.musicplayerapplication.ui

import android.annotation.SuppressLint
import android.app.Dialog
import android.net.Uri
import android.os.Bundle
import android.support.v4.media.MediaDescriptionCompat
import android.support.v4.media.session.MediaSessionCompat
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.FrameLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.ext.mediasession.MediaSessionConnector
import com.google.android.exoplayer2.ext.mediasession.TimelineQueueNavigator
import com.google.android.exoplayer2.source.ConcatenatingMediaSource
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import org.koin.androidx.viewmodel.ext.android.viewModel
import tanayot.barjord.musicplayerapplication.databinding.PlayerFragmentBinding
import java.io.File
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.player_fragment.*
import tanayot.barjord.musicplayerapplication.R
import tanayot.barjord.musicplayerapplication.model.Song
import tanayot.barjord.musicplayerapplication.ui.list.MusicListFragment


class PlayerFragment : Fragment() {
    lateinit var player: SimpleExoPlayer
    private lateinit var binding: PlayerFragmentBinding
    private val viewModel: PlayerViewModel by viewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = PlayerFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
       /*binding.viewModel = viewModel.apply { arguments?.let {
            assignArgument(PlayerFragmentArgs.fromBundle(it))
        } }*/
    }

    override fun onStart() {
        super.onStart()
        if(viewModel.songs.value?.path != null){
            player = ExoPlayerFactory.newSimpleInstance(context, DefaultTrackSelector())
            binding.playerController.player = player
            val dataSourceFactory = DefaultDataSourceFactory(
                context,
                Util.getUserAgent(context, getString(R.string.app_name))
            )

            /*val mediaSession = MediaSessionCompat(context, "MUSIC")
            mediaSession.isActive = true

            val  mediaSessionConnector = MediaSessionConnector(mediaSession)
            mediaSessionConnector.setQueueNavigator(object: TimelineQueueNavigator(mediaSession) {
                override fun getMediaDescription(player: Player?, windowIndex: Int): MediaDescriptionCompat {
                    return Samples.getMediaDescription(context, SAMPLES[windowIndex])
                }

            })*/

            val mediaSource = ProgressiveMediaSource.Factory(dataSourceFactory)
                .createMediaSource(Uri.fromFile(File(viewModel.songs.value?.path!!)))

            player.prepare(mediaSource)
            player.playWhenReady = true
        }


    }

    override fun onDestroy() {
        player.release()
        super.onDestroy()
    }


}
