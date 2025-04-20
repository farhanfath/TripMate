package gli.project.tripmate.data.di

import android.app.Application
import android.content.Context
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import gli.project.tripmate.data.helper.LocationDataStore
import gli.project.tripmate.data.helper.LocationHelper
import gli.project.tripmate.data.helper.n8n.SpeechRecognizerManager
import gli.project.tripmate.data.helper.n8n.TextToSpeechManager
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideFusedLocationProviderClient(
        @ApplicationContext context: Context
    ): FusedLocationProviderClient {
        return LocationServices.getFusedLocationProviderClient(context)
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

    @Provides
    @Singleton
    fun provideSpeechRecognizerManager(application: Application): SpeechRecognizerManager {
        return SpeechRecognizerManager(application)
    }

    @Provides
    @Singleton
    fun provideTextToSpeechManager(application: Application): TextToSpeechManager {
        return TextToSpeechManager(application)
    }
}