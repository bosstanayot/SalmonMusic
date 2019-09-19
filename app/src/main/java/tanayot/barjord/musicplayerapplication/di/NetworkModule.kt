package tanayot.barjord.musicplayerapplication.di

import com.github.kittinunf.fuel.Fuel
import org.koin.dsl.module
import tanayot.barjord.musicplayerapplication.repositories.LoadMoreMusicRepository
import tanayot.barjord.musicplayerapplication.repositories.MusicListRepository

val networkModule = module {
    single { LoadMoreMusicRepository(get()) }
    single { MusicListRepository(get()) }
    single { Fuel }
}