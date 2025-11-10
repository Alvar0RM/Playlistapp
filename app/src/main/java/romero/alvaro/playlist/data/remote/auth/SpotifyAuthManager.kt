package romero.alvaro.playlist.data.remote.auth

import android.util.Base64
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

object SpotifyAuthManager {
  private const val CLIENT_ID = "6bdbfa172891436c9069939117e3272a" // Reemplaza con tu Client ID real
  private const val CLIENT_SECRET = "1d3c66793d9d462098bb1e7196a81722" // Reemplaza con tu Client Secret real
  private const val TOKEN_URL = "https://accounts.spotify.com/api/token"

  private var accessToken: String? = null
  private var tokenExpiryTime: Long = 0

  suspend fun getAccessToken(): String {
    // Si el token existe y no ha expirado, lo retornamos
    if (accessToken != null && System.currentTimeMillis() < tokenExpiryTime) {
      return accessToken!!
    }

    // Si no, obtenemos un nuevo token
    return withContext(Dispatchers.IO) {
      try {
        val url = URL(TOKEN_URL)
        val connection = url.openConnection() as HttpURLConnection

        // Configurar la conexión
        connection.requestMethod = "POST"
        connection.doOutput = true

        // Agregar headers de autenticación
        val credentials = "$CLIENT_ID:$CLIENT_SECRET"
        val auth = "Basic " + Base64.encodeToString(credentials.toByteArray(), Base64.NO_WRAP)
        connection.setRequestProperty("Authorization", auth)
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded")

        // Escribir el body de la request
        val output = "grant_type=client_credentials".toByteArray()
        connection.outputStream.write(output)

        // Leer la respuesta
        val responseCode = connection.responseCode
        if (responseCode == HttpURLConnection.HTTP_OK) {
          val response = connection.inputStream.bufferedReader().use { it.readText() }
          val jsonObject = JSONObject(response)

          accessToken = jsonObject.getString("access_token")
          val expiresIn = jsonObject.getInt("expires_in")

          // Calcular tiempo de expiración (restamos 60 segundos como margen)
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