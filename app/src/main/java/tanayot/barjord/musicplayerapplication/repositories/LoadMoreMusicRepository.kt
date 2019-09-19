package tanayot.barjord.musicplayerapplication.repositories

import com.github.kittinunf.fuel.Fuel
import tanayot.barjord.musicplayerapplication.ApiConstant
import tanayot.barjord.musicplayerapplication.model.PagingMusic

class LoadMoreMusicRepository(private val api: Fuel) {
    fun getMusicListMore(offset: Int, listener: LoadMoreMusicListener){
        api.get(ApiConstant.BASE_URL +"music?offset=${offset}&limit=10")
            .responseObject(PagingMusic.Deserializer()){ _, _, result ->
                result.fold(success = { pagingMusic ->
                    listener.onGetMusicSuccess(pagingMusic.data)
                }, failure = {error ->
                    listener.onGetMusicFailure(error)
                })
            }
    }
}