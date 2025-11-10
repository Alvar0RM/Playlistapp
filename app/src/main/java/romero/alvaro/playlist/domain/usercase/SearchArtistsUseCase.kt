package romero.alvaro.playlist.domain.usercase

import romero.alvaro.playlist.domain.model.Artist
import romero.alvaro.playlist.domain.repository.SpotifyRepository
//import javax.inject.Inject

//class SearchArtistsUseCase @Inject constructor(
class SearchArtistsUseCase constructor(
  private val repository: SpotifyRepository
) {
  suspend operator fun invoke(query: String, limit: Int = 20, offset: Int = 0): List<Artist> {
    return repository.searchArtists(query, limit, offset)
  }
}