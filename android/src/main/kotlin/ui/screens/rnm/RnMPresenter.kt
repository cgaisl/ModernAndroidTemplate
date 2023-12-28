package ui.screens.rnm

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import data.RickAndMortyRepository
import data.RnMCharacter
import org.koin.compose.koinInject
import ui.Rendering

data class RnMScreenState(
    val characters: List<RnMCharacter>
)

sealed interface RnMScreenEvent {
    data class CharacterClicked(val character: RnMCharacter) : RnMScreenEvent
}


@Composable
fun rnMScreenPresenter(): Rendering<RnMScreenState, Unit, RnMScreenEvent> {
    val repository = koinInject<RickAndMortyRepository>()

    LaunchedEffect(Unit) {
        repository.loadCharacters()
    }

    return Rendering(
        state = RnMScreenState(repository.characters.collectAsState().value),
    ) { event ->
        when (event) {
            is RnMScreenEvent.CharacterClicked -> println("Character clicked: ${event.character}")
        }
    }
}
