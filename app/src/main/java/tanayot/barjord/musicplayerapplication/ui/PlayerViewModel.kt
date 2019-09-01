package tanayot.barjord.musicplayerapplication.ui

import android.content.Context
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import tanayot.barjord.musicplayerapplication.model.Song

class PlayerViewModel(private val context: Context) : ViewModel() {
    val songs: MutableLiveData<Song> = MutableLiveData()
    var artWork: Drawable? = null

   /* fun assignArgument(args: PlayerFragmentArgs) {
       songs.value = args.songData
        artWork = BitmapDrawable(context.resources, args.songData.imgSong)
    }*/

}
