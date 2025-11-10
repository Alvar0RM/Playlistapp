package romero.alvaro.playlist.data.mapper

import romero.alvaro.playlist.data.remote.dto.ArtistDto
import romero.alvaro.playlist.domain.model.Artist

fun ArtistDto.toArtist(): Artist {
  return Artist(
    id = this.id,
    name = this.name,
    imageUrl = this.images?.firstOrNull()?.url, // Toma la primera imagen (la más grande)
    followers = this.followers?.total
  )
}