package romero.alvaro.playlist.domain.usercase

import romero.alvaro.playlist.domain.model.Track
import romero.alvaro.playlist.domain.repository.SpotifyRepository
//import javax.inject.Inject

//class GetAlbumTracksUseCase @Inject constructor(
class GetAlbumTracksUseCase constructor(
  private val repository: SpotifyRepository
) {
  suspend operator fun invoke(albumId: String, limit: Int = 20, offset: Int = 0): List<Track> {
    return repository.getAlbumTracks(albumId, limit, offset)
  }
}