package gli.project.tripmate.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import gli.project.tripmate.data.local.converter.ListStringConverters
import gli.project.tripmate.data.local.dao.FavoriteDao
import gli.project.tripmate.data.local.dao.RecentViewDao
import gli.project.tripmate.data.local.model.FavoriteEntity
import gli.project.tripmate.data.local.model.RecentViewEntity

@Database(entities = [RecentViewEntity::class, FavoriteEntity::class], version = 1, exportSchema = false)
@TypeConverters(ListStringConverters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun recentViewDao(): RecentViewDao
    abstract fun favoriteDao() : FavoriteDao
}
