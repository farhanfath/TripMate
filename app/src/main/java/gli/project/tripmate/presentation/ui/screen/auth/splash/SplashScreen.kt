package gli.project.tripmate.presentation.ui.screen.auth.splash

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import gli.project.tripmate.presentation.viewmodel.auth.UserViewModel
import kotlinx.coroutines.delay
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun SplashScreen(
    onUserAvailable: () -> Unit,
    onUserNotAvailable: () -> Unit
) {
    val viewModel: UserViewModel = hiltViewModel()
    val state by viewModel.appEntryState.collectAsState()

    // Mulai pengecekan status login saat composable pertama kali di-compose
    LaunchedEffect(Unit) {
        viewModel.checkUserLoginStatus()
    }

    // Tampilkan splash screen dengan timer dan logic navigasi
    var splashAnimationFinished by remember { mutableStateOf(false) }

    // Timer untuk minimum display time dari splash screen
    LaunchedEffect(key1 = true) {
        delay(3000) // Splash screen minimal tampil 3 detik
        splashAnimationFinished = true
    }

    LaunchedEffect(splashAnimationFinished, state.isLoading) {
        if (splashAnimationFinished && !state.isLoading) {
            if (state.user == null) {
                onUserNotAvailable()
            } else {
                onUserAvailable()
            }
        }
    }

    // Tampilkan splash screen animation
    TripMateSplashScreen()
}

@Composable
fun TripMateSplashScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            TripMateLogo(
                modifier = Modifier
                    .size(240.dp)
                    .padding(16.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "TripMate",
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                fontFamily = FontFamily.SansSerif
            )

            Text(
                text = "Your trusted travel companion",
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontFamily = FontFamily.SansSerif
            )
        }
    }
}

@Composable
fun TripMateLogo(modifier: Modifier = Modifier) {
    val infiniteTransition = rememberInfiniteTransition(label = "logoAnimation")

    // Animasi untuk handle tas yang bergerak ke atas dan ke bawah
    val handleOffsetY by infiniteTransition.animateFloat(
        initialValue = -10f,
        targetValue = 10f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "handleBounce"
    )

    // Animasi untuk tas yang bergoyang
    val bagRotation by infiniteTransition.animateFloat(
        initialValue = -5f,
        targetValue = 5f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "bagRotation"
    )

    // Animasi untuk icon lokasi yang muncul dari tas
    val locationIconScale by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = FastOutSlowInEasing, delayMillis = 300),
            repeatMode = RepeatMode.Restart,
        ),
        label = "locationScale"
    )

    // TAMBAHKAN: Animasi rotasi untuk icon-icon
    val iconRotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "iconRotation"
    )

    // Animasi untuk efek kilauan
    val sparkleAlpha by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000),
            repeatMode = RepeatMode.Reverse
        ),
        label = "sparkleAlpha"
    )

    val primaryColor = MaterialTheme.colorScheme.primary
    val secondaryColor = MaterialTheme.colorScheme.secondary
    val tertiaryColor = MaterialTheme.colorScheme.tertiary

    Canvas(modifier = modifier) {
        val centerX = size.width / 2
        val centerY = size.height / 2
        val bagWidth = size.width * 0.7f
        val bagHeight = size.height * 0.5f

        // Gambar shadow untuk tas
        drawRoundRect(
            color = Color.Gray.copy(alpha = 0.3f),
            topLeft = Offset(centerX - bagWidth / 2 + 10, centerY - bagHeight / 2 + bagHeight * 0.2f + 10),
            size = Size(bagWidth, bagHeight),
            cornerRadius = CornerRadius(30f, 30f),
            style = Fill
        )

        // Gambar tas dengan rotasi
        rotate(degrees = bagRotation) {
            // Badan tas
            drawRoundRect(
                brush = Brush.linearGradient(
                    colors = listOf(
                        primaryColor,
                        primaryColor.copy(alpha = 0.9f)
                    )
                ),
                topLeft = Offset(centerX - bagWidth / 2, centerY - bagHeight / 2 + bagHeight * 0.2f),
                size = Size(bagWidth, bagHeight),
                cornerRadius = CornerRadius(30f, 30f),
                style = Fill
            )

            // Garis aksen pada tas
            drawRoundRect(
                color = primaryColor.copy(alpha = 0.7f),
                topLeft = Offset(centerX - bagWidth / 2, centerY - bagHeight / 2 + bagHeight * 0.2f),
                size = Size(bagWidth, bagHeight),
                cornerRadius = CornerRadius(30f, 30f),
                style = Stroke(width = 8f)
            )

            // Kantong tas
            drawRoundRect(
                color = primaryColor.copy(alpha = 0.8f),
                topLeft = Offset(centerX - bagWidth / 4, centerY - bagHeight / 4 + bagHeight * 0.3f),
                size = Size(bagWidth / 2, bagHeight / 3),
                cornerRadius = CornerRadius(15f, 15f),
                style = Stroke(width = 4f)
            )

            // Risleting
            drawLine(
                color = Color.White,
                start = Offset(centerX, centerY - bagHeight / 2 + bagHeight * 0.2f),
                end = Offset(centerX, centerY + bagHeight / 2 + bagHeight * 0.2f),
                strokeWidth = 4f
            )

            // Handle tas (dengan efek animasi)
            val handleWidth = bagWidth * 0.5f
            val handleHeight = handleWidth * 0.6f

            drawArc(
                color = secondaryColor,
                startAngle = 0f,
                sweepAngle = 180f,
                useCenter = false,
                topLeft = Offset(
                    centerX - handleWidth / 2,
                    centerY - bagHeight / 2 - handleHeight + handleOffsetY
                ),
                size = Size(handleWidth, handleHeight * 2),
                style = Stroke(width = 10f, cap = StrokeCap.Round)
            )
        }

        // Icon lokasi yang keluar dari tas dengan animasi
        for (i in 0 until 3) {
            val angle = (i * 45f - 45f) * (Math.PI / 180f)
            val distance = size.width * 0.3f * locationIconScale
            val x = centerX + cos(angle).toFloat() * distance
            val y = centerY - sin(angle).toFloat() * distance - bagHeight * 0.2f
            val iconSize = size.width * 0.12f * locationIconScale

            // Lingkaran belakang icon
            drawCircle(
                color = tertiaryColor.copy(alpha = 0.2f * locationIconScale),
                radius = iconSize * 1.2f,
                center = Offset(x, y)
            )

            // TAMBAHKAN: Rotasi untuk setiap icon
            // Gunakan iconRotation dengan kecepatan berbeda untuk setiap icon
            val individualRotation = (iconRotation + (i * 120)) % 360

            // Icon dengan rotasi
            rotate(degrees = individualRotation, pivot = Offset(x, y)) {
                // Icon berbeda untuk setiap lokasi
                when (i) {
                    0 -> { // Lokasi pin
                        drawCircle(
                            color = tertiaryColor,
                            radius = iconSize * 0.5f,
                            center = Offset(x, y - iconSize * 0.3f)
                        )

                        val path = Path().apply {
                            moveTo(x, y + iconSize * 0.7f)
                            lineTo(x - iconSize * 0.6f, y - iconSize * 0.3f)
                            lineTo(x + iconSize * 0.6f, y - iconSize * 0.3f)
                            close()
                        }
                        drawPath(path, color = tertiaryColor)
                    }

                    1 -> { // Pesawat
                        val planePath = Path().apply {
                            moveTo(x, y - iconSize * 0.5f)
                            lineTo(x + iconSize * 0.7f, y)
                            lineTo(x, y + iconSize * 0.5f)
                            lineTo(x - iconSize * 0.7f, y)
                            close()
                        }
                        drawPath(planePath, color = tertiaryColor)

                        // Sayap pesawat
                        drawLine(
                            color = tertiaryColor,
                            start = Offset(x - iconSize * 0.3f, y - iconSize * 0.2f),
                            end = Offset(x + iconSize * 0.3f, y - iconSize * 0.2f),
                            strokeWidth = iconSize * 0.2f
                        )
                    }

                    2 -> { // Kamera
                        drawRoundRect(
                            color = tertiaryColor,
                            topLeft = Offset(x - iconSize * 0.6f, y - iconSize * 0.4f),
                            size = Size(iconSize * 1.2f, iconSize * 0.8f),
                            cornerRadius = CornerRadius(iconSize * 0.1f, iconSize * 0.1f)
                        )

                        drawCircle(
                            color = Color.White,
                            radius = iconSize * 0.2f,
                            center = Offset(x, y)
                        )
                    }
                }
            }
        }

        // Efek kilauan di sekitar tas
        val sparklePositions = listOf(
            Offset(centerX - bagWidth * 0.4f, centerY - bagHeight * 0.3f),
            Offset(centerX + bagWidth * 0.4f, centerY - bagHeight * 0.1f),
            Offset(centerX - bagWidth * 0.2f, centerY + bagHeight * 0.2f),
            Offset(centerX + bagWidth * 0.3f, centerY + bagHeight * 0.4f)
        )

        sparklePositions.forEachIndexed { index, position ->
            val sparkSize = size.width * 0.06f
            drawCircle(
                color = Color.White.copy(alpha = sparkleAlpha * (if (index % 2 == 0) 0.7f else 0.4f)),
                radius = sparkSize * (if (index % 2 == 0) 1f else 0.7f),
                center = position
            )
        }
    }
}