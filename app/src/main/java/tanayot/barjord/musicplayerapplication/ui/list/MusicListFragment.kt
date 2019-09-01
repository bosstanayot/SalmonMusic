package tanayot.barjord.musicplayerapplication.ui.list

import android.Manifest
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ConcatenatingMediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.source.TrackGroupArray
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.trackselection.TrackSelectionArray
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import com.sothree.slidinguppanel.SlidingUpPanelLayout
import kotlinx.android.synthetic.main.player_controler_view.view.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import tanayot.barjord.musicplayerapplication.R
import tanayot.barjord.musicplayerapplication.databinding.MusicListFragmentBinding
import tanayot.barjord.musicplayerapplication.model.Song
import tanayot.barjord.musicplayerapplication.viewmodels.MusicListViewModel
import java.io.File

class MusicListFragment : Fragment(), PermissionListener, MusicListListener {
    var player: SimpleExoPlayer? = null
    private val viewModel: MusicListViewModel by viewModel()
    private lateinit var binding: MusicListFragmentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding =  MusicListFragmentBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        runPermission()
    }

    private fun runPermission(){
        Dexter.withActivity(activity)
            .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
            .withListener(this).check()
    }

    override fun onPermissionGranted(response: PermissionGrantedResponse?) {
        binding.adapter = MusicListViewAdapter(viewModel.getSongs(), this)
        viewModel.mSongs.observe(this, Observer {
            binding.adapter?.setNewList(it)
        })
    }

    override fun onPermissionRationaleShouldBeShown(permission: PermissionRequest?, token: PermissionToken?) {
        token!!.continuePermissionRequest()
    }

    override fun onPermissionDenied(response: PermissionDeniedResponse?) {}

    override fun onMusicClicked(song: Song?, position: Int) {
      //  val action = PlayerFragmentDirections.actionPlayerFragmentToPlayerFragment2(song!!)
        if(player == null){
            initPlayer()
            binding.slidingLayout.panelState = SlidingUpPanelLayout.PanelState.COLLAPSED
            binding.slidingLayout.getChildAt(1).setOnClickListener(null)
            binding.playerView.dragMe.setOnClickListener {
                binding.slidingLayout.panelState = when (binding.slidingLayout.panelState) {
                    SlidingUpPanelLayout.PanelState.COLLAPSED -> SlidingUpPanelLayout.PanelState.EXPANDED
                    else -> SlidingUpPanelLayout.PanelState.COLLAPSED
                }
            }
        }
        player?.seekTo(position, 0)
        player?.playWhenReady = true
    }

    private fun initPlayer(){
        player = ExoPlayerFactory.newSimpleInstance(context, DefaultTrackSelector())
        binding.playerView.playerController.player = player
        //binding.playerController.player = player
        val dataSourceFactory = DefaultDataSourceFactory(
            context,
            Util.getUserAgent(context, getString(R.string.app_name))
        )

        val concatenatingMediaSource = ConcatenatingMediaSource()
        for (song in viewModel.getSongs()) {
            val mediaSource = ProgressiveMediaSource.Factory(dataSourceFactory)
                .createMediaSource(Uri.fromFile(File(song.path)))
            concatenatingMediaSource.addMediaSource(mediaSource)
        }

        player?.apply {
            prepare(concatenatingMediaSource)
            addListener(object :Player.EventListener{
                /*override fun onPositionDiscontinuity(reason: Int) {
                    binding.playerView.viewModel = viewModel.apply {
                        currentSong.value = getSongs()[player?.currentWindowIndex!!]
                    }
                }*/
                override fun onTracksChanged(trackGroups: TrackGroupArray?, trackSelections: TrackSelectionArray?) {
                    binding.playerView.viewModel = viewModel.apply {
                        currentSong.value = getSongs()[player?.currentWindowIndex!!]
                    }
                }
            })
        }
    }

}
