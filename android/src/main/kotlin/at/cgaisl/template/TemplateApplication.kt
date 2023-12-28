package at.cgaisl.template

import android.app.Application
import data.RickAndMortyRepository
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

class TemplateApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@TemplateApplication)
            modules(koinModule)
        }
    }
}


val koinModule = module {
    singleOf(::RickAndMortyRepository)
}
