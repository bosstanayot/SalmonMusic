package tanayot.barjord.musicplayerapplication.datasource

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.FuelError
import tanayot.barjord.musicplayerapplication.ApiConstant
import tanayot.barjord.musicplayerapplication.util.SingleLiveEvent
import tanayot.barjord.musicplayerapplication.model.Music
import tanayot.barjord.musicplayerapplication.model.PagingMusic

class MusicListDataSource(private val api:  Fuel,  private val dataPath: String): PageKeyedDataSource<String, Music>() {
    val isInitLoading = MutableLiveData<Boolean>()
    val networkError = MutableLiveData<FuelError>()
    val tempMusicList =
        SingleLiveEvent<ArrayList<Music>>()
    val musicDataList =
        SingleLiveEvent<ArrayList<Music>>()
    val lastMusicDataList =
        SingleLiveEvent<ArrayList<Music>>()
    private var countMusic = 0
    override fun loadInitial(
        params: LoadInitialParams<String>,
        callback: LoadInitialCallback<String, Music>
    ) {
        isInitLoading.postValue(true)
        api.get(ApiConstant.BASE_URL + dataPath)
            .responseObject(PagingMusic.Deserializer()){_, _, result ->
                result.fold(success = { pagingMusic ->
                    isInitLoading.postValue(false)
                    countMusic += 10
                    tempMusicList.postValue(arrayListOf())
                    lastMusicDataList.postValue(pagingMusic.data)

                    musicDataList.postValue(pagingMusic.data)
                    Log.w("sizedata",  "musicDataList${lastMusicDataList.value}")

                    callback.onResult(pagingMusic.data, null, pagingMusic.next)
                }, failure = {error ->
                    networkError.postValue(error)
                })
            }
    }

    override fun loadAfter(params: LoadParams<String>, callback: LoadCallback<String, Music>) {
        countMusic += 10
        Log.w("data",  "before if ${tempMusicList.value?.size}")
        if(tempMusicList.value?.size == 0) {
           // Log.w("sizedata",  "${params.key}")
            api.get(params.key)
                .responseObject(PagingMusic.Deserializer()) { _, _, result ->
                    result.fold(success = { pagingMusic ->
                        tempMusicList.value!!.clear()
                        val newList = musicDataList.value
                        newList?.addAll(pagingMusic.data)
                        musicDataList.postValue(newList)
                        lastMusicDataList.postValue(pagingMusic.data)
                        Log.w("data",  "if ${pagingMusic.data.toString()}")
                        callback.onResult(pagingMusic.data, pagingMusic.next)
                    }, failure = { error ->
                        networkError.postValue(error)
                    })
                }
        }else{
            countMusic = musicDataList.value!!.size
            val tempLastest = arrayListOf<Music>()
            tempLastest.addAll(tempMusicList.value!!)
            Log.w("data",  "else ${tempMusicList.value?.size}")
            tempMusicList.value?.clear()
            Log.w("data",  "temp ${tempLastest.size}")

            callback.onResult(tempLastest,ApiConstant.BASE_URL+"music?offset=${countMusic}}&limit=10")
        }
    }

    override fun loadBefore(params: LoadParams<String>, callback: LoadCallback<String, Music>) {}
}