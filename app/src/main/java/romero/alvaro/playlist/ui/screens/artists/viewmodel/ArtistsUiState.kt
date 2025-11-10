package romero.alvaro.playlist.ui.screens.artists.viewmodel

import romero.alvaro.playlist.domain.model.Artist

sealed interface ArtistsUiState {
  object Loading : ArtistsUiState
  data class Success(
    val artists: List<Artist>,
    val canLoadMore: Boolean = true  // ← NUEVO PARÁMETRO
  ) : ArtistsUiState
  data class Error(val message: String) : ArtistsUiState
}