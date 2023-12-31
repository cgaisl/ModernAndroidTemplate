import app.cash.molecule.RecompositionMode
import app.cash.molecule.moleculeFlow
import app.cash.turbine.test
import data.RickAndMortyRepository
import data.RnMCharacter
import io.mockk.*
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.koin.core.context.startKoin
import org.koin.dsl.module
import ui.screens.rnmlist.RnMListScreenEvent
import ui.screens.rnmlist.RnMListScreenEvent.CharacterClicked
import ui.screens.rnmlist.RnmListScreenEffect
import ui.screens.rnmlist.RnmListScreenEffect.NavigateToDetail
import ui.screens.rnmlist.rnMListScreenPresenter
import kotlin.test.BeforeTest
import kotlin.test.assertEquals

class RnMListScreenPresenterTest {

    @BeforeTest
    fun setup() {
        val mockRepo = mockk<RickAndMortyRepository> {
            coEvery { loadCharacters() } just runs
            every { characters } returns MutableStateFlow(
                listOf(
                    RnMCharacter(
                        "1",
                        "Rick Sanchez",
                        "https://rickandmortyapi.com/api/character/avatar/1.jpeg",
                        "origin",
                        "species",
                        "gender",
                        "status",
                    )
                )
            )
        }
        startKoin { modules(module { single { mockRepo } }) }
    }

    @Test
    fun `characters are loaded and when character clicked, event is emitted`() = runTest {
        val events = MutableSharedFlow<RnMListScreenEvent>(extraBufferCapacity = 20)
        val effects = MutableSharedFlow<RnmListScreenEffect>(extraBufferCapacity = 20)

        moleculeFlow(RecompositionMode.Immediate) {
            rnMListScreenPresenter(events) { effects.tryEmit(it) }
        }.test {
            val state = awaitItem()
            assertEquals(1, state.characters.size)

            effects.test {
                events.tryEmit(CharacterClicked(state.characters.first()))
                assertEquals(
                    state.characters.first().id,
                    (awaitItem() as NavigateToDetail).characterId
                )
            }
        }
    }
}
