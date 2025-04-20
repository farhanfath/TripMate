package gli.project.tripmate.presentation.ui.screen.main.chat

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import gli.project.tripmate.data.helper.n8n.startContinuousListening
import gli.project.tripmate.presentation.ui.component.common.CustomTopBarWithNavigation
import gli.project.tripmate.presentation.ui.screen.main.chat.component.MessageBubble
import gli.project.tripmate.presentation.ui.screen.main.chat.component.PulsatingDots
import gli.project.tripmate.presentation.ui.screen.main.chat.component.VoiceRecorderButton
import gli.project.tripmate.presentation.viewmodel.main.N8nViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun ConversationScreen(
    onBackClick: () -> Unit
) {
    val n8nViewModel: N8nViewModel = hiltViewModel()
    var inputText by remember { mutableStateOf("") }

    val conversation by n8nViewModel.conversation.collectAsState()
    val isListening by n8nViewModel.isListening.collectAsState()
    val isSpeaking by n8nViewModel.isSpeaking.collectAsState()
    val isLoading by n8nViewModel.isLoading.collectAsState()
    val error by n8nViewModel.error.collectAsState()

    val listState = rememberLazyListState()
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    val recordAudioPermission = rememberPermissionState(android.Manifest.permission.RECORD_AUDIO)

    // Request permission if not granted
    LaunchedEffect(Unit) {
        if (!recordAudioPermission.status.isGranted) {
            recordAudioPermission.launchPermissionRequest()
        }
    }

    LaunchedEffect(Unit) {
        // Reset semua layanan audio
        n8nViewModel.resetAudioServices()

        // Cek izin dan mulai listening
        if (!recordAudioPermission.status.isGranted) {
            recordAudioPermission.launchPermissionRequest()
        }
    }

    // Show error in Snackbar with improved styling
    LaunchedEffect(error) {
        error?.let {
            coroutineScope.launch {
                snackbarHostState.showSnackbar(
                    message = it,
                    duration = SnackbarDuration.Short
                )
                n8nViewModel.clearError()
            }
        }
    }

    // Scroll to bottom when new message is added
    LaunchedEffect(conversation.size) {
        if (conversation.isNotEmpty()) {
            listState.animateScrollToItem(conversation.size - 1)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "TripMate",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold
                            )
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .background(
                                    color = if (isListening || isSpeaking || isLoading)
                                        MaterialTheme.colorScheme.primary
                                    else
                                        Color.Green,
                                    shape = CircleShape
                                )
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.surface
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.surface
                ),
                modifier = Modifier.shadow(elevation = 4.dp)
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) { data ->
                Snackbar(
                    modifier = Modifier.padding(16.dp),
                    containerColor = MaterialTheme.colorScheme.errorContainer,
                    contentColor = MaterialTheme.colorScheme.onErrorContainer,
                    action = {
                        TextButton(
                            onClick = { data.dismiss() }
                        ) {
                            Text("Tutup")
                        }
                    }
                ) {
                    Text(data.visuals.message)
                }
            }
        },
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(
                    MaterialTheme.colorScheme.background.copy(alpha = 0.95f)
                )
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                // Status Indicator with animation
                AnimatedVisibility(
                    visible = isListening || isLoading || isSpeaking,
                    enter = fadeIn() + expandVertically(),
                    exit = fadeOut() + shrinkVertically()
                ) {
                    StatusIndicator(
                        isListening = isListening,
                        isLoading = isLoading,
                        isSpeaking = isSpeaking
                    )
                }

                // Main conversation area
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                ) {
                    // Empty state with clickable suggestions
                    if (conversation.isEmpty() && !isLoading) {
                        EmptyConversationState(
                            onSuggestionSelected = { suggestion ->
                                inputText = suggestion
                                // Request focus and show keyboard
                                coroutineScope.launch {
                                    delay(100) // Short delay to ensure UI updates
                                    focusRequester.requestFocus()
                                }
                            }
                        )
                    }

                    // Conversation Messages
                    LazyColumn(
                        state = listState,
                        contentPadding = PaddingValues(
                            top = 16.dp,
                            bottom = 24.dp,
                            start = 16.dp,
                            end = 16.dp
                        ),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(conversation) { item ->
                            MessageBubble(
                                item = item,
                                onTravelSpotClick = { travelSpot ->
                                    // Handle click on travel spot item
                                    coroutineScope.launch {
                                        snackbarHostState.showSnackbar("Selected: ${travelSpot.name}")
                                    }
                                }
                            )
                        }
                        when {
                            isLoading -> {
                                item {
                                    PulsatingDots()
                                }
                            }
                        }
                    }
                }

                // Input area with elegant styling
                InputArea(
                    inputText = inputText,
                    onInputTextChange = {
                        inputText = it
                    },
                    onSendClick = {
                        if (inputText.isNotBlank()) {
                            n8nViewModel.textProcessInput(inputText)
                            inputText = ""
                            focusManager.clearFocus() // Clear focus after sending
                        }
                    },
                    isListening = isListening,
                    onMicClick = {
                        if (isListening) {
                            n8nViewModel.stopListening()
                        } else {
                            n8nViewModel.startListening()
                        }
                    },
                    focusRequester = focusRequester
                )
            }
        }
    }
}

@Composable
fun StatusIndicator(
    isListening: Boolean,
    isLoading: Boolean,
    isSpeaking: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = when {
                    isListening -> MaterialTheme.colorScheme.primaryContainer
                    isLoading -> MaterialTheme.colorScheme.secondaryContainer
                    isSpeaking -> MaterialTheme.colorScheme.tertiaryContainer
                    else -> MaterialTheme.colorScheme.surface
                }
            )
            .padding(12.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        when {
            isListening -> {
                Icon(
                    imageVector = Icons.Default.Mic,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    "Mendengarkan...",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
            isLoading -> {
                CircularProgressIndicator(
                    modifier = Modifier.size(16.dp),
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                    strokeWidth = 2.dp
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    "Memproses...",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }
            isSpeaking -> {
                Icon(
                    imageVector = Icons.AutoMirrored.Default.VolumeUp,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onTertiaryContainer
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    "Berbicara...",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onTertiaryContainer
                )
            }
        }
    }
}

@Composable
fun EmptyConversationState(
    onSuggestionSelected: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.Explore,
            contentDescription = null,
            modifier = Modifier
                .size(80.dp)
                .padding(bottom = 16.dp),
            tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f)
        )
        Text(
            text = "Selamat Datang di TripMate",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Asisten perjalanan pintar yang siap menjawab pertanyaan dan memberikan rekomendasi destinasi wisata yang sesuai dengan preferensi Anda.",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
        )
        Spacer(modifier = Modifier.height(24.dp))
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Beberapa pertanyaan yang dapat Anda tanyakan :",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(8.dp))
                // sample suggestions with visual feedback
                SuggestedQuestion(
                    text = "Temukan tempat wisata di Bali",
                    onSelectedClick = onSuggestionSelected
                )
                SuggestedQuestion(
                    text = "Rekomendasi hotel di Jakarta",
                    onSelectedClick = onSuggestionSelected
                )
                SuggestedQuestion(
                    text = "Rute ke Monumen Nasional di Jakarta",
                    onSelectedClick = onSuggestionSelected
                )
            }
        }
    }
}

@Composable
fun SuggestedQuestion(
    text: String,
    onSelectedClick: (String) -> Unit
) {
    var isPressed by remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
                        isPressed = true
                        tryAwaitRelease()
                        isPressed = false
                    },
                    onTap = {
                        onSelectedClick(text)
                    }
                )
            },
        shape = RoundedCornerShape(8.dp),
        color = if (isPressed)
            MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
        else
            MaterialTheme.colorScheme.surface,
        tonalElevation = if (isPressed) 0.dp else 1.dp
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.ChatBubbleOutline,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = text,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
fun InputArea(
    inputText: String,
    onInputTextChange: (String) -> Unit,
    onSendClick: () -> Unit,
    isListening: Boolean,
    onMicClick: () -> Unit,
    focusRequester: FocusRequester
) {
    Surface(
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 6.dp,
        shadowElevation = 8.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 16.dp,
                    vertical = 12.dp
                )
        ) {
            OutlinedTextField(
                value = inputText,
                onValueChange = onInputTextChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 56.dp, max = 120.dp)
                    .focusRequester(focusRequester),
                placeholder = {
                    Box(
                        modifier = Modifier.width(180.dp)
                    ) {
                        Text(
                            text = "Tanya TripMate tentang perjalanan Anda...",
                            style = MaterialTheme.typography.bodyLarge,
                            maxLines = 1,
                            modifier = Modifier.basicMarquee()
                        )
                    }
                },
                trailingIcon = {
                    Row(
                        modifier = Modifier.padding(end = 18.dp)
                    ) {
                        IconButton(
                            onClick = onMicClick,
                            modifier = Modifier
                                .background(
                                    color = if (isListening) MaterialTheme.colorScheme.primary
                                    else MaterialTheme.colorScheme.surfaceVariant,
                                    shape = CircleShape
                                )
                                .size(40.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Mic,
                                contentDescription = "Microphone",
                                tint = if (isListening) MaterialTheme.colorScheme.onPrimary
                                else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        IconButton(
                            onClick = onSendClick,
                            enabled = inputText.isNotBlank(),
                            modifier = Modifier
                                .background(
                                    color = if (inputText.isNotBlank())
                                        MaterialTheme.colorScheme.primary
                                    else
                                        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                                    shape = CircleShape
                                )
                                .size(40.dp)
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.Send,
                                contentDescription = "Send Message",
                                tint = if (inputText.isNotBlank())
                                    MaterialTheme.colorScheme.onPrimary
                                else
                                    MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                            )
                        }
                    }
                },
                shape = RoundedCornerShape(24.dp),
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                    unfocusedIndicatorColor = MaterialTheme.colorScheme.outline
                ),
                textStyle = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

//@Composable
//fun ConversationScreen(
//    onBackClick: () -> Unit
//) {
//    val n8nViewModel: N8nViewModel = hiltViewModel()
//    var inputText by remember { mutableStateOf("") }
//
//    val conversation by n8nViewModel.conversation.collectAsState()
//    val isListening by n8nViewModel.isListening.collectAsState()
//    val isSpeaking by n8nViewModel.isSpeaking.collectAsState()
//    val isLoading by n8nViewModel.isLoading.collectAsState()
//    val error by n8nViewModel.error.collectAsState()
//
//    val listState = rememberLazyListState()
//    val snackbarHostState = remember { SnackbarHostState() }
//    val coroutineScope = rememberCoroutineScope()
//
//    // Show error in Snackbar
//    LaunchedEffect(error) {
//        error?.let {
//            coroutineScope.launch {
//                snackbarHostState.showSnackbar(it)
//                println(it)
//                n8nViewModel.clearError()
//            }
//        }
//    }
//
//    // Scroll to bottom when new message is added
//    LaunchedEffect(conversation.size) {
//        if (conversation.isNotEmpty()) {
//            listState.animateScrollToItem(conversation.size - 1)
//        }
//    }
//
//    Scaffold(
//        topBar = {
//            CustomTopBarWithNavigation(
//                title = "Trip Assistant",
//                onBackClick = onBackClick
//            )
//        },
//        snackbarHost = { SnackbarHost(snackbarHostState) },
//        modifier = Modifier.fillMaxSize()
//    ) { paddingValues ->
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(paddingValues)
//        ) {
//            // Status Indicator
//            Row(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(16.dp),
//                horizontalArrangement = Arrangement.Center,
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                when {
//                    isListening -> Text("Mendengarkan...")
//                    isLoading -> {
//                        CircularProgressIndicator(
//                            modifier = Modifier
//                                .width(16.dp)
//                                .height(16.dp)
//                        )
//                        Spacer(modifier = Modifier.width(8.dp))
//                        Text("Memproses...")
//                    }
//                    isSpeaking -> Text("Berbicara...")
//                    conversation.isEmpty() -> {
//                        Text("Tekan tombol untuk mulai berbicara")
//                    }
//                    else -> Text("Siap mendengarkan")
//                }
//            }
//
//            // Conversation Messages
//            LazyColumn(
//                state = listState,
//                contentPadding = PaddingValues(16.dp),
//                verticalArrangement = Arrangement.spacedBy(8.dp),
//                modifier = Modifier.weight(1f)
//            ) {
//                items(conversation) { item ->
//                    MessageBubble(
//                        item = item,
//                        onTravelSpotClick = { travelSpot ->
//                            // Handle click on travel spot item
//                            coroutineScope.launch {
//                                snackbarHostState.showSnackbar("Selected: ${travelSpot.name}")
//                                // Tambahkan tindakan lain jika diperlukan, seperti membuka detail, dsb.
//                            }
//                        }
//                    )
//                }
//                when {
//                    isLoading -> {
//                        item {
//                            PulsatingDots()
//                        }
//                    }
//                }
//            }
//            Row(
//                modifier = Modifier
//                    .background(color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.1f))
//                    .fillMaxWidth()
//                    .padding(16.dp),
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                OutlinedTextField(
//                    colors = TextFieldDefaults.colors(
//                        unfocusedContainerColor = Color.White,
//                        focusedContainerColor = Color.White,
//                        unfocusedIndicatorColor = Color.Transparent,
//                        focusedIndicatorColor = Color.Transparent
//                    ),
//                    value = inputText,
//                    onValueChange = { inputText = it },
//                    modifier = Modifier.weight(1f),
//                    placeholder = { Text("Tanya TripMate...") }
//                )
//                IconButton(
//                    onClick = {
//                        if (inputText.isNotBlank()) {
//                            n8nViewModel.textProcessInput(inputText)
//                            inputText = ""
//                        }
//                    }
//                ) {
//                    Icon(
//                        modifier = Modifier.padding(start = 8.dp),
//                        imageVector = Icons.AutoMirrored.Filled.Send,
//                        contentDescription = "Send Message",
//                        tint = MaterialTheme.colorScheme.primary
//                    )
//                }
//            }
//        }
//    }
//}