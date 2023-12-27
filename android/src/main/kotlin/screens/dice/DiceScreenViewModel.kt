package screens.dice

import androidx.compose.runtime.*
import androidx.compose.ui.platform.AndroidUiDispatcher
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.cash.molecule.RecompositionMode.ContextClock
import app.cash.molecule.launchMolecule
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.StateFlow

data class DiceScreenState(
    val currentDice: Int,
)

sealed interface DiceScreenEvent {
    data object RollDicePressed : DiceScreenEvent
}

sealed interface DiceScreenEffect {
    data object DiceRolled : DiceScreenEffect
}

class DiceScreenViewModel : ViewModel() {
    private val scope = CoroutineScope(viewModelScope.coroutineContext + AndroidUiDispatcher.Main)

    private val events = MutableSharedFlow<DiceScreenEvent>(extraBufferCapacity = 20)
    private val _effects = MutableSharedFlow<DiceScreenEffect>(extraBufferCapacity = 20)
    val effects: Flow<DiceScreenEffect> = _effects

    val state: StateFlow<DiceScreenState> = scope.launchMolecule(ContextClock) {
        diceScreenMolecule(events) {
            _effects.tryEmit(it)
        }
    }

    fun onEvent(event: DiceScreenEvent) {
        events.tryEmit(event)
    }
}

@Composable
fun diceScreenMolecule(
    events: Flow<DiceScreenEvent>,
    emitEffect: (DiceScreenEffect) -> Unit,
): DiceScreenState {

    var currentDice by remember { mutableIntStateOf(1) }

    LaunchedEffect(Unit) {
        events.collect { event ->
            when (event) {
                is DiceScreenEvent.RollDicePressed -> {
                    currentDice = (1..6).random()
                    emitEffect(DiceScreenEffect.DiceRolled)
                }
            }
        }
    }

    return DiceScreenState(currentDice)
}
