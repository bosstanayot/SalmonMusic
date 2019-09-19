package tanayot.barjord.musicplayerapplication.ui.player

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
import tanayot.barjord.musicplayerapplication.R
import tanayot.barjord.musicplayerapplication.model.Music
import tanayot.barjord.musicplayerapplication.ui.adapter.DescriptionAdapter

class MyPlayer(private val context: Context) {

    fun initPlayer(
        concatenatingMediaSource: ConcatenatingMediaSource,
        descriptionAdapter: DescriptionAdapter,
        musicList: LiveData<ArrayList<Music>>,
        listener: PlayerListener
    ): SimpleExoPlayer {

        val player = ExoPlayerFactory.newSimpleInstance(context, DefaultTrackSelector())
        player?.apply {
            prepare(concatenatingMediaSource)
            addListener(object : Player.EventListener{
                override fun onTracksChanged(trackGroups: TrackGroupArray?, trackSelections: TrackSelectionArray?) {
                   listener.onTracksChanged(player.currentWindowIndex)
                }
            })
        }
        val mediaSession = setMediaSession(musicList, player)
        setPlayerNotificationManager(mediaSession.sessionToken, descriptionAdapter, player)
        return player
    }

    private fun setMediaSession(musicList: LiveData<ArrayList<Music>>, player: SimpleExoPlayer): MediaSessionCompat{
        val mediaSession = MediaSessionCompat(context, PlayerConstant.MEDIA_SESSION_TAG)
        mediaSession.isActive  =  true
        val mediaSessionConnector  = MediaSessionConnector(mediaSession)
        mediaSessionConnector.setQueueNavigator(object : TimelineQueueNavigator(mediaSession){
            override fun getMediaDescription(player: Player?, windowIndex: Int): MediaDescriptionCompat {
                return Music.getMediaDescription(musicList.value?.get(windowIndex))
            }
        })
        mediaSessionConnector.setPlayer(player, null)
        return mediaSession
    }

    private fun setPlayerNotificationManager(sessionToken: MediaSessionCompat.Token, descriptionAdapter: DescriptionAdapter, player: SimpleExoPlayer){
        val playerNotificationManager = PlayerNotificationManager.createWithNotificationChannel(
            context,
            PlayerConstant.CHANNEL_ID,
            R.string.playback_channel_name,
            R.string.playback_channel_description,
            PlayerConstant.NOTIFICATION_ID,
            descriptionAdapter)
        playerNotificationManager.setFastForwardIncrementMs(0)
        playerNotificationManager.setRewindIncrementMs(0)
        playerNotificationManager.setUseNavigationActionsInCompactView(true)
        playerNotificationManager.setPlayer(player)
        playerNotificationManager.setMediaSessionToken(sessionToken)
    }
}