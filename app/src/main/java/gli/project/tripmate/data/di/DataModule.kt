package gli.project.tripmate.data.di

import android.content.Context
import com.google.android.gms.location.FusedLocationProviderClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import gli.project.tripmate.data.helper.LocationDataStore
import gli.project.tripmate.data.helper.LocationHelper
import gli.project.tripmate.data.remote.gemini.datasource.GeminiDataSource
import gli.project.tripmate.data.remote.geoapify.datasource.PlacesRemoteDataSource
import gli.project.tripmate.data.remote.pexels.datasource.PexelRemoteDataSource
import gli.project.tripmate.data.repository.ChatRepositoryImpl
import gli.project.tripmate.data.repository.LocationRepositoryImpl
import gli.project.tripmate.data.repository.PlacesRepositoryImpl
import gli.project.tripmate.domain.repository.ChatRepository
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
        pexelRemoteDataSource: PexelRemoteDataSource
    ): PlacesRepository {
        return PlacesRepositoryImpl(remoteDataSource, pexelRemoteDataSource)
    }

    @Provides
    @Singleton
    fun provideLocationRepository(
        @ApplicationContext context: Context,
        fusedLocationClient: FusedLocationProviderClient,
        locationHelper: LocationHelper
    ): LocationRepository {
        return LocationRepositoryImpl(context, fusedLocationClient, locationHelper)
    }

    @Provides
    @Singleton
    fun provideChatRepository(
        chatDataSource: GeminiDataSource
    ): ChatRepository {
        return ChatRepositoryImpl(chatDataSource)
    }

    @Provides
    @Singleton
    fun provideLocationDataStore(): LocationDataStore {
        return LocationDataStore()
    }

    @Provides
    @Singleton
    fun provideLocationHelper(
        @ApplicationContext context: Context
    ): LocationHelper {
        return LocationHelper(context = context)
    }
}