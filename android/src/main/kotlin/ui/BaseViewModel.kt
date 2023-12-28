package ui

import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.platform.AndroidUiDispatcher
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import app.cash.molecule.RecompositionMode
import app.cash.molecule.launchMolecule
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.emptyFlow
import java.util.*

@Composable
fun <State, Effect, Event> getRendering(
    key: String? = null,
    presenter: @Composable (MutableSharedFlow<Effect>) -> Rendering<State, Effect, Event>,
): Rendering<State, Effect, Event> {
    val vmKey = rememberSaveable { key ?: UUID.randomUUID().toString() }

    return viewModel<BaseViewModel<State, Effect, Event>>(
        key = vmKey,
        factory = ViewModelFactory { presenter(it) }
    ).rendering.collectAsState().value
}

data class Rendering<State, Effect, Event>(
    val state: State,
    val effects: Flow<Effect> = emptyFlow(),
    val eventSink: (Event) -> Unit
)

private class ViewModelFactory<State, Effect, Event>(
    private val presenter: @Composable (MutableSharedFlow<Effect>) -> Rendering<State, Effect, Event>,
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return BaseViewModel(presenter) as T
    }
}


private class BaseViewModel<State, Effect, Event>(
    presenter: @Composable (MutableSharedFlow<Effect>) -> Rendering<State, Effect, Event>,
) : ViewModel() {
    private val scope = CoroutineScope(viewModelScope.coroutineContext + AndroidUiDispatcher.Main)
    private val effects = MutableSharedFlow<Effect>(extraBufferCapacity = 20)

    val rendering: StateFlow<Rendering<State, Effect, Event>> = scope.launchMolecule(RecompositionMode.ContextClock) {

        presenter(effects)
    }
}
