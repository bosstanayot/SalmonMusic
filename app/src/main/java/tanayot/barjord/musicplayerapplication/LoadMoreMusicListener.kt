package tanayot.barjord.musicplayerapplication

import com.github.kittinunf.fuel.core.FuelError
import tanayot.barjord.musicplayerapplication.model.Music

interface LoadMoreMusicListener {
    fun onGetMusicSuccess(musicList: ArrayList<Music>)
    fun onGetMusicFailure(error: FuelError)
}