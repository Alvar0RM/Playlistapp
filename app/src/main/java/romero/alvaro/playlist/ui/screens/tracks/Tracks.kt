package romero.alvaro.playlist.ui.screens.tracks

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import romero.alvaro.playlist.domain.model.Track
import romero.alvaro.playlist.ui.screens.tracks.component.TrackItem
import romero.alvaro.playlist.ui.screens.tracks.viewmodel.TracksUiState
import romero.alvaro.playlist.ui.screens.tracks.viewmodel.TracksViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Tracks(
  albumId: String? = null,
  viewModel: TracksViewModel = viewModel(),
  onBackClick: () -> Unit = {}
) {
  val uiState = viewModel.uiState.collectAsState().value
  val snackbarHostState = remember { SnackbarHostState() }

  LaunchedEffect(albumId) {
    albumId?.let {
      println("TracksScreen: Cargando canciones para álbum: $it")
      viewModel.loadTracks(it)
    }
  }

  LaunchedEffect(uiState) {
    if (uiState is TracksUiState.Error) {
      println("TracksScreen: Error - ${uiState.message}")
      snackbarHostState.showSnackbar(uiState.message)
    }
  }

  Scaffold(
    topBar = {
      TopAppBar(
        title = { Text("Canciones") },
        navigationIcon = {
          IconButton(onClick = onBackClick) {
            Icon(
              imageVector = Icons.AutoMirrored.Filled.ArrowBack,
              contentDescription = "Volver atrás"
            )
          }
        },
      )
    },
    snackbarHost = { SnackbarHost(snackbarHostState) }
  ) { paddingValues ->
    Box(
      modifier = Modifier
        .fillMaxSize()
        .padding(paddingValues)
    ) {
      when (uiState) {
        is TracksUiState.Loading -> {
          CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }

        is TracksUiState.Success -> {
          if (uiState.tracks.isEmpty()) {
            EmptyTracksMessage()
          } else {
            TracksList(tracks = uiState.tracks)
          }
        }

        is TracksUiState.Error -> {
          ErrorState(
            message = uiState.message,
            onRetry = viewModel::retryLoading
          )
        }
      }
    }
  }
}

@Composable
private fun TracksList(tracks: List<Track>) {
  LazyColumn {
    items(tracks) { track ->
      TrackItem(track = track)
    }
  }
}

@Composable
private fun EmptyTracksMessage() {
  Box(
    modifier = Modifier.fillMaxSize(),
    contentAlignment = Alignment.Center
  ) {
    Text(
      text = "No se encontraron canciones",
      style = MaterialTheme.typography.bodyLarge
    )
  }
}

@Composable
private fun ErrorState(
  message: String,
  onRetry: () -> Unit
) {
  Column(
    modifier = Modifier
      .fillMaxSize()
      .padding(16.dp),
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    Text(
      text = message,
      style = MaterialTheme.typography.bodyMedium,
      modifier = Modifier.padding(bottom = 16.dp)
    )

    Button(onClick = onRetry) {
      Text("Reintentar")
    }
  }
}

@Preview
@Composable
fun TracksPreview() {
  Tracks()
}