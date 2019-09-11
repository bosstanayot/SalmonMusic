package tanayot.barjord.musicplayerapplication.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import com.github.kittinunf.fuel.core.FuelError

class DataResult (
    val result: LiveData<PagedList<Music>> = MutableLiveData(),
    val isInitLoading: LiveData<Boolean> =  MutableLiveData(),
    val networkError: LiveData<FuelError> = MutableLiveData()
)