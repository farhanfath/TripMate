package gli.project.tripmate.data.local.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "place_favorite")
data class FavoriteEntity(
    @PrimaryKey val id: String,
    val placeId: String,
    val placeName: String,
    val placeImage: String,
    val location: String,
    val timeStamp: Long = System.currentTimeMillis()
) : Parcelable