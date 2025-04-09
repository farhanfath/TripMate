package gli.project.tripmate.presentation.ui.screen.main.home.profile.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import gli.project.tripmate.presentation.ui.theme.SurfaceVariantLight

@Composable
fun ProfileSectionItem(
    icon : ImageVector,
    title : String,
    arrowVisible: Boolean,
    contentColor: Color = LocalContentColor.current,
    containerColor: Color = MaterialTheme.colorScheme.surface,
    onItemClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onItemClick()
            },
        color = SurfaceVariantLight,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    modifier = Modifier
                        .background(
                            shape = RoundedCornerShape(corner = CornerSize(10.dp)),
                            color = containerColor
                        )
                        .size(30.dp)
                        .padding(5.dp),
                    tint = contentColor
                )
                Text(
                    text = title,
                    color = contentColor,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold
                )
            }
            if (arrowVisible) {
                Icon(
                    modifier = Modifier.size(20.dp),
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = null
                )
            }
        }
    }
}