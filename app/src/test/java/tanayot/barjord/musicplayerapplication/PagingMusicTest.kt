package tanayot.barjord.musicplayerapplication

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import tanayot.barjord.musicplayerapplication.model.Music
import tanayot.barjord.musicplayerapplication.model.PagingMusic
import java.lang.reflect.Type

class PagingMusicTest {
    private var json: String = ""
    private lateinit var musicListData: PagingMusic

    @Before
    @Throws(Exception::class)
    fun setUp() {
        json = TestHelper().getStringFromFile("musicList.json")
        val listType: Type = object : TypeToken<PagingMusic>() {}.type
        musicListData = Gson().fromJson<PagingMusic>(json, listType)
    }

    @Test
    @Throws(Exception::class)
    fun parseJson_getNext(){
        Assert.assertEquals("", musicListData.next, "https://salmon-music.appspot.com/music?offset=10&limit=10")
    }

    @Test
    @Throws(Exception::class)
    fun parseJsonIndex0_getArtist() {
        Assert.assertEquals("", musicListData.data[0].artist, "LiSA")
    }

    @Test
    @Throws(Exception::class)
    fun parseJsonIndex0_getTitle() {
        Assert.assertEquals("", musicListData.data[0].title, "Adamas")
    }

    @Test
    @Throws(Exception::class)
    fun parseJsonIndex0_getPicUrl() {
        Assert.assertEquals("", musicListData.data[0].picUrl, "https://storage.googleapis.com/salmon-music.appspot.com/music/Adamas-LiSA/Adamas_picart")
    }

    @Test
    @Throws(Exception::class)
    fun parseJsonIndex0_getStreamUrl() {
        Assert.assertEquals("", musicListData.data[0].streamUrl, "https://storage.googleapis.com/salmon-music.appspot.com/music/Adamas-LiSA/Adamas")
    }

    @Test
    @Throws(Exception::class)
    fun parseJsonIndex0_getId() {
        Assert.assertEquals("", musicListData.data[0].musicId, "muluAei0IzY0mGTN3sHQ")
    }

}