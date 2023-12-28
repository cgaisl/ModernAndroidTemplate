import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import org.koin.androidx.compose.KoinAndroidContext
import org.koin.core.annotation.KoinExperimentalAPI
import ui.screens.rnm.RnMListScreen

@OptIn(KoinExperimentalAPI::class)
@Composable
fun App() {
    KoinAndroidContext {
        MaterialTheme {
//                DiceScreen()
//            rnMScreenPresenter()
            RnMListScreen()
        }
    }
}


