package tanayot.barjord.musicplayerapplication.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.github.kittinunf.fuel.Fuel
import tanayot.barjord.musicplayerapplication.datasource.MusicListDataSourceFactory
import tanayot.barjord.musicplayerapplication.PagingConfig
import tanayot.barjord.musicplayerapplication.model.DataResult
import tanayot.barjord.musicplayerapplication.model.Music
import java.util.concurrent.Executors

class MusicListRepository(private val api: Fuel) {
    var result: LiveData<PagedList<Music>> = MutableLiveData()
    fun getPageList(dataPath: String): DataResult{
        val musicListDataSourceFactory =
            MusicListDataSourceFactory(
                api,
                dataPath
            )
        result = LivePagedListBuilder(musicListDataSourceFactory, PagingConfig.config)
            .setFetchExecutor(Executors.newFixedThreadPool(5))
            .build()

        return DataResult(
            result,
            musicListDataSourceFactory.isInitLoading,
            musicListDataSourceFactory.networkError
        )
    }
}