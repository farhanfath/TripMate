package gli.project.tripmate.presentation.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun CustomTopBar(
    additionalContent: @Composable () -> Unit = {}
) {
    Column {
        Box(
            modifier = Modifier
                .height(35.dp)
                .fillMaxWidth()
                .background(color = MaterialTheme.colorScheme.primary)
        )
        additionalContent()
    }
}

@Composable
fun CustomTopBarWithNavigation(
    title: String,
    onBackClick: () -> Unit
) {
    CustomTopBar(
        additionalContent = {
            Box(
                modifier = Modifier
                    .background(color = MaterialTheme.colorScheme.primary)
                    .fillMaxWidth()
                    .padding(10.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    IconButton(
                        onClick = {
                            onBackClick()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = "",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                    Text(
                        text = title,
                        color = MaterialTheme.colorScheme.onPrimary,
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }
        }
    )
}