package gli.project.tripmate.data.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import gli.project.tripmate.data.remote.geoapify.GeoApiService
import gli.project.tripmate.data.remote.geoapify.datasource.PlacesRemoteDataSource
import gli.project.tripmate.data.remote.geoapify.datasource.PlacesRemoteDataSourceImpl
import gli.project.tripmate.data.remote.pexels.PexelsApiService
import gli.project.tripmate.data.remote.pexels.datasource.PexelRemoteDataSource
import gli.project.tripmate.data.remote.pexels.datasource.PexelRemoteDataSourceImpl
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataSourceModule {

    @Provides
    @Singleton
    fun providePlacesRemoteDataSource(@Named("GeoApiService") geoApiService: GeoApiService): PlacesRemoteDataSource {
        return PlacesRemoteDataSourceImpl(geoApiService)
    }

    @Provides
    @Singleton
    fun providePexelRemoteDataSource(@Named("PexelsApiService") apiService: PexelsApiService): PexelRemoteDataSource {
        return PexelRemoteDataSourceImpl(apiService)
    }
}