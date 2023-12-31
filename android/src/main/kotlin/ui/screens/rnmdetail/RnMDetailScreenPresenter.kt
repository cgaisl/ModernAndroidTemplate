package ui.screens.rnmdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import data.RickAndMortyRepository
import data.RnMCharacter
import kotlinx.coroutines.flow.StateFlow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import ui.BaseViewModel
import utils.mapState

data class RnMDetailScreenState(
    val character: RnMCharacter
)

class RnMDetailScreenViewModelFactory(
    private val characterId: String,
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return RnMDetailScreenViewModel(characterId) as T
    }
}

class RnMDetailScreenViewModel(private val characterId: String) :
    BaseViewModel<RnMDetailScreenState, Unit, Unit>(), KoinComponent {
    private val repository by inject<RickAndMortyRepository>()

    override val state: StateFlow<RnMDetailScreenState> = repository.characters.mapState {
        RnMDetailScreenState(it.first { it.id == characterId })
    }
}
