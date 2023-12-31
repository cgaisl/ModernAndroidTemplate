package ui

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.cash.molecule.AndroidUiDispatcher
import app.cash.molecule.RecompositionMode
import app.cash.molecule.launchMolecule
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.emptyFlow


abstract class BaseViewModel<State, Effect, Event> : ViewModel() {
    abstract val state: StateFlow<State>
    open val effects: Flow<Effect> = emptyFlow()
    open fun eventSink(event: Event) = Unit
}

abstract class BaseMoleculeViewModel<State, Effect, Event> : BaseViewModel<State, Effect, Event>() {
    private val scope = CoroutineScope(viewModelScope.coroutineContext + AndroidUiDispatcher.Main)
    protected val events = MutableSharedFlow<Event>(extraBufferCapacity = 20)
    protected val _effects = MutableSharedFlow<Effect>(extraBufferCapacity = 20)

    @Composable
    abstract fun renderState(): State

    override val state: StateFlow<State> by lazy(LazyThreadSafetyMode.NONE) {
        scope.launchMolecule(mode = RecompositionMode.ContextClock) {
            renderState()
        }
    }

    override val effects: Flow<Effect>
        get() = _effects

    override fun eventSink(event: Event) {
        events.tryEmit(event)
    }
}

@Composable
fun <State, Effect, Event> BaseViewModel<State, Effect, Event>.rendering(): Triple<State, Flow<Effect>, (Event) -> Unit> {
    return Triple(state.collectAsState().value, effects, ::eventSink)
}
