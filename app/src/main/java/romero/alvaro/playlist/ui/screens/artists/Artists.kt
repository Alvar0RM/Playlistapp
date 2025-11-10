package romero.alvaro.playlist.ui.screens.artists

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import romero.alvaro.playlist.ui.screens.artists.component.ArtistItem
import romero.alvaro.playlist.ui.screens.artists.component.SearchBar
import romero.alvaro.playlist.ui.screens.artists.viewmodel.ArtistsUiState
import romero.alvaro.playlist.ui.screens.artists.viewmodel.ArtistsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Artists(
  viewModel: ArtistsViewModel = viewModel(),
  onArtistClick: (String) -> Unit = {}
) {
  val uiState by viewModel.uiState.collectAsState()
  val snackbarHostState = remember { SnackbarHostState() }
  var showSearchBar by remember { mutableStateOf(false) }
  var searchQuery by remember { mutableStateOf("rock") }

  val lazyGridState = rememberLazyGridState()

  LaunchedEffect(lazyGridState) {
    snapshotFlow { lazyGridState.layoutInfo.visibleItemsInfo }
      .collect { visibleItems ->
        when (val currentState = uiState) {
          is ArtistsUiState.Success -> {
            if (visibleItems.isNotEmpty()) {
              val lastVisibleItem = visibleItems.last()
              val totalItems = currentState.artists.size

              if (lastVisibleItem.index >= totalItems - 3 && currentState.canLoadMore) {
                println("Artists: Llegando al final, cargando más artistas...")
                viewModel.loadMoreArtists()
              }
            }
          }
          else -> { /* No hacer nada para otros estados */ }
        }
      }
  }

  LaunchedEffect(searchQuery) {
    if (showSearchBar && searchQuery.isNotBlank()) {
      viewModel.resetPagination()
      viewModel.searchArtists(searchQuery)
    }
  }

  LaunchedEffect(uiState) {
    when (val currentState = uiState) {
      is ArtistsUiState.Error -> {
        val error = currentState.message
        snackbarHostState.showSnackbar(error)
      }
      else -> { /* No hacer nada para otros estados */ }
    }
  }

  Scaffold(
    topBar = {
      Column {
        TopAppBar(
          title = { Text("Artistas") },
          actions = {
            IconButton(
              onClick = {
                showSearchBar = !showSearchBar
                if (!showSearchBar) {
                  searchQuery = "rock"
                  viewModel.resetPagination()
                  viewModel.searchArtists("rock")
                }
              }
            ) {
              Icon(
                imageVector = Icons.Default.Search,
                contentDescription = if (showSearchBar) "Cerrar búsqueda" else "Buscar artistas"
              )
            }
          }
        )

        if (showSearchBar) {
          SearchBar(
            query = searchQuery,
            onQueryChange = { searchQuery = it },
            modifier = Modifier
              .fillMaxWidth()
              .padding(horizontal = 16.dp, vertical = 8.dp)
          )
        }
      }
    },
    snackbarHost = { SnackbarHost(snackbarHostState) }
  ) { paddingValues ->
    Box(
      modifier = Modifier
        .fillMaxSize()
        .padding(paddingValues)
    ) {
      when (val currentState = uiState) {
        is ArtistsUiState.Loading -> {
          CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }

        is ArtistsUiState.Success -> {
          val artists = currentState.artists
          val canLoadMore = currentState.canLoadMore

          if (artists.isEmpty()) {
            EmptyArtistsMessage(searchQuery)
          } else {
            ArtistsGrid(
              artists = artists,
              onArtistClick = onArtistClick,
              lazyGridState = lazyGridState,
              canLoadMore = canLoadMore
            )
          }
        }

        is ArtistsUiState.Error -> {
          ErrorState(
            message = currentState.message,
            onRetry = {
              viewModel.resetPagination()
              viewModel.searchArtists(searchQuery)
            }
          )
        }
      }
    }
  }
}

@Composable
fun ArtistsGrid(
  artists: List<romero.alvaro.playlist.domain.model.Artist>,
  onArtistClick: (String) -> Unit,
  lazyGridState: androidx.compose.foundation.lazy.grid.LazyGridState,
  canLoadMore: Boolean,
  modifier: Modifier = Modifier
) {
  LazyVerticalGrid(
    columns = GridCells.Adaptive(minSize = 160.dp),
    modifier = modifier.fillMaxSize(),
    contentPadding = PaddingValues(8.dp),
    horizontalArrangement = Arrangement.spacedBy(8.dp),
    verticalArrangement = Arrangement.spacedBy(8.dp),
    state = lazyGridState
  ) {
    items(artists) { artist ->
      ArtistItem(
        artist = artist,
        onClick = { onArtistClick(artist.id) }
      )
    }

    if (canLoadMore) {
      item {
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
          contentAlignment = Alignment.Center
        ) {
          CircularProgressIndicator()
        }
      }
    }
  }
}

@Composable
private fun EmptyArtistsMessage(query: String) {
  Box(
    modifier = Modifier.fillMaxSize(),
    contentAlignment = Alignment.Center
  ) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
      Text(
        text = "No se encontraron artistas",
        style = MaterialTheme.typography.bodyLarge
      )
      Text(
        text = "para \"$query\"",
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant
      )
    }
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
fun ArtistsPreview() {
  Artists()
}