package romero.alvaro.playlist.di

import romero.alvaro.playlist.data.repository.SpotifyRepositoryImpl
import romero.alvaro.playlist.domain.repository.SpotifyRepository
import romero.alvaro.playlist.domain.usercase.GetAlbumTracksUseCase
import romero.alvaro.playlist.domain.usercase.GetArtistAlbumsUseCase
import romero.alvaro.playlist.domain.usercase.SearchArtistsUseCase

object AppModule {

  fun provideSpotifyRepository(): SpotifyRepository {
    return SpotifyRepositoryImpl()
  }

  fun provideSearchArtistsUseCase(repository: SpotifyRepository): SearchArtistsUseCase {
    return SearchArtistsUseCase(repository)
  }

  fun provideGetArtistAlbumsUseCase(repository: SpotifyRepository): GetArtistAlbumsUseCase {
    return GetArtistAlbumsUseCase(repository)
  }

  fun provideGetAlbumTracksUseCase(repository: SpotifyRepository): GetAlbumTracksUseCase {
    return GetAlbumTracksUseCase(repository)
  }
}