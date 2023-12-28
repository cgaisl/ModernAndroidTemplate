import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import ui.screens.dice.DiceScreen

@Composable
fun App() {
    MaterialTheme {
        DiceScreen()
    }
}


@Preview
@Composable
fun AppAndroidPreview() {
    App()
}
