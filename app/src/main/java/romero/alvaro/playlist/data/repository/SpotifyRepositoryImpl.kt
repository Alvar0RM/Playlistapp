package romero.alvaro.playlist.data.repository

import android.util.Log
import romero.alvaro.playlist.data.mapper.toAlbum
import romero.alvaro.playlist.data.mapper.toArtist
import romero.alvaro.playlist.data.mapper.toTrack
import romero.alvaro.playlist.data.remote.service.RetrofitInstance
import romero.alvaro.playlist.domain.model.Album
import romero.alvaro.playlist.domain.model.Artist
import romero.alvaro.playlist.domain.model.Track
import romero.alvaro.playlist.domain.repository.SpotifyRepository
//import javax.inject.Inject

//class SpotifyRepositoryImpl @Inject constructor() : SpotifyRepository {
class SpotifyRepositoryImpl constructor() : SpotifyRepository {

  private val apiService = RetrofitInstance.apiService
  private val tag = "SpotifyRepository"

  override suspend fun searchArtists(query: String, limit: Int, offset: Int): List<Artist> {
    Log.d(tag, "Buscando artistas: query=$query, limit=$limit, offset=$offset")
    try {
      val response = apiService.searchArtists(
        query = query,
        limit = limit,
        offset = offset
      )
      Log.d(tag, "Respuesta recibida, ${response.artists.items.size} artistas encontrados")

      val artists = response.artists.items.map { it.toArtist() }
      Log.d(tag, "Artistas mapeados: ${artists.size}")

      // Log del primer artista para debug
      if (artists.isNotEmpty()) {
        Log.d(tag, "Primer artista: ${artists.first().name}, imagen: ${artists.first().imageUrl}")
      }

      return artists
    } catch (e: Exception) {
      Log.e(tag, "Error al buscar artistas: ${e.message}", e)
      throw e
    }
  }

  override suspend fun getArtistAlbums(artistId: String, limit: Int, offset: Int): List<Album> {
    Log.d(tag, "Obteniendo álbumes para artista: $artistId")
    try {
      val response = apiService.getArtistAlbums(
        artistId = artistId,
        limit = limit,
        offset = offset
      )
      Log.d(tag, "Álbumes recibidos: ${response.items.size}")
      return response.items.map { it.toAlbum() }
    } catch (e: Exception) {
      Log.e(tag, "Error al obtener álbumes: ${e.message}", e)
      throw e
    }
  }

  override suspend fun getAlbumTracks(albumId: String, limit: Int, offset: Int): List<Track> {
    Log.d(tag, "Obteniendo canciones para álbum: $albumId")
    try {
      val response = apiService.getAlbumTracks(
        albumId = albumId,
        limit = limit,
        offset = offset
      )
      Log.d(tag, "Canciones recibidas: ${response.items.size}")

      // CORRECCIÓN: Los tracks vienen directamente en response.items
      val tracks = response.items.map { it.toTrack() }

      Log.d(tag, "Canciones mapeadas: ${tracks.size}")

      // Log de la primera canción para debug
      if (tracks.isNotEmpty()) {
        Log.d(tag, "Primera canción: ${tracks.first().name}, duración: ${tracks.first().durationMs}ms")
      } else {
        Log.d(tag, "Lista de canciones vacía después del mapeo")
      }

      return tracks
    } catch (e: Exception) {
      Log.e(tag, "Error al obtener canciones: ${e.message}", e)
      throw e
    }
  }
}