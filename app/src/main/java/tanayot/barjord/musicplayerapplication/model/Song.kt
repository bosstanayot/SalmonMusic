package tanayot.barjord.musicplayerapplication.model

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Parcel
import android.os.Parcelable

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
}