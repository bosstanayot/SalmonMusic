package tanayot.barjord.musicplayerapplication.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.github.kittinunf.fuel.core.FuelError
import tanayot.barjord.musicplayerapplication.repositories.LoadMoreMusicListener
import tanayot.barjord.musicplayerapplication.repositories.LoadMoreMusicRepository
import tanayot.barjord.musicplayerapplication.model.DataResult
import tanayot.barjord.musicplayerapplication.model.Music
import tanayot.barjord.musicplayerapplication.repositories.MusicListRepository

class MusicListViewModel (private val musicListRepository: MusicListRepository,
                          private val loadMoreMusicRepository: LoadMoreMusicRepository
):ViewModel(),
    LoadMoreMusicListener {

    private val dataResult:  MutableLiveData<DataResult> = MutableLiveData()
    var musicList: LiveData<PagedList<Music>> = Transformations.switchMap(dataResult){it.result}
    var tempMusicList: LiveData<ArrayList<Music>> = Transformations.switchMap(dataResult){it.tempMusicList}
    var musicDataList: LiveData<ArrayList<Music>> = Transformations.switchMap(dataResult){it.musicDataList}
    var lastMusicDataList: LiveData<ArrayList<Music>> = Transformations.switchMap(dataResult){it.lastMusicDataList}
    var isInitLoading: LiveData<Boolean> = Transformations.switchMap(dataResult){it.isInitLoading}
    var networkError: LiveData<FuelError> = Transformations.switchMap(dataResult){it.networkError}
    val currentMusic: MutableLiveData<Music> = MutableLiveData()
    init {
        getMusic()
    }

    fun getMusic(){
        dataResult.postValue(musicListRepository.getPageList("music"))
    }

    fun getMoreMusic(offset: Int){
        loadMoreMusicRepository.getMusicListMore(offset, this)
    }

    override fun onGetMusicSuccess(musicList: ArrayList<Music>) {
        val newTempMusicList =  tempMusicList.value.apply { this?.addAll(musicList) }
        val newMusicDataList =  musicDataList.value.apply { this?.addAll(musicList) }
        dataResult.value?.tempMusicList?.value = newTempMusicList
        dataResult.value?.musicDataList?.value = newMusicDataList
    }

    override fun onGetMusicFailure(error: FuelError) {
        dataResult.value?.networkError?.value = error
    }

    fun setCurrentMusic(indexMusic: Int){
        currentMusic.value = musicDataList.value?.get(indexMusic)
    }


}
