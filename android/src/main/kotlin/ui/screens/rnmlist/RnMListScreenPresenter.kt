package ui.screens.rnmlist

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import data.RickAndMortyRepository
import data.RnMCharacter
import kotlinx.coroutines.flow.Flow
import org.koin.compose.koinInject
import ui.BaseMoleculeViewModel

data class RnMListScreenState(
    val characters: List<RnMCharacter>
)

sealed interface RnmListScreenEffect {
    class NavigateToDetail(val characterId: String) : RnmListScreenEffect
}

sealed interface RnMListScreenEvent {
    data class CharacterClicked(val character: RnMCharacter) : RnMListScreenEvent
}

class RnMListScreenViewModel : BaseMoleculeViewModel<RnMListScreenState, RnmListScreenEffect, RnMListScreenEvent>() {
    @Composable
    override fun renderState(): RnMListScreenState {
        return rnMListScreenPresenter(events) { _effects.tryEmit(it) }
    }
}

@Composable
fun rnMListScreenPresenter(
    events: Flow<RnMListScreenEvent>,
    effectSink: (RnmListScreenEffect) -> Unit
): RnMListScreenState {
    val repository = koinInject<RickAndMortyRepository>()

    LaunchedEffect(Unit) {
        repository.loadCharacters()
        events.collect { event ->
            when (event) {
                is RnMListScreenEvent.CharacterClicked -> {
                    effectSink(RnmListScreenEffect.NavigateToDetail(event.character.id))
                }
            }
        }
    }

    return RnMListScreenState(repository.characters.collectAsState().value)
}
