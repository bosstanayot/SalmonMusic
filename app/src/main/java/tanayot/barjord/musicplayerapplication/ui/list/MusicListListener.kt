package tanayot.barjord.musicplayerapplication.ui.list

import tanayot.barjord.musicplayerapplication.model.Song

interface MusicListListener {
    fun onMusicClicked(song: Song?)
}