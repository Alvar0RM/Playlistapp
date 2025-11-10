package romero.alvaro.playlist.domain.usercase

import romero.alvaro.playlist.domain.model.Album
import romero.alvaro.playlist.domain.repository.SpotifyRepository
//import javax.inject.Inject

//class GetArtistAlbumsUseCase @Inject constructor(
class GetArtistAlbumsUseCase constructor(
  private val repository: SpotifyRepository
) {
  suspend operator fun invoke(artistId: String, limit: Int = 20, offset: Int = 0): List<Album> {
    return repository.getArtistAlbums(artistId, limit, offset)
  }
}