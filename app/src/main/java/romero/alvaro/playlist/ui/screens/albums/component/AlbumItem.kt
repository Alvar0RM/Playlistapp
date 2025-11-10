package romero.alvaro.playlist.ui.screens.albums.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import romero.alvaro.playlist.R
import romero.alvaro.playlist.domain.model.Album

@Composable
fun AlbumItem(
  album: Album,
  modifier: Modifier = Modifier,
  onClick: () -> Unit = {}
) {
  Card(
    modifier = modifier
      .fillMaxWidth()
      .padding(horizontal = 16.dp, vertical = 8.dp),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    onClick = onClick
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      // Imagen del álbum
      AsyncImage(
        model = album.imageUrl ?: R.drawable.album,
        contentDescription = "Portada de ${album.name}",
        modifier = Modifier
          .size(80.dp)
          .clip(RoundedCornerShape(8.dp)),
        contentScale = ContentScale.Crop
      )

      Spacer(modifier = Modifier.width(16.dp))

      // Información del álbum
      Column(modifier = Modifier.weight(1f)) {
        Text(
          text = album.name,
          style = MaterialTheme.typography.titleMedium,
          fontWeight = FontWeight.Bold,
          maxLines = 2,
          overflow = TextOverflow.Ellipsis
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
          text = "Lanzamiento: ${formatReleaseDate(album.releaseDate)}",
          style = MaterialTheme.typography.bodySmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(2.dp))

        Text(
          text = "${album.totalTracks} canciones",
          style = MaterialTheme.typography.bodySmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
      }
    }
  }
}

private fun formatReleaseDate(releaseDate: String): String {
  return try {
    val parts = releaseDate.split("-")
    when (parts.size) {
      3 -> "${parts[2]}/${parts[1]}/${parts[0]}" // yyyy-mm-dd -> dd/mm/yyyy
      2 -> "${parts[1]}/${parts[0]}" // yyyy-mm -> mm/yyyy
      else -> releaseDate
    }
  } catch (e: Exception) {
    releaseDate
  }
}

@Preview
@Composable
fun AlbumItemPreview() {
  val mockAlbum = Album(
    id = "1",
    name = "Abbey Road",
    imageUrl = "https://i.scdn.co/image/ab67616d0000b273dc30583ba717007b00cceb25",
    releaseDate = "1969-09-26",
    totalTracks = 17
  )
  AlbumItem(album = mockAlbum)
}