package tanayot.barjord.musicplayerapplication.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.app.NotificationManager
import android.content.Context
import tanayot.barjord.musicplayerapplication.ui.player.PlayerConstant


class MusicPlayerService: Service() {
    private var notificationManager: NotificationManager? = null
    override fun onBind(p0: Intent?): IBinder? { return null }

    override fun onTaskRemoved(rootIntent: Intent?) {
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?
        notificationManager?.cancel(PlayerConstant.NOTIFICATION_ID)
        stopSelf()
        super.onTaskRemoved(rootIntent)
    }
}