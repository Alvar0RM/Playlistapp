package romero.alvaro.playlist.ui.screens.albums

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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import romero.alvaro.playlist.domain.model.Album
import romero.alvaro.playlist.ui.screens.albums.component.AlbumItem
import romero.alvaro.playlist.ui.screens.albums.viewmodel.AlbumsUiState
import romero.alvaro.playlist.ui.screens.albums.viewmodel.AlbumsViewModel

@Composable
fun Albums(
  artistId: String? = null,
  viewModel: AlbumsViewModel = viewModel(),
  onAlbumClick: (String) -> Unit = {},
  onBackClick: () -> Unit = {}
) {
  val uiState = viewModel.uiState.collectAsState().value
  val snackbarHostState = remember { SnackbarHostState() }

  LaunchedEffect(artistId) {
    artistId?.let {
      println("AlbumsScreen: Cargando álbumes para artista: $it")
      viewModel.loadAlbums(it)
    }
  }

  LaunchedEffect(uiState) {
    if (uiState is AlbumsUiState.Error) {
      println("AlbumsScreen: Error - ${uiState.message}")
      snackbarHostState.showSnackbar(uiState.message)
    }
  }

  Scaffold(
    topBar = {
      TopAppBar(
        title = { Text("Álbumes") },
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
        is AlbumsUiState.Loading -> {
          CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }

        is AlbumsUiState.Success -> {
          if (uiState.albums.isEmpty()) {
            EmptyAlbumsMessage()
          } else {
            AlbumsList(
              albums = uiState.albums,
              onAlbumClick = onAlbumClick
            )
          }
        }

        is AlbumsUiState.Error -> {
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
private fun AlbumsList(
  albums: List<Album>,
  onAlbumClick: (String) -> Unit
) {
  LazyColumn {
    items(albums) { album ->
      AlbumItem(
        album = album,
        onClick = { onAlbumClick(album.id) }
      )
    }
  }
}

@Composable
private fun EmptyAlbumsMessage() {
  Box(
    modifier = Modifier.fillMaxSize(),
    contentAlignment = Alignment.Center
  ) {
    Text(
      text = "No se encontraron álbumes",
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
fun AlbumsPreview() {
  Albums()
}