import app.cash.molecule.RecompositionMode
import app.cash.molecule.moleculeFlow
import app.cash.turbine.test
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.test.runTest
import org.junit.Test
import ui.screens.dice.DiceScreenEffect
import ui.screens.dice.DiceScreenEvent
import ui.screens.dice.diceScreenPresenter
import kotlin.test.assertEquals

class DiceScreenPresenterTest {

    @Test
    fun `when RollDicePressed event is sent, DiceRolled effect is emitted`() = runTest {
        moleculeFlow(RecompositionMode.Immediate) {
            diceScreenPresenter()
        }.test {
            val (state, effects, eventSink) = awaitItem()
            assertEquals(1, state.currentDice)
            effects.onEach {
                println(it)
            }.test {
                eventSink(DiceScreenEvent.RollDicePressed)
                assertEquals(DiceScreenEffect.DiceRolled, awaitItem())
            }

            assert(awaitState().currentDice in 1..6)
        }
    }
}
