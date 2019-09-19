package tanayot.barjord.musicplayerapplication.di

import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import tanayot.barjord.musicplayerapplication.ui.player.MyPlayer
import tanayot.barjord.musicplayerapplication.ui.adapter.DescriptionAdapter
import tanayot.barjord.musicplayerapplication.ui.viewmodels.MusicListViewModel

val musicModule = module {
    viewModel { MusicListViewModel(get(), get()) }
    single { MyPlayer(get()) }
    single { (viewModel: MusicListViewModel) ->
        DescriptionAdapter(
            androidContext(),
            viewModel
        )
    }
}