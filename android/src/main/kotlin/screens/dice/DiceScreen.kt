package screens.dice

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import at.cgaisl.template.R
import kotlinx.coroutines.delay
import vibratePhone

@Composable
fun DiceScreen() {
    val viewModel = viewModel<DiceScreenViewModel>()
    val state by viewModel.state.collectAsState()
    val vibrateFunction = vibratePhone()

    LaunchedEffect(Unit) {
        viewModel.effects.collect { effect ->
            when (effect) {
                is DiceScreenEffect.DiceRolled -> {
                    vibrateFunction()
                }
            }
        }
    }

    DiceScreeContent(
        state = state,
        onEvent = viewModel::onEvent,
    )
}

@Composable
fun DiceScreeContent(
    state: DiceScreenState,
    onEvent: (DiceScreenEvent) -> Unit,
) {
    var targetRotation by remember { mutableStateOf(0f) }
    val rotation by animateFloatAsState(targetRotation)

    LaunchedEffect(state.currentDice) {
        targetRotation = -30f
        delay(100)
        targetRotation = 20f
        delay(100)
        targetRotation = -10f
        delay(100)
        targetRotation = 0f
    }

    val drawable = when (state.currentDice) {
        1 -> R.drawable.one
        2 -> R.drawable.two
        3 -> R.drawable.three
        4 -> R.drawable.four
        5 -> R.drawable.five
        6 -> R.drawable.six
        else -> throw IllegalStateException("Invalid dice value: ${state.currentDice}")
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Image(
            painter = painterResource(drawable),
            contentDescription = null,
            modifier = Modifier.rotate(rotation),
        )

        Spacer(modifier = Modifier.height(56.dp))

        ElevatedButton(
            onClick = { onEvent(DiceScreenEvent.RollDicePressed) },
        ) {
            Text("Roll Dice")
        }
    }

}

@Preview
@Composable
fun DiceScreenContentPreview() {
    DiceScreeContent(
        state = DiceScreenState(1),
        onEvent = {},
    )
}
