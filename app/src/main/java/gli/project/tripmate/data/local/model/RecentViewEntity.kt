package gli.project.tripmate.data.local.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "recent_view")
data class RecentViewEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val placeId: String,
    val placeName: String,
    val placeImage: String,
    val location: String,
    val categories: List<String>,
    val timeStamp: Long = System.currentTimeMillis()
) : Parcelable