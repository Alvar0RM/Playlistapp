package romero.alvaro.playlist.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import kotlinx.coroutines.delay
import romero.alvaro.playlist.ui.screens.LoadingScreen
import romero.alvaro.playlist.ui.screens.albums.Albums
import romero.alvaro.playlist.ui.screens.artists.Artists
import romero.alvaro.playlist.ui.screens.tracks.Tracks

sealed class Screen(val route: String) {
  object Artists : Screen("artists")
  object Albums : Screen("albums")
  object Tracks : Screen("tracks")
}

@Composable
fun AppNavigation() {
  val navController = rememberNavController()

  NavHost(
    navController = navController,
    startDestination = "loading"
  ) {
    composable(route = "loading") {
      LoadingScreen()

      LaunchedEffect (Unit) {
        delay(3000L)
        navController.navigate(Screen.Artists.route) {
          popUpTo("loading") { inclusive = true }
        }
      }
    }

    composable(route = Screen.Artists.route) {
      Artists(
        onArtistClick = { artistId ->
          println("Navegando a álbumes del artista: $artistId")
          navController.navigate("${Screen.Albums.route}/$artistId")
        }
      )
    }
    composable(
      route = "${Screen.Albums.route}/{artistId}",
      arguments = listOf(
        navArgument("artistId") { type = NavType.StringType }
      )
    ) { backStackEntry ->
      val artistId = backStackEntry.arguments?.getString("artistId")
      Albums(
        artistId = artistId,
        onAlbumClick = { albumId ->
          println("Navegando a canciones del álbum: $albumId")
          navController.navigate("${Screen.Tracks.route}/$albumId")
        },
        onBackClick = {
          navController.popBackStack()
        }
      )
    }
    composable(
      route = "${Screen.Tracks.route}/{albumId}",
      arguments = listOf(
        navArgument("albumId") { type = NavType.StringType }
      )
    ) { backStackEntry ->
      val albumId = backStackEntry.arguments?.getString("albumId")
      Tracks(
        albumId = albumId,
        onBackClick = {
          navController.popBackStack()
        }
      )
    }
  }
}