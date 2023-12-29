import app.cash.turbine.TurbineTestContext
import ui.Rendering

suspend fun <State, Effect, EventSink> TurbineTestContext<Rendering<State, Effect, EventSink>>.awaitState() =
    awaitItem().state
