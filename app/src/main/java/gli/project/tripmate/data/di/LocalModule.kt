package gli.project.tripmate.data.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import gli.project.tripmate.data.local.AppDatabase
import gli.project.tripmate.data.local.RecentViewDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocalModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "recent_view_database"
        ).build()
    }

    // DAO (Data Access Object)
    @Provides
    @Singleton
    fun provideRecentViewDao(database: AppDatabase): RecentViewDao {
        return database.recentViewDao()
    }
}