package tanayot.barjord.musicplayerapplication.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import com.github.kittinunf.fuel.core.FuelError
import tanayot.barjord.musicplayerapplication.util.SingleLiveEvent

class DataResult(
    val result: LiveData<PagedList<Music>> = MutableLiveData(),
    val isInitLoading: LiveData<Boolean> = MutableLiveData(),
    val networkError: LiveData<FuelError> = MutableLiveData(),
    val musicDataList: MutableLiveData<ArrayList<Music>> = SingleLiveEvent(),
    val tempMusicList: MutableLiveData<ArrayList<Music>> = SingleLiveEvent(),
    val lastMusicDataList: LiveData<ArrayList<Music>> = SingleLiveEvent()
)