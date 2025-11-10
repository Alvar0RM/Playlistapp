package romero.alvaro.playlist.ui.screens.tracks.viewmodel

import romero.alvaro.playlist.domain.model.Track

sealed interface TracksUiState {
  object Loading : TracksUiState
  data class Success(val tracks: List<Track>) : TracksUiState
  data class Error(val message: String) : TracksUiState
}