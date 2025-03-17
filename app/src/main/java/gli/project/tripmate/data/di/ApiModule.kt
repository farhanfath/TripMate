package gli.project.tripmate.data.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import gli.project.tripmate.BuildConfig
import gli.project.tripmate.data.remote.ApiService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {
    private const val BASE_URL = BuildConfig.BASE_URL
    private const val API_KEY = BuildConfig.API_KEY

    @Provides
    @Singleton
    @Named("ApiKey")
    fun provideApiKey(): String {
        return API_KEY
    }

    @Provides
    @Singleton
    fun provideApiService(@Named("ApiKey") apiKey: String): ApiService {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(
                OkHttpClient.Builder()
                .addInterceptor(logging)
                .addInterceptor { chain ->
                    val original = chain.request()
                    val originalHttpUrl = original.url

                    val url = originalHttpUrl.newBuilder()
                        .addQueryParameter("apiKey", apiKey)
                        .build()

                    val requestBuilder = original.newBuilder()
                        .url(url)

                    val request = requestBuilder.build()
                    chain.proceed(request)
                }
                .build())
            .build()
            .create(ApiService::class.java)
    }
}