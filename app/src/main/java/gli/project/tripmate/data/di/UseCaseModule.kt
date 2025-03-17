package gli.project.tripmate.data.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import gli.project.tripmate.data.usecase.PlacesUseCaseImpl
import gli.project.tripmate.domain.repository.PlacesRepository
import gli.project.tripmate.domain.usecase.PlacesUseCase

@Module
@InstallIn(ViewModelComponent::class)
object UseCaseModule {

    @Provides
    fun providePlacesUseCase(repository: PlacesRepository): PlacesUseCase {
        return PlacesUseCaseImpl(repository)
    }
}