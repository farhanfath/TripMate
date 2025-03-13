package gli.project.tripmate.presentation.ui.screen.lobby

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import gli.project.tripmate.presentation.ui.screen.lobby.component.Feature
import gli.project.tripmate.presentation.ui.screen.lobby.component.Greeting
import gli.project.tripmate.presentation.ui.screen.lobby.component.HistoryView
import gli.project.tripmate.presentation.ui.screen.lobby.component.Nearby
import gli.project.tripmate.presentation.ui.screen.lobby.component.SearchBar

@Composable
fun LobbyScreen() {
    Scaffold(
        topBar = {
            Box(
                modifier = Modifier
                    .background(color = MaterialTheme.colorScheme.primary)
                    .height(30.dp)
                    .fillMaxWidth()
            )
        },
        bottomBar = {

        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxWidth()
        ) {
            item {
                Box {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(bottomEnd = 30.dp, bottomStart = 30.dp))
                            .background(color = MaterialTheme.colorScheme.primary)
                            .height(200.dp)
                            .fillMaxWidth(),
                    )
                    Column {
                        Greeting()
                        SearchBar()
                    }
                }
            }
            item {
                Spacer(modifier = Modifier.size(20.dp))
                Feature()
            }
            item {
                HistoryView()
            }
            item {
                Nearby()
            }
        }
    }
}

@Preview
@Composable
fun LobbyScreenPreview() {
    LobbyScreen()
}