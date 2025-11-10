package romero.alvaro.playlist.data.remote.service

import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import romero.alvaro.playlist.data.remote.auth.SpotifyAuthManager
import java.util.concurrent.TimeUnit

object RetrofitInstance {
  private const val BASE_URL = "https://api.spotify.com/v1/"

  private val authInterceptor = Interceptor { chain ->
    val token = runBlocking {
      SpotifyAuthManager.getAccessToken()
    }

    val request = chain.request().newBuilder()
      .addHeader("Authorization", "Bearer $token")
      .build()

    chain.proceed(request)
  }

  private val loggingInterceptor = HttpLoggingInterceptor().apply {
    level = HttpLoggingInterceptor.Level.BODY
  }

  private val client = OkHttpClient.Builder()
    .addInterceptor(authInterceptor)
    .addInterceptor(loggingInterceptor)
    .connectTimeout(30, TimeUnit.SECONDS)
    .readTimeout(30, TimeUnit.SECONDS)
    .build()

  val apiService: SpotifyApiService by lazy {
    Retrofit.Builder()
      .baseUrl(BASE_URL)
      .client(client)
      .addConverterFactory(GsonConverterFactory.create())
      .build()
      .create(SpotifyApiService::class.java)
  }
}