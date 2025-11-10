package romero.alvaro.playlist.ui.screens.tracks.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import romero.alvaro.playlist.domain.usercase.GetAlbumTracksUseCase
import romero.alvaro.playlist.di.AppModule

class TracksViewModel : ViewModel() {

  private val getAlbumTracksUseCase: GetAlbumTracksUseCase by lazy {
    val repository = AppModule.provideSpotifyRepository()
    GetAlbumTracksUseCase(repository)
  }

  private val _uiState = MutableStateFlow<TracksUiState>(TracksUiState.Loading)
  val uiState: StateFlow<TracksUiState> = _uiState.asStateFlow()

  private var currentAlbumId: String = ""

  fun loadTracks(albumId: String) {
    currentAlbumId = albumId
    viewModelScope.launch {
      _uiState.value = TracksUiState.Loading
      println("TracksViewModel: Cargando canciones para álbum: $albumId")

      try {
        val tracks = getAlbumTracksUseCase(albumId, 20, 0)
        println("TracksViewModel: Canciones obtenidas: ${tracks.size}")

        if (tracks.isNotEmpty()) {
          _uiState.value = TracksUiState.Success(tracks)
          println("TracksViewModel: Estado -> Success con ${tracks.size} canciones")
        } else {
          _uiState.value = TracksUiState.Error("No se encontraron canciones para este álbum")
          println("TracksViewModel: Estado -> Error - lista vacía")
        }
      } catch (e: Exception) {
        val errorMessage = when {
          e.message?.contains("authentication", ignoreCase = true) == true -> {
            "Error de autenticación. Verifica tus credenciales de Spotify."
          }
          e.message?.contains("Unable to resolve host", ignoreCase = true) == true -> {
            "Error de conexión. Verifica tu internet."
          }
          else -> {
            "Error al cargar canciones: ${e.message ?: "Error desconocido"}"
          }
        }
        _uiState.value = TracksUiState.Error(errorMessage)
        println("TracksViewModel: Estado -> Error - $errorMessage")
      }
    }
  }

  fun retryLoading() {
    println("TracksViewModel: Reintentando carga...")
    if (currentAlbumId.isNotEmpty()) {
      loadTracks(currentAlbumId)
    }
  }
}