package romero.alvaro.playlist.domain.model

data class Album(
  val id: String,
  val name: String,
  val imageUrl: String?,
  val releaseDate: String,
  val totalTracks: Int
)