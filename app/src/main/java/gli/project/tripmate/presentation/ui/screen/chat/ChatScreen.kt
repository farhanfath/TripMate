package gli.project.tripmate.presentation.ui.screen.chat

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import gli.project.tripmate.presentation.ui.component.CustomTopBar
import gli.project.tripmate.presentation.ui.screen.chat.component.ChatBubble
import gli.project.tripmate.presentation.ui.screen.chat.component.PulsatingDots
import gli.project.tripmate.presentation.util.extensions.customResponseHandler
import gli.project.tripmate.presentation.viewmodel.ChatViewModel
import gli.project.tripmate.presentation.viewmodel.Message

@Composable
fun ChatScreen(
    chatViewModel: ChatViewModel
) {
    val chatState by chatViewModel.chatState.collectAsState()
    var inputText by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            CustomTopBar()
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
                    onError = {

                    }
                )
            }

            // Input chat di bagian bawah
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextField(
                    value = inputText,
                    onValueChange = { inputText = it },
                    modifier = Modifier.weight(1f),
                    placeholder = { Text("Ketik pesan...") }
                )
                Button(
                    onClick = {
                        if (inputText.isNotBlank()) {
                            chatViewModel.sendMessage(inputText)
                            inputText = ""
                        }
                    },
                    modifier = Modifier.padding(start = 8.dp)
                ) {
                    Text("Send")
                }
            }
        }
    }
}
