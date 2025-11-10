package romero.alvaro.playlist.data.mapper

import romero.alvaro.playlist.data.remote.dto.TrackDto
import romero.alvaro.playlist.domain.model.Track

fun TrackDto.toTrack(): Track {
  return Track(
    id = this.id,
    name = this.name,
    durationMs = this.durationMs,
    trackNumber = this.trackNumber
  )
}