package tanayot.barjord.musicplayerapplication.model

import com.github.kittinunf.fuel.core.ResponseDeserializable
import com.google.gson.Gson

class PagingMusic(val next: String,
                  val data: ArrayList<Music>) {
    class Deserializer: ResponseDeserializable<PagingMusic> {
        override fun deserialize(content: String): PagingMusic? {
            return Gson().fromJson(content, PagingMusic::class.java)
        }
    }
}