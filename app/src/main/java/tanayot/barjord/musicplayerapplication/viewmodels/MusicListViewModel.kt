package tanayot.barjord.musicplayerapplication.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.github.kittinunf.fuel.core.FuelError
import tanayot.barjord.musicplayerapplication.model.DataResult
import tanayot.barjord.musicplayerapplication.model.Music
import tanayot.barjord.musicplayerapplication.repositories.MusicListRepository

class MusicListViewModel (private val musicListRepository: MusicListRepository):ViewModel() {

    private val dataResult:  MutableLiveData<DataResult> = MutableLiveData()
    var musicList: LiveData<PagedList<Music>> = Transformations.switchMap(dataResult){it.result}
    var isInitLoading: LiveData<Boolean> = Transformations.switchMap(dataResult){it.isInitLoading}
    var networkError: LiveData<FuelError> = Transformations.switchMap(dataResult){it.networkError}
    val currentSong: MutableLiveData<Music> = MutableLiveData()
    init {
        getMusic()
    }

    private fun getMusic(){
        dataResult.value = musicListRepository.getPageList("music")
    }
}