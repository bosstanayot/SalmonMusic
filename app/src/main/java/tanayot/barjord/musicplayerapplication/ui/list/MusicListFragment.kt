package tanayot.barjord.musicplayerapplication.ui.list

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ConcatenatingMediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import com.sothree.slidinguppanel.SlidingUpPanelLayout
import kotlinx.android.synthetic.main.music_list_fragment.view.*
import kotlinx.android.synthetic.main.player_fragment.view.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import tanayot.barjord.musicplayerapplication.ui.player.MyPlayer
import tanayot.barjord.musicplayerapplication.ui.adapter.DescriptionAdapter
import tanayot.barjord.musicplayerapplication.service.MusicPlayerService
import tanayot.barjord.musicplayerapplication.R
import tanayot.barjord.musicplayerapplication.databinding.MusicListFragmentBinding
import tanayot.barjord.musicplayerapplication.model.Music
import tanayot.barjord.musicplayerapplication.ui.player.PlayerListener
import tanayot.barjord.musicplayerapplication.ui.viewmodels.MusicListViewModel

class MusicListFragment : Fragment(), MusicListListener {
    private var player: SimpleExoPlayer? = null
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

        setService()
        dataSourceFactory = DefaultDataSourceFactory(context, Util.getUserAgent(context, getString(R.string.app_name)))

        binding.swipeLayout.setOnRefreshListener{
            closePlayer()
            viewModel.getMusic()
        }

        attachViewModel()
    }

    private fun attachViewModel(){
        viewModel.musicList.observe(this, Observer {
            binding.adapter?.submitList(it)
        })

        viewModel.isInitLoading.observe(this, Observer {
            binding.swipeLayout.isRefreshing = it
        })

        viewModel.musicDataList.observe(this, Observer {})

        viewModel.lastMusicDataList.observe(this, Observer {
            addMusicToConcatenatingMediaSource(it)
        })

        viewModel.tempMusicList.observe(this, Observer {
            addMusicToConcatenatingMediaSource(it)
        })

        viewModel.networkError.observe(this, Observer {
            Toast.makeText(context, it.response.toString(), Toast.LENGTH_SHORT).show()
        })
    }

    override fun onMusicClicked(position: Int) {
        if(player == null){
            initPlayer()
            setPlayerPanel()
        }
        selectMusicAndPlay(position)
    }

    private fun initPlayer(){
        player = myPlayer.initPlayer(concatenatingMediaSource, descriptionAdapter, viewModel.musicDataList, object :
            PlayerListener {
            override fun onTracksChanged(currentWindowIndex: Int) {
                binding.playerView.viewModel = viewModel.apply {
                    setCurrentMusic(player?.currentWindowIndex!!)
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
        viewModel.currentMusic.value = Music()
        binding.slidingLayout.playerView.collapesLayout.imageViewCollapse.setImageResource(0)
        player = null
        concatenatingMediaSource.clear()
        binding.slidingLayout.panelState = SlidingUpPanelLayout.PanelState.HIDDEN
    }

    private fun setService(){
        val intent = Intent(context, MusicPlayerService::class.java)
        context?.startService(intent)
    }

    private fun selectMusicAndPlay(position: Int){
        player?.seekTo(position, 0)
        player?.playWhenReady = true
    }

}
