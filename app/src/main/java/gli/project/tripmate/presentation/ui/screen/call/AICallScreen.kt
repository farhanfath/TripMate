package gli.project.tripmate.presentation.ui.screen.call

import android.Manifest
import android.os.Build
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeOff
import androidx.compose.material.icons.filled.Assistant
import androidx.compose.material.icons.filled.CallEnd
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MicOff
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.accompanist.permissions.rememberPermissionState
import gli.project.tripmate.data.helper.n8n.startContinuousListening
import gli.project.tripmate.data.helper.n8n.stopContinuousListening
import gli.project.tripmate.presentation.ui.component.common.CustomImageLoader
import gli.project.tripmate.presentation.ui.component.common.CustomTopBar
import gli.project.tripmate.presentation.ui.screen.call.component.CustomerServiceDialog
import gli.project.tripmate.presentation.util.extensions.getNotification
import gli.project.tripmate.presentation.util.extensions.parseMarkdown
import gli.project.tripmate.presentation.viewmodel.main.N8nViewModel
import kotlinx.coroutines.delay
import java.util.Locale

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun N8nActiveCallScreen(
    onEndCallClick: () -> Unit,
    assistantName: String = "AI Assistant",
    assistantProfilePicUrl: String? = null,
    callStartTime: Long = System.currentTimeMillis(),
    viewModel: N8nViewModel = hiltViewModel(),
    onCustomerServiceIntent: () -> Unit
) {
    val conversation by viewModel.conversation.collectAsState()
    val isListening by viewModel.isListening.collectAsState()
    val isSpeaking by viewModel.isSpeaking.collectAsState()
    val currentTranscription by viewModel.currentTranscription.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    val coroutineScope = rememberCoroutineScope()
    val listState = rememberLazyListState()

    // Tambahkan state untuk tracking inisialisasi
    var isInitialized by remember { mutableStateOf(false) }

    // customer service handler
    var showCustomerServiceDialog by remember { mutableStateOf(false) }

    // Call timer
    var callDuration by remember { mutableIntStateOf(0) }
    LaunchedEffect(key1 = callStartTime) {
        while (true) {
            callDuration = ((System.currentTimeMillis() - callStartTime) / 1000).toInt()
            delay(1000)
        }
    }

    // Format call duration
    val formattedDuration = remember(callDuration) {
        val minutes = callDuration / 60
        val seconds = callDuration % 60
        String.format(Locale("id", "ID"), "%02d:%02d", minutes, seconds)
    }

    // Background gradient - dioptimasi menggunakan remember untuk menghindari rekomposisi
    val primaryColor = MaterialTheme.colorScheme.primary
    val inversePrimary = MaterialTheme.colorScheme.inversePrimary
    val gradientColors = remember {
        listOf(
            primaryColor,
            inversePrimary
        )
    }

    // Perbaikan request permission dengan dua langkah berbeda
    val recordAudioPermission = rememberPermissionState(Manifest.permission.RECORD_AUDIO)

    // Gabungkan semua permintaan izin ke dalam satu langkah untuk menghindari multiple requests
    val permissionsState = rememberMultiplePermissionsState(
        permissions = remember {
            buildList {
                add(Manifest.permission.RECORD_AUDIO) // Selalu tambahkan RECORD_AUDIO

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    add(Manifest.permission.READ_MEDIA_AUDIO)
                    add(Manifest.permission.POST_NOTIFICATIONS)
                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    add(Manifest.permission.BLUETOOTH_CONNECT)
                    add(Manifest.permission.READ_PHONE_STATE)
                } else {
                    add(Manifest.permission.READ_EXTERNAL_STORAGE)
                    add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    add(Manifest.permission.CAMERA)
                }
            }
        }
    )

    // Pisahkan init dari request permission
    LaunchedEffect(Unit) {
        // Reset audio services sekali saja di awal
        viewModel.resetAudioServices()

        // Request permissions sekali
        if (!permissionsState.allPermissionsGranted) {
            permissionsState.launchMultiplePermissionRequest()
        }

        // Berikan delay yang cukup sebelum memulai listening
        delay(500)

        // Mulai listening setelah inisialisasi selesai
        if (recordAudioPermission.status.isGranted) {
            viewModel.startContinuousListening(coroutineScope)
            isInitialized = true
        }
    }

    // Manajemen listening yang lebih responsif
    LaunchedEffect(isSpeaking, isLoading, isInitialized) {
        if (!isInitialized) return@LaunchedEffect

        if (isSpeaking || isLoading) {
            viewModel.stopListening()
        } else {
            // Berikan delay yang lebih pendek untuk responsivitas yang lebih baik
            delay(300)
            if (!isListening) {
                viewModel.startListening()
            }
        }
    }

    // Auto-scroll ke bawah dengan animasi yang lebih smooth
    LaunchedEffect(conversation.size) {
        if (conversation.isNotEmpty()) {
            delay(100) // Slight delay for smoother scrolling
            listState.animateScrollToItem(
                index = conversation.size - 1,
                scrollOffset = 0
            )
        }
    }

    // Show error snackbar if needed
    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(error) {
        error?.let {
            snackbarHostState.showSnackbar(
                message = it,
                duration = SnackbarDuration.Short
            )
            viewModel.clearError()
        }
    }

    // Clean up saat composable dibuang
    DisposableEffect(Unit) {
        onDispose {
            viewModel.stopContinuousListening()
            viewModel.stopSpeaking()
        }
    }

    // notification_handler
    DisposableEffect(Unit) {
        getNotification(
            coroutineScope = coroutineScope,
            onGetNotification = { _, _, _, _ ->
                showCustomerServiceDialog = true
            }
        )
    }

    Scaffold(
        topBar = {
            CustomTopBar()
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(brush = Brush.verticalGradient(gradientColors))
        ) {
            // Call info at the top
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 48.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = assistantName,
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Ongoing call • $formattedDuration",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.White.copy(alpha = 0.8f)
                )
            }

            // Conversation history (optimized visibility)
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .padding(top = 120.dp, bottom = 280.dp) // Adjusted bottom padding
                    .alpha(0.9f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(conversation) { item ->
                    ChatBubble(
                        message = item.text,
                        isUser = item.isUser
                    )
                }
            }

            // Avatar dan status section
            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 160.dp), // Adjusted position
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Optimasi animasi avatar
                val infiniteTransition = rememberInfiniteTransition(label = "avatarAnimation")
                val listeningScale by infiniteTransition.animateFloat(
                    initialValue = 1f,
                    targetValue = 1.1f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(700),
                        repeatMode = RepeatMode.Reverse
                    ),
                    label = "pulseAnimation"
                )

                val avatarScale = when {
                    isListening -> listeningScale
                    isSpeaking -> 1.05f
                    else -> 1f
                }

                Box(
                    modifier = Modifier
                        .size(120.dp) // Slightly smaller avatar
                        .scale(avatarScale)
                ) {
                    // Animated rings when listening - optimized with key
                    if (isListening) {
                        repeat(3) { index ->
                            val pulseScale by infiniteTransition.animateFloat(
                                initialValue = 1f,
                                targetValue = 2f,
                                animationSpec = infiniteRepeatable(
                                    animation = tween(2000, delayMillis = index * 600),
                                    repeatMode = RepeatMode.Restart
                                ),
                                label = "pulse$index"
                            )

                            val pulseAlpha by infiniteTransition.animateFloat(
                                initialValue = 0.4f,
                                targetValue = 0f,
                                animationSpec = infiniteRepeatable(
                                    animation = tween(2000, delayMillis = index * 600),
                                    repeatMode = RepeatMode.Restart
                                ),
                                label = "pulseAlpha$index"
                            )

                            Box(
                                modifier = Modifier
                                    .matchParentSize()
                                    .scale(pulseScale)
                                    .alpha(pulseAlpha)
                                    .clip(CircleShape)
                                    .background(Color(0xFF4CAF50).copy(alpha = 0.3f))
                            )
                        }
                    }

                    // Animated waves when speaking
                    if (isSpeaking) {
                        repeat(3) { index ->
                            val waveScale by infiniteTransition.animateFloat(
                                initialValue = 1f,
                                targetValue = 1.5f,
                                animationSpec = infiniteRepeatable(
                                    animation = tween(1000, delayMillis = index * 300),
                                    repeatMode = RepeatMode.Reverse
                                ),
                                label = "wave$index"
                            )

                            val waveAlpha by infiniteTransition.animateFloat(
                                initialValue = 0.3f,
                                targetValue = 0.1f,
                                animationSpec = infiniteRepeatable(
                                    animation = tween(1000, delayMillis = index * 300),
                                    repeatMode = RepeatMode.Reverse
                                ),
                                label = "waveAlpha$index"
                            )

                            Box(
                                modifier = Modifier
                                    .matchParentSize()
                                    .scale(waveScale)
                                    .alpha(waveAlpha)
                                    .clip(CircleShape)
                                    .background(Color(0xFF2196F3).copy(alpha = 0.3f))
                            )
                        }
                    }

                    // Warna avatar - dioptimasi menggunakan remember
                    val avatarBackgroundColor = when {
                        isListening -> Color(0xFF4CAF50).copy(alpha = 0.2f)
                        isSpeaking -> Color(0xFF2196F3).copy(alpha = 0.2f)
                        isLoading -> Color(0xFFFFA000).copy(alpha = 0.2f)
                        else -> MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.2f)
                    }

                    val avatarBorderColor = when {
                        isListening -> Color(0xFF4CAF50)
                        isSpeaking -> Color(0xFF2196F3)
                        isLoading -> Color(0xFFFFA000)
                        else -> Color.White
                    }

                    // Assistant avatar
                    Box(
                        modifier = Modifier
                            .size(180.dp)
                            .clip(CircleShape)
                            .background(avatarBackgroundColor)
                            .border(
                                width = 4.dp,
                                color = avatarBorderColor,
                                shape = CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        if (assistantProfilePicUrl != null) {
                            CustomImageLoader(
                                url = "",
                                desc = "Assistant profile picture",
                                scale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Filled.Assistant,
                                contentDescription = "Assistant",
                                tint = Color.White,
                                modifier = Modifier.size(65.dp)
                            )
                        }

                        // Loading indicator
                        if (isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier
                                    .matchParentSize()
                                    .padding(4.dp),
                                color = Color(0xFFFFA000),
                                strokeWidth = 4.dp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Status text - lebih jelas dengan warna yang sesuai state
                val statusText = when {
                    isLoading -> "Processing..."
                    isListening -> "Listening..."
                    isSpeaking -> "Speaking..."
                    else -> "Ready"
                }

                val statusColor = when {
                    isLoading -> Color(0xFFFFA000)
                    isListening -> Color(0xFF4CAF50)
                    isSpeaking -> Color(0xFF2196F3)
                    else -> Color.White
                }

                Text(
                    text = statusText,
                    color = statusColor,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                // Current transcription dengan animasi yang lebih smooth
                AnimatedVisibility(
                    visible = isListening && currentTranscription != null && currentTranscription!!.isNotBlank(),
                    enter = fadeIn() + expandVertically(),
                    exit = fadeOut() + shrinkVertically()
                ) {
                    Text(
                        text = currentTranscription ?: "",
                        color = Color.White.copy(alpha = 0.8f),
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .padding(top = 8.dp, start = 32.dp, end = 32.dp)
                            .alpha(0.8f)
                    )
                }
            }

            // Call actions at the bottom
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 48.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Call control buttons
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 40.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    val listenButtonEnabled = remember(isInitialized, isSpeaking, isLoading) {
                        isInitialized && !isSpeaking && !isLoading
                    }

                    // Warna button yang menunjukkan state enabled/disabled
                    val listenButtonColor = if (!listenButtonEnabled) {
                        MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
                    } else if (isListening) {
                        Color(0xFF4CAF50)
                    } else {
                        MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
                    }

                    CallControlButton(
                        icon = if (isListening) Icons.Filled.Mic else Icons.Filled.MicOff,
                        label = if (isListening) "Stop" else "Listen",
                        backgroundColor = listenButtonColor,
                        iconTint = Color.White,
                        enabled = listenButtonEnabled,
                        onClick = {
                            if (listenButtonEnabled) {
                                if (isListening) {
                                    viewModel.stopListening()
                                    viewModel.submitCurrentTranscription()
                                } else {
                                    viewModel.startListening()
                                }
                            }
                        }
                    )

                    // End call button
                    CallControlButton(
                        icon = Icons.Filled.CallEnd,
                        label = "End Call",
                        backgroundColor = Color.Red,
                        iconTint = Color.White,
                        onClick = {
                            viewModel.stopContinuousListening()
                            viewModel.stopSpeaking()
                            onEndCallClick()
                        },
                        size = 72.dp
                    )

                    // Stop speaking button
                    val speakButtonEnabled = remember(isSpeaking) { isSpeaking }
                    val speakButtonColor = if (isSpeaking) {
                        Color(0xFF2196F3)
                    } else {
                        MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
                    }

                    CallControlButton(
                        icon = Icons.AutoMirrored.Filled.VolumeOff,
                        label = if (isSpeaking) "Stop Voice" else "Voice Off",
                        backgroundColor = speakButtonColor,
                        iconTint = Color.White,
                        enabled = speakButtonEnabled,
                        onClick = {
                            if (isSpeaking) {
                                viewModel.stopSpeaking()
                            }
                        }
                    )
                }
            }
        }

        AnimatedVisibility(
            visible = showCustomerServiceDialog,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            CustomerServiceDialog(
                onDismissRequest = { showCustomerServiceDialog = false },
                onConnectConfirmed = {
                    onCustomerServiceIntent()
                }
            )
        }
    }
}

@Composable
fun ChatBubble(
    message: String,
    isUser: Boolean
) {
    val backgroundColor = if (isUser) {
        Color(0xFF2C5F2D).copy(alpha = 0.8f)
    } else {
        Color(0xFF2B4162).copy(alpha = 0.8f)
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start
    ) {
        Box(
            modifier = Modifier
                .widthIn(max = 300.dp)
                .clip(
                    RoundedCornerShape(
                        topStart = 16.dp,
                        topEnd = 16.dp,
                        bottomStart = if (isUser) 16.dp else 4.dp,
                        bottomEnd = if (isUser) 4.dp else 16.dp
                    )
                )
                .background(backgroundColor)
                .padding(12.dp)
        ) {
            Text(
                text = parseMarkdown(message),
                color = Color.White,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
fun CallControlButton(
    icon: ImageVector,
    label: String,
    backgroundColor: Color,
    iconTint: Color,
    onClick: () -> Unit,
    size: Dp = 60.dp,
    enabled: Boolean = true
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(size)
                .clip(CircleShape)
                .background(backgroundColor)
                .clickable(
                    onClick = onClick,
                    enabled = enabled,
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = iconTint.copy(alpha = if (enabled) 1f else 0.5f),
                modifier = Modifier.size(size * 0.5f)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = label,
            color = Color.White.copy(alpha = if (enabled) 1f else 0.5f),
            fontSize = 12.sp
        )
    }
}