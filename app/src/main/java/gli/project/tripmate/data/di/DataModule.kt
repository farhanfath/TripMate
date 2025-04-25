package gli.project.tripmate.data.di

import android.content.Context
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import gli.project.tripmate.data.helper.LocationHelper
import gli.project.tripmate.data.local.datasource.FavoriteDataSource
import gli.project.tripmate.data.local.datasource.RecentViewDataSource
import gli.project.tripmate.data.remote.geoapify.datasource.PlacesRemoteDataSource
import gli.project.tripmate.data.remote.n8n.datasource.N8nDataSource
import gli.project.tripmate.data.remote.pexels.datasource.PexelRemoteDataSource
import gli.project.tripmate.data.remote.travelai.datasource.TravelAiDataSource
import gli.project.tripmate.data.repository.FavoriteRepositoryImpl
import gli.project.tripmate.data.repository.LocationRepositoryImpl
import gli.project.tripmate.data.repository.N8nRepositoryImpl
import gli.project.tripmate.data.repository.PlacesRepositoryImpl
import gli.project.tripmate.data.repository.RatingRepositoryImpl
import gli.project.tripmate.data.repository.RecentViewRepositoryImpl
import gli.project.tripmate.data.repository.UserRepositoryImpl
import gli.project.tripmate.domain.repository.FavoriteRepository
import gli.project.tripmate.domain.repository.LocationRepository
import gli.project.tripmate.domain.repository.N8nRepository
import gli.project.tripmate.domain.repository.PlacesRepository
import gli.project.tripmate.domain.repository.RatingRepository
import gli.project.tripmate.domain.repository.RecentViewRepository
import gli.project.tripmate.domain.repository.UserRepository
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
    fun provideN8nRepository(
        n8nDataSource: N8nDataSource,
        travelAiDataSource: TravelAiDataSource
    ): N8nRepository {
        return N8nRepositoryImpl(n8nDataSource, travelAiDataSource)
    }

    @Provides
    @Singleton
    fun provideRecentViewRepository(
        recentViewDataSource: RecentViewDataSource
    ): RecentViewRepository {
        return RecentViewRepositoryImpl(recentViewDataSource)
    }

    @Provides
    @Singleton
    fun provideFavoriteRepository(
        favDataSource: FavoriteDataSource
    ): FavoriteRepository {
        return FavoriteRepositoryImpl(favDataSource)
    }

    @Provides
    @Singleton
    fun provideUserRepository(
        auth: FirebaseAuth,
        fireStore: FirebaseFirestore
    ): UserRepository {
        return UserRepositoryImpl(auth, fireStore)
    }

    @Provides
    @Singleton
    fun provideRatingRepository(
        fireStore: FirebaseFirestore
    ) : RatingRepository {
        return RatingRepositoryImpl(fireStore)
    }
}