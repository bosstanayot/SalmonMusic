package tanayot.barjord.musicplayerapplication

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.SimpleExoPlayer
import android.app.NotificationManager
import android.content.Context
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T



class MusicPlayerService: Service() {
    var NOTIFICATION_ID = 1
    private var mNM: NotificationManager? = null
    override fun onBind(p0: Intent?): IBinder? { return null }

    override fun onTaskRemoved(rootIntent: Intent?) {
        mNM = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?
        mNM?.cancel(NOTIFICATION_ID)
        stopSelf()
        super.onTaskRemoved(rootIntent)
    }
}