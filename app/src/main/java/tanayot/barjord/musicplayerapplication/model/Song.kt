package tanayot.barjord.musicplayerapplication.model

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.support.v4.media.MediaDescriptionCompat
import android.support.v4.media.MediaMetadataCompat

data class Song(
    val id: String,
    val artist: String,
    val title: String,
    val path: String,
    val fileName: String,
    val duration: Int,
    val imgSong: Bitmap
) : Parcelable {
    constructor(source: Parcel) : this(
        source.readString(),
        source.readString(),
        source.readString(),
        source.readString(),
        source.readString(),
        source.readInt(),
        source.readParcelable<Bitmap>(Bitmap::class.java.classLoader)
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(id)
        writeString(artist)
        writeString(title)
        writeString(path)
        writeString(fileName)
        writeInt(duration)
        writeParcelable(imgSong, 0)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Song> = object : Parcelable.Creator<Song> {
            override fun createFromParcel(source: Parcel): Song = Song(source)
            override fun newArray(size: Int): Array<Song?> = arrayOfNulls(size)
        }
    }

    fun getMediaDescription(context: Context, song: Song): MediaDescriptionCompat {
        val extras = Bundle()
        extras.putParcelable(MediaMetadataCompat.METADATA_KEY_ALBUM_ART, song.imgSong)
        extras.putParcelable(MediaMetadataCompat.METADATA_KEY_DISPLAY_ICON, song.imgSong)
        return MediaDescriptionCompat.Builder()
            .setMediaId(song.id)
            .setIconBitmap(song.imgSong)
            .setTitle(song.title)
            .setDescription(song.artist)
            .setExtras(extras)
            .build()
    }

}