package tanayot.barjord.musicplayerapplication.ui

import android.annotation.SuppressLint
import android.app.Dialog
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.FrameLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.SimpleExoPlayer
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


class PlayerFragment : BottomSheetDialogFragment() {
    lateinit var player: SimpleExoPlayer
    private lateinit var binding: PlayerFragmentBinding
    private val viewModel: PlayerViewModel by viewModel()
    private lateinit var sheetBehavior: BottomSheetBehavior<View>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = PlayerFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
       binding.viewModel = viewModel.apply { arguments?.let {
            assignArgument(PlayerFragmentArgs.fromBundle(it))
        } }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.viewTreeObserver.addOnGlobalLayoutListener {
            val bottomSheet = dialog?.findViewById(com.google.android.material.R.id.design_bottom_sheet) as FrameLayout
            val behavior = BottomSheetBehavior.from(bottomSheet)
           behavior.peekHeight = binding.dragMe.height
           binding.dragMe.requestLayout()
        }
    }

    @SuppressLint("RestrictedApi")
    override fun setupDialog(dialog: Dialog, style: Int) {
        super.setupDialog(dialog, R.style.CustomBottomSheetDialogTheme)
        dialog?.window?.setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE)
        dialog?.window?.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        dialog?.setCanceledOnTouchOutside(false)
        //dialog?.setCancelable(false)
    }

    override fun onStart() {
        super.onStart()
        player = ExoPlayerFactory.newSimpleInstance(context, DefaultTrackSelector())
        binding.playerController.player = player
        val dataSourceFactory = DefaultDataSourceFactory(
            context,
            Util.getUserAgent(context, getString(R.string.app_name))
        )

        val mediaSource = ProgressiveMediaSource.Factory(dataSourceFactory)
            .createMediaSource(Uri.fromFile(File(viewModel.songs.value?.path!!)))

        player.prepare(mediaSource)
        player.playWhenReady = true

    }

    override fun onDestroy() {
        player.release()
        super.onDestroy()
    }


}
