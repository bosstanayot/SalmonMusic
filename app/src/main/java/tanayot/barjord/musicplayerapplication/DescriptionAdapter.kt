package tanayot.barjord.musicplayerapplication

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ui.PlayerNotificationManager
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
        return viewModel.getSongs()[player!!.currentWindowIndex].artist
    }

    override fun getCurrentContentTitle(player: Player?): String {
        return viewModel.getSongs()[player!!.currentWindowIndex].title
    }

    override fun getCurrentLargeIcon(player: Player?, callback: PlayerNotificationManager.BitmapCallback?): Bitmap? {
        return viewModel.getSongs()[player!!.currentWindowIndex].imgSong
    }
}