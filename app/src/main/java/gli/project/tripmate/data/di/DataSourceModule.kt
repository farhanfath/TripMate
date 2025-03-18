package gli.project.tripmate.data.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import gli.project.tripmate.data.remote.ApiService
import gli.project.tripmate.data.remote.datasource.PlacesRemoteDataSource
import gli.project.tripmate.data.remote.datasource.PlacesRemoteDataSourceImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataSourceModule {

    @Provides
    @Singleton
    fun providePlacesRemoteDataSource(apiService: ApiService): PlacesRemoteDataSource {
        return PlacesRemoteDataSourceImpl(apiService)
    }

}