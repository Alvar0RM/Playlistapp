package romero.alvaro.playlist.ui.screens.tracks.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import romero.alvaro.playlist.R
import romero.alvaro.playlist.domain.model.Track

@Composable
fun TrackItem(
  track: Track,
  modifier: Modifier = Modifier
) {
  Card(
    modifier = modifier
      .fillMaxWidth()
      .padding(horizontal = 16.dp, vertical = 4.dp),
    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      // Número de pista
      Text(
        text = "${track.trackNumber}",
        style = MaterialTheme.typography.bodyMedium,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.width(24.dp)
      )

      Spacer(modifier = Modifier.width(16.dp))

      Box() {
        Image(
          painter = painterResource(R.drawable.casete),
          contentDescription = "Disco de vinilo",
          modifier = Modifier.width(50.dp).align(Alignment.Center)
        )
        Icon(
          imageVector = Icons.Default.PlayArrow,
          contentDescription = "Canción",
          modifier = Modifier.align(Alignment.Center)
        )
      }


      Spacer(modifier = Modifier.width(16.dp))

      // Información de la canción
      Column(modifier = Modifier.weight(1f)) {
        Text(
          text = track.name,
          style = MaterialTheme.typography.bodyMedium,
          fontWeight = FontWeight.Medium,
          maxLines = 2,
          overflow = TextOverflow.Ellipsis
        )

        Spacer(modifier = Modifier.height(2.dp))

        Text(
          text = track.durationFormatted,
          style = MaterialTheme.typography.bodySmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
      }
    }
  }
}

@Preview
@Composable
fun TrackItemPreview() {
  val mockTrack = Track(
    id = "1",
    name = "Come Together",
    durationMs = 259000,
    trackNumber = 1
  )
  TrackItem(track = mockTrack)
}