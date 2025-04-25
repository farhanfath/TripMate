package gli.project.tripmate.data.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import gli.project.tripmate.BuildConfig
import gli.project.tripmate.data.remote.geoapify.GeoApiService
import gli.project.tripmate.data.remote.n8n.N8nApiService
import gli.project.tripmate.data.remote.pexels.PexelsApiService
import gli.project.tripmate.data.remote.travelai.TravelAiApiService
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {
    private const val GEO_API_BASE_URL = BuildConfig.GEO_API_BASE_URL
    private const val GEOPIFY_API_KEY = BuildConfig.GEOPIFY_API_KEY
    private const val PEXELS_API_BASE_URL = BuildConfig.PEXELS_API_BASE_URL
    private const val PEXELS_API_KEY = BuildConfig.PEXELS_API_KEY
    private const val N8N_BASE_URL = BuildConfig.N8N_BASE_URL
    private const val TRAVEL_AI_BASE_URL = BuildConfig.TRAVEL_AI_BASE_URL

    @Provides
    @Singleton
    @Named("GeoApiKey")
    fun provideGeoApiKey(): String {
        return GEOPIFY_API_KEY
    }

    @Provides
    @Singleton
    @Named("PexelsApiKey")
    fun providePexelsApiKey(): String {
        return PEXELS_API_KEY
    }

    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    @Provides
    @Singleton
    @Named("GeoApiKeyInterceptor")
    fun provideGeoApiKeyInterceptor(@Named("GeoApiKey") apiKey: String): Interceptor {
        return Interceptor { chain ->
            val originalRequest = chain.request()
            val newUrl = originalRequest.url.newBuilder()
                .addQueryParameter("apiKey", apiKey)
                .build()

            val newRequest = originalRequest.newBuilder()
                .url(newUrl)
                .build()

            chain.proceed(newRequest)
        }
    }

    @Provides
    @Singleton
    @Named("PexelsApiKeyInterceptor")
    fun providePexelsApiKeyInterceptor(@Named("PexelsApiKey") apiKey: String): Interceptor {
        return Interceptor { chain ->
            val request = chain.request().newBuilder()
                .addHeader("Authorization", apiKey)
                .build()

            chain.proceed(request)
        }
    }

    @Provides
    @Singleton
    @Named("GeoOkhttpClient")
    fun provideGeoOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor,
        @Named("GeoApiKeyInterceptor")apiKeyInterceptor: Interceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(apiKeyInterceptor)
            .addInterceptor(loggingInterceptor)
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    @Named("PexelsOkhttpClient")
    fun providePexelsOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor,
        @Named("PexelsApiKeyInterceptor")apiKeyInterceptor: Interceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(apiKeyInterceptor)
            .addInterceptor(loggingInterceptor)
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    @Named("N8nOkhttpClient")
    fun provideN8nOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    @Named("TravelAiOkhttpClient")
    fun provideTravelAiOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    @Named("GeoRetrofit")
    fun provideGeoRetrofit(@Named("GeoOkhttpClient") okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(GEO_API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }

    @Provides
    @Singleton
    @Named("PexelsRetrofit")
    fun providePexelsRetrofit(@Named("PexelsOkhttpClient") okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(PEXELS_API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }

    @Provides
    @Singleton
    @Named("N8nRetrofit")
    fun provideN8nRetrofit(@Named("N8nOkhttpClient") okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(N8N_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }

    @Provides
    @Singleton
    @Named("TravelAiRetrofit")
    fun provideTravelAiRetrofit(@Named("TravelAiOkhttpClient") okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(TRAVEL_AI_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }

    @Provides
    @Singleton
    @Named("GeoApiService")
    fun provideGeoApiService(@Named("GeoRetrofit") retrofit: Retrofit): GeoApiService {
        return retrofit.create(GeoApiService::class.java)
    }

    @Provides
    @Singleton
    @Named("PexelsApiService")
    fun providePexelsApiService(@Named("PexelsRetrofit") retrofit: Retrofit): PexelsApiService {
        return retrofit.create(PexelsApiService::class.java)
    }

    @Provides
    @Singleton
    @Named("N8nApiService")
    fun provideN8nApiService(@Named("N8nRetrofit") retrofit: Retrofit): N8nApiService {
        return retrofit.create(N8nApiService::class.java)
    }

    @Provides
    @Singleton
    @Named("TravelAiApiService")
    fun provideTravelAiApiService(@Named("TravelAiRetrofit") retrofit: Retrofit): TravelAiApiService {
        return retrofit.create(TravelAiApiService::class.java)
    }
}