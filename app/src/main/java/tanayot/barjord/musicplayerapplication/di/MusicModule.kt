package tanayot.barjord.musicplayerapplication.di

import com.github.kittinunf.fuel.Fuel
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import tanayot.barjord.musicplayerapplication.LoadMoreMusicRepository
import tanayot.barjord.musicplayerapplication.MyPlayer
import tanayot.barjord.musicplayerapplication.ui.DescriptionAdapter
import tanayot.barjord.musicplayerapplication.repositories.MusicListRepository
import tanayot.barjord.musicplayerapplication.viewmodels.MusicListViewModel

val musicModule = module {
    viewModel { MusicListViewModel(get(), get()) }
    single { (viewModel: MusicListViewModel) ->
        DescriptionAdapter(
            androidContext(),
            viewModel
        )
    }
    single { LoadMoreMusicRepository(get()) }
    single { MusicListRepository(get()) }
    single { Fuel }
    single { MyPlayer(get()) }
}