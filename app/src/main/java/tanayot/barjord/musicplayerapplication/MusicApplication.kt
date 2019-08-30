package tanayot.barjord.musicplayerapplication

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import tanayot.barjord.musicplayerapplication.di.musicModule

class MusicApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@MusicApplication)
            modules(musicModule)
        }
    }
}