package tanayot.barjord.musicplayerapplication.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import tanayot.barjord.musicplayerapplication.model.Song
import tanayot.barjord.musicplayerapplication.repositories.SongRepository

class MusicListViewModel (songRepository: SongRepository):ViewModel() {
    var mSongs: MutableLiveData<ArrayList<Song>> =  MutableLiveData()
    val currentSong: MutableLiveData<Song> = MutableLiveData()
    init {
        mSongs = songRepository.getSongs()
    }
    fun getSongs(): ArrayList<Song>{
        return mSongs.value!!
    }
}