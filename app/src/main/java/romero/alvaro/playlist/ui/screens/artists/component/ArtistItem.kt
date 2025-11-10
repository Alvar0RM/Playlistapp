package romero.alvaro.playlist.ui.screens.artists.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import romero.alvaro.playlist.R
import romero.alvaro.playlist.domain.model.Artist

@Composable
fun ArtistItem(
  artist: Artist,
  modifier: Modifier = Modifier,
  onClick: () -> Unit = {}
) {
  Card(
    modifier = modifier
      .fillMaxWidth()
      .padding(4.dp),
    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
    onClick = onClick
  ) {
    Column(
      modifier = Modifier.padding(12.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      // Imagen del artista (circular)
      AsyncImage(
        model = artist.imageUrl ?: R.drawable.artist,
        contentDescription = "Imagen de ${artist.name}",
        modifier = Modifier
          .size(100.dp)
          .clip(CircleShape),
        contentScale = ContentScale.Crop
      )

      Spacer(modifier = Modifier.height(8.dp))

      // Nombre del artista
      Text(
        text = artist.name,
        style = MaterialTheme.typography.bodyMedium,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center,
        maxLines = 2,
        overflow = TextOverflow.Ellipsis,
        modifier = Modifier.fillMaxWidth()
      )

      Spacer(modifier = Modifier.height(4.dp))

      // Seguidores (si están disponibles)
      artist.followers?.let { followers ->
        Text(
          text = "${formatFollowers(followers)} seguidores",
          style = MaterialTheme.typography.labelSmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
          textAlign = TextAlign.Center
        )
      }
    }
  }
}

private fun formatFollowers(followers: Int): String {
  return when {
    followers >= 1_000_000 -> "${followers / 1_000_000}M"
    followers >= 1_000 -> "${followers / 1_000}K"
    else -> followers.toString()
  }
}

@Preview
@Composable
fun ArtistItemPreview() {
  val mockArtist = Artist(
    id = "1",
    name = "The Beatles",
    imageUrl = "https://i.scdn.co/image/ab6761610000e5ebc9690bc711d04b3d4fd4b87c",
    followers = 25000000
  )
  ArtistItem(artist = mockArtist)
}