import app.cash.molecule.RecompositionMode
import app.cash.molecule.moleculeFlow
import app.cash.turbine.test
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.test.runTest
import org.junit.Test
import ui.screens.dice.DiceScreenEffect
import ui.screens.dice.DiceScreenEvent
import ui.screens.dice.diceScreenPresenter
import kotlin.test.assertEquals

class DiceScreenPresenterTest {

    @Test
    fun `when RollDicePressed event is sent, DiceRolled effect is emitted`() = runTest {
        val effects = MutableSharedFlow<DiceScreenEffect>(extraBufferCapacity = 20)
        val events = MutableSharedFlow<DiceScreenEvent>(extraBufferCapacity = 20)

        moleculeFlow(RecompositionMode.Immediate) {
            diceScreenPresenter(events) { effects.tryEmit(it) }
        }.test {
            val state = awaitItem()
            assertEquals(1, state.currentDice)
            effects.test {
                events.tryEmit(DiceScreenEvent.RollDicePressed)
                assertEquals(DiceScreenEffect.DiceRolled, awaitItem())
            }
            cancelAndIgnoreRemainingEvents()
        }
    }
}
