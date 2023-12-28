package ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import data.RnMCharacter

@Composable
fun RnMListItem(character: RnMCharacter, onClick: () -> Unit) {
    Row(
        modifier = Modifier.clickable(onClick = onClick)
    ) {
        AsyncImage(
            model = character.image,
            contentDescription = null,
            modifier = Modifier.size(64.dp)
        )

        Text(character.name)
    }
}

@Preview
@Composable
fun RnMListItemPreview() {
    RnMListItem(
        RnMCharacter(
            id = "1",
            name = "Rick Sanchez",
            gender = "gender",
            origin = "origin",
            species = "species",
            status = "status",
            image = "https://rickandmortyapi.com/api/character/avatar/1.jpeg"
        )
    ) {}
}
