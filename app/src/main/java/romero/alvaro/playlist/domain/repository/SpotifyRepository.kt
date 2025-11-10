package romero.alvaro.playlist.domain.repository

import romero.alvaro.playlist.domain.model.Album
import romero.alvaro.playlist.domain.model.Artist
import romero.alvaro.playlist.domain.model.Track

interface SpotifyRepository {
  suspend fun searchArtists(query: String, limit: Int = 20, offset: Int = 0): List<Artist>
  suspend fun getArtistAlbums(artistId: String, limit: Int = 20, offset: Int = 0): List<Album>
  suspend fun getAlbumTracks(albumId: String, limit: Int = 20, offset: Int = 0): List<Track>
}