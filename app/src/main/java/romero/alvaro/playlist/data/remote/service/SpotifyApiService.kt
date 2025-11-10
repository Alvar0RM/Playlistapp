package romero.alvaro.playlist.data.remote.service

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import romero.alvaro.playlist.data.remote.dto.AlbumsResponseDto
import romero.alvaro.playlist.data.remote.dto.ArtistsResponseDto
import romero.alvaro.playlist.data.remote.dto.TracksResponseDto

interface SpotifyApiService {

  @GET("search")
  suspend fun searchArtists(
    @Query("q") query: String,
    @Query("type") type: String = "artist",
    @Query("limit") limit: Int = 20,
    @Query("offset") offset: Int = 0
  ): ArtistsResponseDto

  @GET("artists/{id}/albums")
  suspend fun getArtistAlbums(
    @Path("id") artistId: String,
    @Query("limit") limit: Int = 20,
    @Query("offset") offset: Int = 0
  ): AlbumsResponseDto

  @GET("albums/{id}/tracks")
  suspend fun getAlbumTracks(
    @Path("id") albumId: String,
    @Query("limit") limit: Int = 20,
    @Query("offset") offset: Int = 0
  ): TracksResponseDto
}