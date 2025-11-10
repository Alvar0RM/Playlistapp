package romero.alvaro.playlist.data.mapper

import romero.alvaro.playlist.data.remote.dto.AlbumDto
import romero.alvaro.playlist.domain.model.Album

fun AlbumDto.toAlbum(): Album {
  return Album(
    id = this.id,
    name = this.name,
    imageUrl = this.images?.firstOrNull()?.url,
    releaseDate = this.releaseDate,
    totalTracks = this.totalTracks
  )
}