package tanayot.barjord.musicplayerapplication.datasource

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.FuelError
import tanayot.barjord.musicplayerapplication.model.Music

class MusicListDataSourceFactory(api: Fuel, dataPath: String): DataSource.Factory<String, Music>(){
    private val musicSource =
        MusicListDataSource(api, dataPath)
    val isInitLoading: LiveData<Boolean> = musicSource.isInitLoading
    val networkError: LiveData<FuelError> = musicSource.networkError
    override fun create(): DataSource<String, Music> =  musicSource
}