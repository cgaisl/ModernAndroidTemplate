package ui.screens.rnmlist

import LocalNavController
import Screen
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.viewmodel.compose.viewModel
import ui.components.RnMListItem
import ui.rendering

@Composable
fun RnMListScreen() {
    val navController = LocalNavController.current
    val (state, effects, eventSink) = viewModel<RnMListScreenViewModel>().rendering()

    LaunchedEffect(Unit) {
        effects.collect {
            when (it) {
                is RnmListScreenEffect.NavigateToDetail -> navController.navigate(Screen.RnMDetail.route(it.characterId))
            }
        }
    }

    RnMListScreenContent(
        state = state,
        eventSink = eventSink,
    )
}

@Composable
fun RnMListScreenContent(
    state: RnMListScreenState,
    eventSink: (RnMListScreenEvent) -> Unit
) {
    LazyColumn {
        items(state.characters) { character ->
            RnMListItem(
                character = character,
                onClick = { eventSink(RnMListScreenEvent.CharacterClicked(character)) }
            )
        }
    }
}
