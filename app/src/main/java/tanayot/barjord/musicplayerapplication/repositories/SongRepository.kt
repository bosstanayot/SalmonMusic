package tanayot.barjord.musicplayerapplication.repositories

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.media.MediaMetadataRetriever
import android.provider.MediaStore
import androidx.lifecycle.MutableLiveData
import tanayot.barjord.musicplayerapplication.model.Song

class SongRepository(private val context: Context) {
    private val music: MutableLiveData<ArrayList<Song>> = MutableLiveData()

    fun getSongs(): MutableLiveData<ArrayList<Song>>{
        music.value = findSong()
        return music
    }

    private fun findSong(): ArrayList<Song> {
        val selection = MediaStore.Audio.Media.IS_MUSIC + " != 0"
        val projection = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.DATA,
            MediaStore.Audio.Media.DISPLAY_NAME,
            MediaStore.Audio.Media.DURATION
        )

        val cursor = context.contentResolver.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            projection,
            selection,
            null, null
        )

        val songs = ArrayList<Song>()
        while (cursor!!.moveToNext()) {
            val mmr = MediaMetadataRetriever()
            mmr.setDataSource(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA)))
            val data = mmr.embeddedPicture
            val bitmap = BitmapFactory.decodeByteArray(data, 0, data.size)
            val drawable: Drawable = BitmapDrawable(context.resources, bitmap)

            songs.add(
                Song(
                    cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media._ID)),
                    cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST)),
                    cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE)),
                    cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA)),
                    cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME)),
                    cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION)),
                    bitmap
                )
            )
        }
        cursor.close()
        return songs
    }

}