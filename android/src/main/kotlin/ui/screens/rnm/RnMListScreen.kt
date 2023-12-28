package ui.screens.rnm

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import ui.components.RnMListItem
import ui.getRendering

@Composable
fun RnMListScreen() {
    val (state, _, eventSink) = getRendering { rnMScreenPresenter() }

    RnMListScreenContent(
        state = state,
        eventSink = eventSink,
    )
}

@Composable
fun RnMListScreenContent(
    state: RnMScreenState,
    eventSink: (RnMScreenEvent) -> Unit
) {
    LazyColumn {
        items(state.characters) { character ->
            RnMListItem(
                character = character,
                onClick = { eventSink(RnMScreenEvent.CharacterClicked(character)) }
            )
        }
    }
}
