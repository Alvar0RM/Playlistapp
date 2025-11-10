package romero.alvaro.playlist.data.remote.dto

import com.google.gson.annotations.SerializedName

data class ArtistDto(
  @SerializedName("id")
  val id: String,

  @SerializedName("name")
  val name: String,

  @SerializedName("images")
  val images: List<ImageDto>?,

  @SerializedName("followers")
  val followers: FollowersDto?
)

data class ImageDto(
  @SerializedName("url")
  val url: String,

  @SerializedName("height")
  val height: Int?,

  @SerializedName("width")
  val width: Int?
)

data class FollowersDto(
  @SerializedName("total")
  val total: Int
)

data class ArtistsResponseDto(
  @SerializedName("artists")
  val artists: ArtistsListDto
)

data class ArtistsListDto(
  @SerializedName("items")
  val items: List<ArtistDto>
)