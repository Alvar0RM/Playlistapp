package romero.alvaro.playlist.domain.model

data class Track(
  val id: String,
  val name: String,
  val durationMs: Int,
  val trackNumber: Int
) {
  val durationFormatted: String
    get() {
      val minutes = durationMs / 60000
      val seconds = (durationMs % 60000) / 1000
      return String.format("%d:%02d", minutes, seconds)
    }
}