package romero.alvaro.playlist.domain.model

data class Artist(
  val id: String,
  val name: String,
  val imageUrl: String?,
  val followers: Int?
)