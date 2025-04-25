package gli.project.tripmate.presentation.ui.screen.main.home.lobby.component.location

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.GpsFixed
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import gli.project.tripmate.presentation.ui.component.common.BaseModalBottomSheet

@Composable
fun LocationBottomSheet(
    isPermissionIssue: Boolean,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    isVisible: Boolean
) {
    BaseModalBottomSheet(
        isVisible = isVisible,
        onDismiss = onDismiss,
        fullScreen = true
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Animated location icon
            LocationAnimatedIcon(isPermissionIssue)


            // Title with accent color on part of text
            val titleText = if (isPermissionIssue) "Izin Lokasi Diperlukan" else "Layanan Lokasi Diperlukan"
            val titleParts = titleText.split(" ")

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                titleParts.forEachIndexed { index, part ->
                    Text(
                        text = part + if (index < titleParts.size - 1) " " else "",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (index == 0) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Description with better formatting
            Text(
                text = if (isPermissionIssue) {
                    "Aplikasi ini memerlukan akses ke lokasi Anda untuk menampilkan tempat terdekat. " +
                            "Mohon berikan izin untuk menggunakan fitur ini."
                } else {
                    "Aplikasi ini memerlukan layanan lokasi aktif untuk menampilkan tempat terdekat. " +
                            "Mohon aktifkan layanan lokasi untuk menggunakan fitur ini."
                },
                style = MaterialTheme.typography.bodyMedium.copy(
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                ),
                modifier = Modifier.padding(horizontal = 16.dp),
                lineHeight = 24.sp
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Improved feature cards with animation
            AnimatedFeatureCards()

            Spacer(modifier = Modifier.height(32.dp))

            // Enhanced privacy notice
            PrivacyNotice()

            Spacer(modifier = Modifier.height(24.dp))

            // Improved buttons with icon and effects
            Button(
                onClick = onConfirm,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Icon(
                    imageVector = if (isPermissionIssue) Icons.Default.LocationOn else Icons.Default.GpsFixed,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimary
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (isPermissionIssue) "Berikan Izin Lokasi" else "Aktifkan Layanan Lokasi",
                    modifier = Modifier.padding(vertical = 4.dp),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedButton(
                onClick = onDismiss,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(
                    text = "Nanti Saja",
                    modifier = Modifier.padding(vertical = 4.dp),
                    fontSize = 16.sp
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun LocationAnimatedIcon(isPermissionIssue: Boolean) {
    // Create pulsing animation
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseFactor by infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )

    val waveAlpha by infiniteTransition.animateFloat(
        initialValue = 0.1f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500),
            repeatMode = RepeatMode.Restart
        ),
        label = "wave"
    )

    // Container with location illustration
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(140.dp)
    ) {
        // Background waves (3 circles with decreasing opacity)
        Box(
            modifier = Modifier
                .size(120.dp)
                .scale(1.0f + (pulseFactor - 0.8f) * 0.5f)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary.copy(alpha = waveAlpha))
        )

        Box(
            modifier = Modifier
                .size(100.dp)
                .scale(1.0f + (pulseFactor - 0.8f) * 0.3f)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary.copy(alpha = waveAlpha + 0.05f))
        )

        Box(
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f))
        )

        // Main icon with scale animation
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(60.dp)
                .scale(pulseFactor * 0.9f)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.2f))
        ) {
            Icon(
                imageVector = if (isPermissionIssue) Icons.Default.LocationOn else Icons.Default.GpsFixed,
                contentDescription = "Location",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(40.dp)
            )
        }
    }
}

@Composable
fun AnimatedFeatureCards() {
    val features = listOf(
        Feature(Icons.Default.Place, "Temukan Tempat Terdekat", "Lihat rekomendasi lokasi menarik di sekitar Anda"),
        Feature(Icons.Default.DirectionsCar, "Navigasi Lebih Mudah", "Dapatkan petunjuk arah dan informasi jarak tempuh"),
        Feature(Icons.Default.LocalOffer, "Promo Terdekat", "Temukan penawaran khusus dari tempat-tempat di sekitar")
    )

    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(horizontal = 4.dp)
    ) {
        items(features) { feature ->
            EnhancedFeatureCard(
                icon = feature.icon,
                title = feature.title,
                description = feature.description
            )
        }
    }
}

@Composable
fun EnhancedFeatureCard(
    icon: ImageVector,
    title: String,
    description: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .width(180.dp)
            .height(160.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.3f)
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = title,
                fontSize = 16.sp,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = description,
                fontSize = 12.sp,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 16.sp
            )
        }
    }
}

@Composable
fun PrivacyNotice() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
    ) {
        Icon(
            imageVector = Icons.Default.Security,
            contentDescription = "Privacy",
            tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f),
            modifier = Modifier.size(16.dp)
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = "Kami hanya menggunakan data lokasi saat Anda menggunakan aplikasi",
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f),
            style = MaterialTheme.typography.bodySmall
        )
    }
}

// Data class for features
data class Feature(
    val icon: ImageVector,
    val title: String,
    val description: String
)