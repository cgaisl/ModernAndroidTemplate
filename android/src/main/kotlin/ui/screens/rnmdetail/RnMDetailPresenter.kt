package ui.screens.rnmdetail

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import data.RickAndMortyRepository
import data.RnMCharacter
import kotlinx.coroutines.flow.emptyFlow
import org.koin.compose.koinInject
import ui.Rendering

data class RnMDetailState(
    val character: RnMCharacter
)

@Composable
fun rnMDetailPresenter(
    characterId: String,
): Rendering<RnMDetailState, Unit, Unit> {
    val repository = koinInject<RickAndMortyRepository>()

    val character = repository.characters.collectAsState().value.first { it.id == characterId }

    return Rendering(
        state = RnMDetailState(character),
        effects = emptyFlow()
    ) { }
}
