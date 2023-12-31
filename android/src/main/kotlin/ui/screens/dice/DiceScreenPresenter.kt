package ui.screens.dice

import androidx.compose.runtime.*
import kotlinx.coroutines.flow.Flow
import ui.BaseMoleculeViewModel

data class DiceScreenState(
    val currentDice: Int,
)

sealed interface DiceScreenEvent {
    data object RollDicePressed : DiceScreenEvent
}

sealed interface DiceScreenEffect {
    data object DiceRolled : DiceScreenEffect
}

class DiceScreenViewModel :
    BaseMoleculeViewModel<DiceScreenState, DiceScreenEffect, DiceScreenEvent>() {
    @Composable
    override fun renderState(): DiceScreenState = diceScreenPresenter(events) { _effects.tryEmit(it) }
}

@Composable
fun diceScreenPresenter(
    events: Flow<DiceScreenEvent>,
    effects: (DiceScreenEffect) -> Unit,
): DiceScreenState {
    var currentDice by remember { mutableIntStateOf(1) }

    LaunchedEffect(Unit) {
        events.collect { event ->
            when (event) {
                is DiceScreenEvent.RollDicePressed -> {
                    currentDice = (1..6).random()
                    effects(DiceScreenEffect.DiceRolled)
                }
            }
        }
    }

    return DiceScreenState(currentDice)
}
