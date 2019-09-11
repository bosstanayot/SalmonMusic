package tanayot.barjord.musicplayerapplication.ui

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ui.PlayerNotificationManager
import tanayot.barjord.musicplayerapplication.MusicActivity
import tanayot.barjord.musicplayerapplication.viewmodels.MusicListViewModel

class DescriptionAdapter(private val context:Context,
                         private val viewModel: MusicListViewModel): PlayerNotificationManager.MediaDescriptionAdapter {
    override fun createCurrentContentIntent(player: Player?): PendingIntent? {
        return PendingIntent.getActivity(
            context.applicationContext,
            0,
            Intent(context.applicationContext, MusicActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP),
            PendingIntent.FLAG_CANCEL_CURRENT
        )
    }

    override fun getCurrentContentText(player: Player?): String? {
        return viewModel.musicList.value?.get(player!!.currentWindowIndex)?.artist
    }

    override fun getCurrentContentTitle(player: Player?): String? {
        return viewModel.musicList.value?.get(player!!.currentWindowIndex)?.title
    }

    override fun getCurrentLargeIcon(player: Player?, callback: PlayerNotificationManager.BitmapCallback?): Bitmap? {
        loadBitmap(viewModel.musicList.value?.get(player!!.currentWindowIndex)?.picUrl, callback)
        return null
    }

    private fun loadBitmap(url: String?, callback: PlayerNotificationManager.BitmapCallback?) {
        Glide.with(context)
            .asBitmap()
            .load(url)
            .into(object : CustomTarget<Bitmap>() {
                override fun onLoadCleared(placeholder: Drawable?) {}

                override fun onResourceReady(
                    resource: Bitmap,
                    transition: Transition<in Bitmap>?
                ) {
                    callback?.onBitmap(resource)
                }
            })
    }
}