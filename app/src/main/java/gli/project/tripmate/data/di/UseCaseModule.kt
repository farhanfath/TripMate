package gli.project.tripmate.data.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import gli.project.tripmate.data.usecase.FavoriteUseCaseImpl
import gli.project.tripmate.data.usecase.LocationUseCaseImpl
import gli.project.tripmate.data.usecase.N8nUseCaseImpl
import gli.project.tripmate.data.usecase.PlacesUseCaseImpl
import gli.project.tripmate.data.usecase.RatingUseCaseImpl
import gli.project.tripmate.data.usecase.RecentViewUseCaseImpl
import gli.project.tripmate.data.usecase.UserUseCaseImpl
import gli.project.tripmate.domain.repository.FavoriteRepository
import gli.project.tripmate.domain.repository.LocationRepository
import gli.project.tripmate.domain.repository.N8nRepository
import gli.project.tripmate.domain.repository.PlacesRepository
import gli.project.tripmate.domain.repository.RatingRepository
import gli.project.tripmate.domain.repository.RecentViewRepository
import gli.project.tripmate.domain.repository.UserRepository
import gli.project.tripmate.domain.usecase.FavoriteUseCase
import gli.project.tripmate.domain.usecase.LocationUseCase
import gli.project.tripmate.domain.usecase.N8nUseCase
import gli.project.tripmate.domain.usecase.PlacesUseCase
import gli.project.tripmate.domain.usecase.RatingUseCase
import gli.project.tripmate.domain.usecase.RecentViewUseCase
import gli.project.tripmate.domain.usecase.UserUseCase

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
    fun provideN8nUseCase(repository: N8nRepository): N8nUseCase {
        return N8nUseCaseImpl(repository)
    }

    @Provides
    fun provideRecentViewUseCase(repository: RecentViewRepository): RecentViewUseCase {
        return RecentViewUseCaseImpl(repository)
    }

    @Provides
    fun provideFavoriteUseCase(repository: FavoriteRepository): FavoriteUseCase {
        return FavoriteUseCaseImpl(repository)
    }

    @Provides
    fun provideUserUseCase(repository: UserRepository): UserUseCase {
        return UserUseCaseImpl(repository)
    }

    @Provides
    fun provideRatingUseCase(repository: RatingRepository): RatingUseCase {
        return RatingUseCaseImpl(repository)
    }
}