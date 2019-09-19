package tanayot.barjord.musicplayerapplication.datasource

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.FuelError
import tanayot.barjord.musicplayerapplication.model.Music

class MusicListDataSourceFactory(api: Fuel, dataPath: String): DataSource.Factory<String, Music>(){
    private val musicSource =
        MusicListDataSource(api, dataPath)

    val musicDataList: MutableLiveData<ArrayList<Music>> = musicSource.musicDataList
    val tempMusicList: MutableLiveData<ArrayList<Music>> = musicSource.tempMusicList
    val isInitLoading: LiveData<Boolean> = musicSource.isInitLoading
    val networkError: LiveData<FuelError> = musicSource.networkError
    val lastMusicDataList: LiveData<ArrayList<Music>> = musicSource.lastMusicDataList
    override fun create(): DataSource<String, Music> = musicSource


}