package romero.alvaro.playlist.data.remote.dto

import com.google.gson.annotations.SerializedName

data class AlbumDto(
  @SerializedName("id")
  val id: String,

  @SerializedName("name")
  val name: String,

  @SerializedName("images")
  val images: List<ImageDto>?,

  @SerializedName("release_date")
  val releaseDate: String,

  @SerializedName("total_tracks")
  val totalTracks: Int
)

data class AlbumsResponseDto(
  @SerializedName("items")
  val items: List<AlbumDto>
)