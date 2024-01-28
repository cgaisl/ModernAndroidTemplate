package ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.cash.molecule.RecompositionMode
import app.cash.molecule.launchMolecule
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.StateFlow

abstract class MoleculeViewModel<State, Effect, Event> : ViewModel() {
    private val events = MutableSharedFlow<Event>(extraBufferCapacity = 20)
    private val _effects = MutableSharedFlow<Effect>(extraBufferCapacity = 20)


    val state: StateFlow<State> by lazy(LazyThreadSafetyMode.NONE) {
        viewModelScope.launchMolecule(mode = RecompositionMode.Immediate) {
            state(events) {
                _effects.tryEmit(it)
            }
        }
    }
    val effects: Flow<Effect> = _effects
    fun eventSink(event: Event) {
        events.tryEmit(event)
    }

    @Composable
    protected abstract fun state(events: Flow<Event>, effectSink: (Effect) -> Unit): State

    // convenience function to be used with Kotlin destructuring
    // i.e.: val (state, effects, eventSink) = viewModel.rendering()
    @Composable
    fun rendering(): Triple<State, Flow<Effect>, (Event) -> Unit> {
        return Triple(state.collectAsState().value, effects) { eventSink(it) }
    }
}
