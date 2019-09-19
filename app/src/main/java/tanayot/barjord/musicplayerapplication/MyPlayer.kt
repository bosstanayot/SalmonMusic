package tanayot.barjord.musicplayerapplication

import android.content.Context
import android.support.v4.media.MediaDescriptionCompat
import android.support.v4.media.session.MediaSessionCompat
import androidx.lifecycle.LiveData
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.ext.mediasession.MediaSessionConnector
import com.google.android.exoplayer2.ext.mediasession.TimelineQueueNavigator
import com.google.android.exoplayer2.source.ConcatenatingMediaSource
import com.google.android.exoplayer2.source.TrackGroupArray
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.trackselection.TrackSelectionArray
import com.google.android.exoplayer2.ui.PlayerNotificationManager
import tanayot.barjord.musicplayerapplication.model.Music
import tanayot.barjord.musicplayerapplication.ui.DescriptionAdapter

class MyPlayer(private val context: Context) {
    private lateinit var playerNotificationManager:PlayerNotificationManager
    private lateinit var mediaSession:  MediaSessionCompat
    private lateinit var mediaSessionConnector: MediaSessionConnector

    fun initPlayer(
        concatenatingMediaSource: ConcatenatingMediaSource,
        descriptionAdapter: DescriptionAdapter,
        musicList: LiveData<ArrayList<Music>>,
        listener: PlayerListener): SimpleExoPlayer {
        val player = ExoPlayerFactory.newSimpleInstance(context, DefaultTrackSelector())

        player?.apply {
            prepare(concatenatingMediaSource)
            addListener(object : Player.EventListener{
                override fun onTracksChanged(trackGroups: TrackGroupArray?, trackSelections: TrackSelectionArray?) {
                   listener.onTracksChanged(player.currentWindowIndex)
                }
            })
        }

        playerNotificationManager = PlayerNotificationManager.createWithNotificationChannel(
            context,
            "playback_channel",
            R.string.playback_channel_name,
            R.string.playback_channel_description,
            1,
            descriptionAdapter)
        playerNotificationManager.setFastForwardIncrementMs(0)
        playerNotificationManager.setRewindIncrementMs(0)
        playerNotificationManager.setUseNavigationActionsInCompactView(true)
        playerNotificationManager.setPlayer(player)


        mediaSession = MediaSessionCompat(context, "audio_demo")
        mediaSession.isActive  =  true
        playerNotificationManager.setMediaSessionToken(mediaSession.sessionToken)

        mediaSessionConnector  = MediaSessionConnector(mediaSession)
        mediaSessionConnector.setQueueNavigator(object : TimelineQueueNavigator(mediaSession){
            override fun getMediaDescription(
                player: Player?,
                windowIndex: Int
            ): MediaDescriptionCompat {
                return Music.getMediaDescription(musicList.value?.get(windowIndex))
            }

        })
        mediaSessionConnector.setPlayer(player, null)
      return player
    }
}