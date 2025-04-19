package gli.project.tripmate.data.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import gli.project.tripmate.data.local.dao.FavoriteDao
import gli.project.tripmate.data.local.dao.RecentViewDao
import gli.project.tripmate.data.local.datasource.FavoriteDataSource
import gli.project.tripmate.data.local.datasource.FavoriteDataSourceImpl
import gli.project.tripmate.data.local.datasource.RecentViewDataSource
import gli.project.tripmate.data.local.datasource.RecentViewDataSourceImpl
import gli.project.tripmate.data.remote.gemini.GeminiApiService
import gli.project.tripmate.data.remote.gemini.datasource.GeminiDataSource
import gli.project.tripmate.data.remote.gemini.datasource.GeminiDataSourceImpl
import gli.project.tripmate.data.remote.geoapify.GeoApiService
import gli.project.tripmate.data.remote.geoapify.datasource.PlacesRemoteDataSource
import gli.project.tripmate.data.remote.geoapify.datasource.PlacesRemoteDataSourceImpl
import gli.project.tripmate.data.remote.n8n.N8nApiService
import gli.project.tripmate.data.remote.n8n.datasource.N8nDataSource
import gli.project.tripmate.data.remote.n8n.datasource.N8nDataSourceImpl
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

    @Provides
    @Singleton
    fun provideGeminiRemoteDataSource(@Named("GeminiApiService") apiService: GeminiApiService): GeminiDataSource {
        return GeminiDataSourceImpl(apiService)
    }

    @Provides
    @Singleton
    fun provideN8nRemoteDataSource(@Named("N8nApiService") apiService: N8nApiService): N8nDataSource {
        return N8nDataSourceImpl(apiService)
    }

    @Provides
    @Singleton
    fun provideRecentViewLocalDataSource(recentViewDao: RecentViewDao): RecentViewDataSource {
        return RecentViewDataSourceImpl(recentViewDao)
    }

    @Provides
    @Singleton
    fun provideFavoriteLocalDataSource(favorite: FavoriteDao): FavoriteDataSource {
        return FavoriteDataSourceImpl(favorite)
    }
}