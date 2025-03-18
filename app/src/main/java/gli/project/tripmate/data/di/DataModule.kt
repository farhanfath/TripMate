package gli.project.tripmate.data.di

import android.content.Context
import com.google.android.gms.location.FusedLocationProviderClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import gli.project.tripmate.data.remote.datasource.PlacesRemoteDataSource
import gli.project.tripmate.data.repository.LocationRepositoryImpl
import gli.project.tripmate.data.repository.PlacesRepositoryImpl
import gli.project.tripmate.domain.repository.LocationRepository
import gli.project.tripmate.domain.repository.PlacesRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun providePlacesRepository(
        remoteDataSource: PlacesRemoteDataSource,
    ): PlacesRepository {
        return PlacesRepositoryImpl(remoteDataSource)
    }

    @Provides
    @Singleton
    fun provideLocationRepository(
        @ApplicationContext context: Context,
        fusedLocationClient: FusedLocationProviderClient
    ): LocationRepository {
        return LocationRepositoryImpl(context, fusedLocationClient)
    }
}