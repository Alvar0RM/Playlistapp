package romero.alvaro.playlist.data.remote.auth

import android.util.Base64
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import romero.alvaro.playlist.BuildConfig
import java.net.HttpURLConnection
import java.net.URL

object SpotifyAuthManager {
  private val CLIENT_ID = BuildConfig.SPOTIFY_CLIENT_ID
  private val CLIENT_SECRET = BuildConfig.SPOTIFY_CLIENT_SECRET
  private const val TOKEN_URL = "https://accounts.spotify.com/api/token"

  private var accessToken: String? = null
  private var tokenExpiryTime: Long = 0

  suspend fun getAccessToken(): String {
    if (accessToken != null && System.currentTimeMillis() < tokenExpiryTime) {
      return accessToken!!
    }

    return withContext(Dispatchers.IO) {
      try {
        val url = URL(TOKEN_URL)
        val connection = url.openConnection() as HttpURLConnection

        connection.requestMethod = "POST"
        connection.doOutput = true

        val credentials = "$CLIENT_ID:$CLIENT_SECRET"
        val auth = "Basic " + Base64.encodeToString(credentials.toByteArray(), Base64.NO_WRAP)
        connection.setRequestProperty("Authorization", auth)
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded")

        val output = "grant_type=client_credentials".toByteArray()
        connection.outputStream.write(output)

        val responseCode = connection.responseCode
        if (responseCode == HttpURLConnection.HTTP_OK) {
          val response = connection.inputStream.bufferedReader().use { it.readText() }
          val jsonObject = JSONObject(response)

          accessToken = jsonObject.getString("access_token")
          val expiresIn = jsonObject.getInt("expires_in")

          tokenExpiryTime = System.currentTimeMillis() + (expiresIn - 60) * 1000

          accessToken!!
        } else {
          throw Exception("Error al obtener token: $responseCode")
        }
      } catch (e: Exception) {
        throw Exception("Error de autenticación: ${e.message}")
      }
    }
  }
}