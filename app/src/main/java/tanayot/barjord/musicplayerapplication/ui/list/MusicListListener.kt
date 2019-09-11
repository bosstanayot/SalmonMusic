package tanayot.barjord.musicplayerapplication.ui.list

import tanayot.barjord.musicplayerapplication.model.Music

interface MusicListListener {
    fun onMusicClicked(
        music: Music?,
        position: Int
    )
}