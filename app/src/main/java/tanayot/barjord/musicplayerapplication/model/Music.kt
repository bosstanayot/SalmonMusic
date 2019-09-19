package tanayot.barjord.musicplayerapplication.model

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.github.kittinunf.fuel.core.ResponseDeserializable
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import android.os.AsyncTask
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.support.v4.media.MediaDescriptionCompat
import android.support.v4.media.MediaMetadataCompat
import androidx.recyclerview.widget.DiffUtil
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import android.R.attr.bitmap
import android.net.Uri


data class Music(
    @SerializedName("stream_url") val streamUrl: String = "",
    @SerializedName("pic_url") val picUrl: String = "",
    @SerializedName("title") val title: String = "",
    @SerializedName("artist") val artist: String = "",
    @SerializedName("id") val musicId: String = ""
) : Parcelable {
    constructor(source: Parcel) : this(
        source.readString(),
        source.readString(),
        source.readString(),
        source.readString(),
        source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(streamUrl)
        writeString(picUrl)
        writeString(title)
        writeString(artist)
        writeString(musicId)
    }

    companion object {
        val CALLBACK = object : DiffUtil.ItemCallback<Music>() {
            override fun areItemsTheSame(oldItem: Music, newItem: Music): Boolean =
                (oldItem.musicId == newItem.musicId)

            override fun areContentsTheSame(oldItem: Music, newItem: Music): Boolean =
                (oldItem == newItem)
        }

        fun getMediaDescription(music: Music?): MediaDescriptionCompat {
            val extras = Bundle()
            extras.putParcelable(
                MediaMetadataCompat.METADATA_KEY_ALBUM_ART,
                Uri.parse(music?.picUrl)
            )
            extras.putParcelable(
                MediaMetadataCompat.METADATA_KEY_DISPLAY_ICON,
                Uri.parse(music?.picUrl)
            )
            return MediaDescriptionCompat.Builder()
                .setMediaId(music?.musicId)
                .setIconUri(Uri.parse(music?.picUrl))
                .setTitle(music?.title)
                .setDescription(music?.artist)
                .setExtras(extras)
                .build()
        }

        @JvmField
        val CREATOR: Parcelable.Creator<Music> = object : Parcelable.Creator<Music> {
            override fun createFromParcel(source: Parcel): Music = Music(source)
            override fun newArray(size: Int): Array<Music?> = arrayOfNulls(size)
        }
    }
}