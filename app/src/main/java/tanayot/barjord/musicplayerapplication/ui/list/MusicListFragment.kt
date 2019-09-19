package tanayot.barjord.musicplayerapplication.ui.list

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.media.session.MediaSessionCompat
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.BounceInterpolator
import androidx.lifecycle.Observer
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.ext.mediasession.MediaSessionConnector
import com.google.android.exoplayer2.source.ConcatenatingMediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.ui.PlayerNotificationManager
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import com.sothree.slidinguppanel.SlidingUpPanelLayout
import kotlinx.android.synthetic.main.music_list_fragment.view.*
import kotlinx.android.synthetic.main.player_fragment.view.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import tanayot.barjord.musicplayerapplication.MyPlayer
import tanayot.barjord.musicplayerapplication.PlayerListener
import tanayot.barjord.musicplayerapplication.ui.DescriptionAdapter
import tanayot.barjord.musicplayerapplication.service.MusicPlayerService
import tanayot.barjord.musicplayerapplication.R
import tanayot.barjord.musicplayerapplication.databinding.MusicListFragmentBinding
import tanayot.barjord.musicplayerapplication.model.Music
import tanayot.barjord.musicplayerapplication.viewmodels.MusicListViewModel
import kotlin.random.Random

class MusicListFragment : Fragment(), MusicListListener {
    var player: SimpleExoPlayer? = null
    private val myPlayer: MyPlayer by inject()
    private val viewModel: MusicListViewModel by viewModel()
    private lateinit var binding: MusicListFragmentBinding
    private val descriptionAdapter: DescriptionAdapter by inject { parametersOf(viewModel) }
    private val concatenatingMediaSource = ConcatenatingMediaSource()
    private var dataSourceFactory: DataSource.Factory? = null

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
        dataSourceFactory = DefaultDataSourceFactory(
                context,
        Util.getUserAgent(context, getString(R.string.app_name))
        )

        binding.swipeLayout.setOnRefreshListener{
            closePlayer()
            viewModel.getMusic()
        }
        viewModel.musicList.observe(this, Observer {
            binding.adapter?.submitList(it)
        })

       viewModel.isInitLoading.observe(this, Observer {
            binding.swipeLayout.isRefreshing = it
        })

        viewModel.musicDataList.observe(viewLifecycleOwner, Observer {})

        viewModel.lastMusicDataList.observe(this, Observer {
            addMusicToConcatenatingMediaSource(viewModel.lastMusicDataList.value!!)
        })

        viewModel.tempMusicList.observe(this, Observer {
            addMusicToConcatenatingMediaSource(it)
        })
    }

    override fun onMusicClicked(music: Music?, position: Int) {
        if(player == null){
            initPlayer()
            setPlayerPanel()
        }
        player?.seekTo(position, 0)
        player?.playWhenReady = true
    }

    private fun initPlayer(){
        player = myPlayer.initPlayer(concatenatingMediaSource, descriptionAdapter, viewModel.musicDataList, object :PlayerListener{
            override fun onTracksChanged(currentWindowIndex: Int) {
                binding.playerView.viewModel = viewModel.apply {
                    currentSong.value = musicDataList.value?.get(player?.currentWindowIndex!!)
                }
                if(concatenatingMediaSource.size-1 == player?.currentWindowIndex){
                    viewModel.getMoreMusic(concatenatingMediaSource.size)
                }
            }
        })
        binding.playerView.playerController.player = player
        binding.playerView.collapesPlayerView.player = player
    }

    private fun setPlayerPanel(){
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

    private fun addMusicToConcatenatingMediaSource(musicList: ArrayList<Music>){
        for (music in musicList) {
            val mediaSource = ProgressiveMediaSource.Factory(dataSourceFactory)
                .createMediaSource(Uri.parse(music.streamUrl))
            concatenatingMediaSource.addMediaSource(mediaSource)
        }
    }

    private fun closePlayer(){
        player?.stop()
        player?.release()
        viewModel.currentSong.value = Music()
        binding.slidingLayout.playerView.collapesLayout.imageViewCollapse.setImageResource(0)
        player = null
        concatenatingMediaSource.clear()
        binding.slidingLayout.panelState = SlidingUpPanelLayout.PanelState.HIDDEN
    }

}
