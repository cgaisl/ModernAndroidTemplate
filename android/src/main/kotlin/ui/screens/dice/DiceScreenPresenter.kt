package ui.screens.dice

import androidx.compose.runtime.*
import kotlinx.coroutines.flow.MutableSharedFlow
import ui.Rendering

data class DiceScreenState(
    val currentDice: Int,
)

sealed interface DiceScreenEvent {
    data object RollDicePressed : DiceScreenEvent
}

sealed interface DiceScreenEffect {
    data object DiceRolled : DiceScreenEffect
}

@Composable
fun diceScreenPresenter(
    effects: MutableSharedFlow<DiceScreenEffect>,
): Rendering<DiceScreenState, DiceScreenEffect, DiceScreenEvent> {
    var currentDice by remember { mutableIntStateOf(1) }

    return Rendering(
        state = DiceScreenState(currentDice),
        effects = effects
    ) { event ->
        when (event) {
            is DiceScreenEvent.RollDicePressed -> {
                currentDice = (1..6).random()
                effects.tryEmit(DiceScreenEffect.DiceRolled)
            }
        }
    }
}
