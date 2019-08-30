package tanayot.barjord.musicplayerapplication.di

import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import tanayot.barjord.musicplayerapplication.repositories.SongRepository
import tanayot.barjord.musicplayerapplication.ui.PlayerViewModel
import tanayot.barjord.musicplayerapplication.viewmodels.MusicListViewModel

val musicModule = module {
    viewModel { MusicListViewModel(get()) }
    viewModel { PlayerViewModel(androidContext()) }
    single { SongRepository(androidContext()) }
}