package tanayot.barjord.musicplayerapplication

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import tanayot.barjord.musicplayerapplication.di.musicModule

class MusicApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@MusicApplication)
            modules(musicModule)
        }
    }
}