package tanayot.barjord.musicplayerapplication.ui.list

import android.Manifest
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import kotlinx.android.synthetic.main.player_fragment.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import tanayot.barjord.musicplayerapplication.BlankFragment
import tanayot.barjord.musicplayerapplication.databinding.MusicListFragmentBinding
import tanayot.barjord.musicplayerapplication.model.Song
import tanayot.barjord.musicplayerapplication.ui.PlayerFragment
import tanayot.barjord.musicplayerapplication.viewmodels.MusicListViewModel

class MusicListFragment : Fragment(), PermissionListener, MusicListListener {

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
        binding.adapter =
            MusicListViewAdapter(viewModel.getSongs(), this)
        viewModel.mSongs.observe(this, Observer {
            binding.adapter?.setNewList(it)
        })
    }

    override fun onPermissionRationaleShouldBeShown(permission: PermissionRequest?, token: PermissionToken?) {
        token!!.continuePermissionRequest()
    }

    override fun onPermissionDenied(response: PermissionDeniedResponse?) {}

    override fun onMusicClicked(song: Song?) {
        val action = MusicListFragmentDirections.actionMusicListFragmentToPlayerFragment(song!!)
       findNavController().navigate(action)
    }

}
