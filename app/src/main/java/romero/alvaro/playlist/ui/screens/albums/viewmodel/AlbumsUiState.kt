package romero.alvaro.playlist.ui.screens.albums.viewmodel

import romero.alvaro.playlist.domain.model.Album

sealed interface AlbumsUiState {
  object Loading : AlbumsUiState
  data class Success(val albums: List<Album>) : AlbumsUiState
  data class Error(val message: String) : AlbumsUiState
}