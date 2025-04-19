package gli.project.tripmate.presentation.ui.screen.main.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import gli.project.tripmate.presentation.ui.component.common.CustomTopBarWithNavigation
import gli.project.tripmate.presentation.ui.screen.main.chat.component.MessageBubble
import gli.project.tripmate.presentation.ui.screen.main.chat.component.VoiceRecorderButton
import gli.project.tripmate.presentation.viewmodel.main.N8nViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalPermissionsApi::class)
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

    val recordAudioPermission = rememberPermissionState(android.Manifest.permission.RECORD_AUDIO)

    // Request permission if not granted
    LaunchedEffect(Unit) {
        if (!recordAudioPermission.status.isGranted) {
            recordAudioPermission.launchPermissionRequest()
        }
    }

    // Show error in Snackbar
    LaunchedEffect(error) {
        error?.let {
            coroutineScope.launch {
                snackbarHostState.showSnackbar(it)
                println(it)
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
            CustomTopBarWithNavigation(
                title = "Trip Assistant",
                onBackClick = onBackClick
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Status Indicator
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                when {
                    isListening -> Text("Mendengarkan...")
                    isLoading -> {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .width(16.dp)
                                .height(16.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Memproses...")
                    }
                    isSpeaking -> Text("Berbicara...")
                    conversation.isEmpty() -> Text("Tekan tombol untuk mulai berbicara")
                    else -> Text("Siap mendengarkan")
                }
            }

            // Conversation Messages
            LazyColumn(
                state = listState,
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(conversation) { item ->
                    MessageBubble(
                        item = item,
                        onTravelSpotClick = { travelSpot ->
                            // Handle click on travel spot item
                            coroutineScope.launch {
                                snackbarHostState.showSnackbar("Selected: ${travelSpot.name}")
                                // Tambahkan tindakan lain jika diperlukan, seperti membuka detail, dsb.
                            }
                        }
                    )
                }
            }
            Row(
                modifier = Modifier
                    .background(color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.1f))
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = Color.White,
                        focusedContainerColor = Color.White,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent
                    ),
                    value = inputText,
                    onValueChange = { inputText = it },
                    modifier = Modifier.weight(1f),
                    placeholder = { Text("Tanya TripMate...") }
                )
                IconButton(
                    onClick = {
                        if (inputText.isNotBlank()) {
                            n8nViewModel.processUserInput(inputText)
                            inputText = ""
                        }
                    }
                ) {
                    Icon(
                        modifier = Modifier.padding(start = 8.dp),
                        imageVector = Icons.AutoMirrored.Filled.Send,
                        contentDescription = "Send Message",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
                if (recordAudioPermission.status.isGranted) {
                    VoiceRecorderButton(
                        isListening = isListening,
                        isLoading = isLoading,
                        onClick = {
                            if (isListening) {
                                n8nViewModel.stopListening()
                            } else {
                                n8nViewModel.startListening()
                            }
                        }
                    )
                }
            }
        }
    }
}