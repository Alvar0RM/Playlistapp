package romero.alvaro.playlist.ui.screens.artists.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import romero.alvaro.playlist.domain.usercase.SearchArtistsUseCase
import romero.alvaro.playlist.di.AppModule

class ArtistsViewModel : ViewModel() {

  private val searchArtistsUseCase: SearchArtistsUseCase by lazy {
    val repository = AppModule.provideSpotifyRepository()
    SearchArtistsUseCase(repository)
  }

  private val _uiState = MutableStateFlow<ArtistsUiState>(ArtistsUiState.Loading)
  val uiState: StateFlow<ArtistsUiState> = _uiState.asStateFlow()

  private var currentQuery = "rock"
  private var searchJob: Job? = null

  init {
    searchArtists("rock")
  }

  fun searchArtists(query: String) {
    currentQuery = query
    searchJob?.cancel()

    searchJob = viewModelScope.launch {
      // Pequeño delay para evitar búsquedas muy frecuentes
      if (query != "rock") {
        delay(500) // Debounce de 500ms
      }

      _uiState.value = ArtistsUiState.Loading
      println("ArtistsViewModel: Buscando artistas: '$query'")

      try {
        val artists = searchArtistsUseCase(query, 20, 0)
        println("ArtistsViewModel: Artistas obtenidos: ${artists.size}")

        if (artists.isNotEmpty()) {
          _uiState.value = ArtistsUiState.Success(artists)
          println("ArtistsViewModel: Estado -> Success con ${artists.size} artistas")
        } else {
          _uiState.value = ArtistsUiState.Error("No se encontraron artistas para '$query'")
          println("ArtistsViewModel: Estado -> Error - lista vacía")
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
            "Error al buscar artistas: ${e.message ?: "Error desconocido"}"
          }
        }
        _uiState.value = ArtistsUiState.Error(errorMessage)
        println("ArtistsViewModel: Estado -> Error - $errorMessage")
      }
    }
  }

  fun retryLoading() {
    searchArtists(currentQuery)
  }
}