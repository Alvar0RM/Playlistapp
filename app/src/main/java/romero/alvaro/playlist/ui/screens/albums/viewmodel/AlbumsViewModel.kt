package romero.alvaro.playlist.ui.screens.albums.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import romero.alvaro.playlist.domain.usercase.GetArtistAlbumsUseCase
import romero.alvaro.playlist.di.AppModule

class AlbumsViewModel : ViewModel() {

  private val getArtistAlbumsUseCase: GetArtistAlbumsUseCase by lazy {
    val repository = AppModule.provideSpotifyRepository()
    GetArtistAlbumsUseCase(repository)
  }

  private val _uiState = MutableStateFlow<AlbumsUiState>(AlbumsUiState.Loading)
  val uiState: StateFlow<AlbumsUiState> = _uiState.asStateFlow()

  private var currentArtistId: String = ""

  fun loadAlbums(artistId: String) {
    currentArtistId = artistId
    viewModelScope.launch {
      _uiState.value = AlbumsUiState.Loading
      println("AlbumsViewModel: Cargando álbumes para artista: $artistId")

      try {
        val albums = getArtistAlbumsUseCase(artistId, 20, 0)
        println("AlbumsViewModel: Álbumes obtenidos: ${albums.size}")

        if (albums.isNotEmpty()) {
          _uiState.value = AlbumsUiState.Success(albums)
          println("AlbumsViewModel: Estado -> Success con ${albums.size} álbumes")
        } else {
          _uiState.value = AlbumsUiState.Error("No se encontraron álbumes para este artista")
          println("AlbumsViewModel: Estado -> Error - lista vacía")
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
            "Error al cargar álbumes: ${e.message ?: "Error desconocido"}"
          }
        }
        _uiState.value = AlbumsUiState.Error(errorMessage)
        println("AlbumsViewModel: Estado -> Error - $errorMessage")
      }
    }
  }

  fun retryLoading() {
    println("AlbumsViewModel: Reintentando carga...")
    if (currentArtistId.isNotEmpty()) {
      loadAlbums(currentArtistId)
    }
  }
}