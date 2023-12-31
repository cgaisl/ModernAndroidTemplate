package ui.screens.rnmdetail

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import data.RickAndMortyRepository
import data.RnMCharacter
import org.koin.compose.koinInject

data class RnMDetailScreenState(
    val character: RnMCharacter
)

@Composable
fun rnMDetailScreenPresenter(
    characterId: String,
): RnMDetailScreenState {
    val repository = koinInject<RickAndMortyRepository>()

    val character = repository.characters.collectAsState().value.first { it.id == characterId }

    return RnMDetailScreenState(character)
}
