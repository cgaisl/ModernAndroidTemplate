package at.cgaisl.template

import App
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import at.cgaisl.androidtemplate.db.Database
import data.DriverFactory
import data.RickAndMortyRepository
import org.koin.dsl.module


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            App()
        }
    }
}

val koinModule = module {
    single { RickAndMortyRepository() }
    single { Database(DriverFactory(get()).createDriver()).rnMDatabaseQueries }
}
