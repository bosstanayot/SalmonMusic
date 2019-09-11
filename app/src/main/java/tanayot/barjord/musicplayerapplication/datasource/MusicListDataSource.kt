package tanayot.barjord.musicplayerapplication.datasource

import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.FuelError
import tanayot.barjord.musicplayerapplication.ApiConstant
import tanayot.barjord.musicplayerapplication.model.Music
import tanayot.barjord.musicplayerapplication.model.PagingMusic

class MusicListDataSource(private val api:  Fuel,  private val dataPath: String): PageKeyedDataSource<String, Music>() {
    val isInitLoading = MutableLiveData<Boolean>()
    val networkError = MutableLiveData<FuelError>()
    override fun loadInitial(
        params: LoadInitialParams<String>,
        callback: LoadInitialCallback<String, Music>
    ) {
        isInitLoading.postValue(true)
        api.get(ApiConstant.BASE_URL + dataPath)
            .responseObject(PagingMusic.Deserializer()){_, _, result ->
                result.fold(success = { pagingMusic ->
                    isInitLoading.postValue(false)
                    callback.onResult(pagingMusic.data, null, pagingMusic.next)
                }, failure = {error ->
                    networkError.postValue(error)
                })
            }
    }

    override fun loadAfter(params: LoadParams<String>, callback: LoadCallback<String, Music>) {
        api.get(params.key)
            .responseObject(PagingMusic.Deserializer()){_, _, result ->
                result.fold(success = { pagingMusic ->
                    callback.onResult(pagingMusic.data, pagingMusic.next)
                }, failure = {error ->
                    networkError.postValue(error)
                })
            }
    }

    override fun loadBefore(params: LoadParams<String>, callback: LoadCallback<String, Music>) {}
}