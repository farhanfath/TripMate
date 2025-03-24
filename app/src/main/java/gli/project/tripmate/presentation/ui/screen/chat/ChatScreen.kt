package gli.project.tripmate.presentation.ui.screen.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import gli.project.tripmate.presentation.ui.component.CustomTopBar
import gli.project.tripmate.presentation.ui.component.CustomTopBarWithNavigation
import gli.project.tripmate.presentation.ui.screen.chat.component.ChatBubble
import gli.project.tripmate.presentation.ui.screen.chat.component.PulsatingDots
import gli.project.tripmate.presentation.util.extensions.customResponseHandler
import gli.project.tripmate.presentation.viewmodel.ChatViewModel

@Composable
fun ChatScreen(
    chatViewModel: ChatViewModel,
    onBackClick: () -> Unit
) {
    val chatState by chatViewModel.chatState.collectAsState()
    var inputText by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            CustomTopBarWithNavigation(
                title = "Tanya TripMate",
                onBackClick = onBackClick
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.fillMaxSize()
        ) {

            LazyColumn(
                modifier = Modifier
                    .padding(innerPadding)
                    .weight(1f)
            ) {
                items(chatState.message) { msg ->
                    ChatBubble(msg)
                }

                // waiting for response from gemini
                customResponseHandler(
                    response = chatState.chat,
                    onLoading = {
                        item {
                            PulsatingDots()
                        }
                    },
                    onError = {}
                )
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
                            chatViewModel.sendMessage(inputText)
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

            }
        }
    }
}
