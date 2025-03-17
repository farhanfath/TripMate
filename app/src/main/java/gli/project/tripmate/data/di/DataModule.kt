package gli.project.tripmate.data.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import gli.project.tripmate.data.remote.ApiService
import gli.project.tripmate.data.remote.datasource.PlacesRemoteDataSource
import gli.project.tripmate.data.remote.datasource.PlacesRemoteDataSourceImpl
import gli.project.tripmate.data.repository.PlacesRepositoryImpl
import gli.project.tripmate.domain.repository.PlacesRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun providePlacesRemoteDataSource(apiService: ApiService): PlacesRemoteDataSource {
        return PlacesRemoteDataSourceImpl(apiService)
    }

    @Provides
    @Singleton
    fun providePlacesRepository(
        remoteDataSource: PlacesRemoteDataSource,
    ): PlacesRepository {
        return PlacesRepositoryImpl(remoteDataSource)
    }
}