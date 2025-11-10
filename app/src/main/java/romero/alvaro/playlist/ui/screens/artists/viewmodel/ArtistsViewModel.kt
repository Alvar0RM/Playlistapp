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

  private var currentOffset = 0
  private val limit = 20
  private var isLoadingMore = false
  private var canLoadMore = true
  private var currentQuery = "rock"
  private var searchJob: Job? = null

  private val allArtists = mutableListOf<romero.alvaro.playlist.domain.model.Artist>()

  init {
    searchArtists("rock")
  }

  fun searchArtists(query: String) {
    currentQuery = query
    searchJob?.cancel()

    if (currentOffset == 0) {
      _uiState.value = ArtistsUiState.Loading
      allArtists.clear()
      canLoadMore = true
    }

    searchJob = viewModelScope.launch {
      // Pequeño delay para evitar búsquedas muy frecuentes
      if (query != "rock") {
        delay(500)
      }

      if (currentOffset == 0) {
        _uiState.value = ArtistsUiState.Loading
      }

      println("ArtistsViewModel: Buscando artistas: '$query', offset: $currentOffset")

      try {
        val artists = searchArtistsUseCase(query, limit, currentOffset)
        println("ArtistsViewModel: Artistas obtenidos: ${artists.size}")

        allArtists.addAll(artists)

        val hasMoreData = artists.size == limit

        if (allArtists.isNotEmpty()) {
          _uiState.value = ArtistsUiState.Success(
            artists = allArtists.toList(),
            canLoadMore = hasMoreData
          )
          println("ArtistsViewModel: Estado -> Success con ${allArtists.size} artistas totales, canLoadMore: $hasMoreData")
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
      } finally {
        isLoadingMore = false
      }
    }
  }

  fun loadMoreArtists() {
    if (isLoadingMore || !canLoadMore) {
      println("ArtistsViewModel: loadMoreArtists ignorado - isLoadingMore: $isLoadingMore, canLoadMore: $canLoadMore")
      return
    }

    isLoadingMore = true
    currentOffset += limit
    println("ArtistsViewModel: Cargando más artistas, nuevo offset: $currentOffset")
    searchArtists(currentQuery)
  }

  fun resetPagination() {
    currentOffset = 0
    isLoadingMore = false
    canLoadMore = true
    allArtists.clear()
    println("ArtistsViewModel: Paginación reseteada")
  }

  fun retryLoading() {
    resetPagination()
    searchArtists(currentQuery)
  }
}