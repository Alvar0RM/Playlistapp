package romero.alvaro.playlist.data.remote.dto

import com.google.gson.annotations.SerializedName

data class TrackDto(
  @SerializedName("id")
  val id: String,

  @SerializedName("name")
  val name: String,

  @SerializedName("duration_ms")
  val durationMs: Int,

  @SerializedName("track_number")
  val trackNumber: Int
)

data class TracksResponseDto(
  @SerializedName("items")
  val items: List<TrackDto>  // Los tracks vienen directamente aquí, no en un objeto "track"
)