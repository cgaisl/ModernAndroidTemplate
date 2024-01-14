package ui.screens.rnmlist

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import data.RickAndMortyRepository
import data.RnMCharacter
import kotlinx.coroutines.flow.MutableSharedFlow
import org.koin.compose.koinInject
import ui.Rendering

data class RnMListScreenState(
    val characters: List<RnMCharacter>
)

sealed interface RnmListScreenEffect {
    class NavigateToDetail(val characterId: String) : RnmListScreenEffect
}

sealed interface RnMListScreenEvent {
    data class CharacterClicked(val character: RnMCharacter) : RnMListScreenEvent
}

@Composable
fun rnMListScreenPresenter(): Rendering<RnMListScreenState, RnmListScreenEffect, RnMListScreenEvent> {
    val events = remember { MutableSharedFlow<RnMListScreenEvent>(extraBufferCapacity = 20) }
    val effects = remember { MutableSharedFlow<RnmListScreenEffect>(extraBufferCapacity = 20) }
    val repository = koinInject<RickAndMortyRepository>()

    LaunchedEffect(Unit) {
        repository.reloadCharactersFromNetwork()
        events.collect { event ->
            when (event) {
                is RnMListScreenEvent.CharacterClicked -> {
                    effects.tryEmit(RnmListScreenEffect.NavigateToDetail(event.character.id))
                }
            }
        }
    }

    return Rendering(
        state = RnMListScreenState(repository.characters.collectAsState().value),
        effects = effects,
        eventSink = { events.tryEmit(it) }
    )
}
