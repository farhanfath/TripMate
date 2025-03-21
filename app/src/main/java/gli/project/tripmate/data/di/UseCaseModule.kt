package gli.project.tripmate.data.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import gli.project.tripmate.data.usecase.ChatUseCaseImpl
import gli.project.tripmate.data.usecase.LocationUseCaseImpl
import gli.project.tripmate.data.usecase.PlacesUseCaseImpl
import gli.project.tripmate.domain.repository.ChatRepository
import gli.project.tripmate.domain.repository.LocationRepository
import gli.project.tripmate.domain.repository.PlacesRepository
import gli.project.tripmate.domain.usecase.ChatUseCase
import gli.project.tripmate.domain.usecase.LocationUseCase
import gli.project.tripmate.domain.usecase.PlacesUseCase

@Module
@InstallIn(ViewModelComponent::class)
object UseCaseModule {

    @Provides
    fun providePlacesUseCase(repository: PlacesRepository): PlacesUseCase {
        return PlacesUseCaseImpl(repository)
    }

    @Provides
    fun provideLocationUseCase(repository: LocationRepository): LocationUseCase {
        return LocationUseCaseImpl(repository)
    }

    @Provides
    fun provideChatUseCase(repository: ChatRepository): ChatUseCase {
        return ChatUseCaseImpl(repository)
    }
}