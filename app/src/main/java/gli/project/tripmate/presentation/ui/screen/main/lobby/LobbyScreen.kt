package gli.project.tripmate.presentation.ui.screen.main.lobby

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
import gli.project.tripmate.presentation.ui.screen.main.lobby.component.Feature
import gli.project.tripmate.presentation.ui.screen.main.lobby.component.Greeting
import gli.project.tripmate.presentation.ui.screen.main.lobby.component.HistoryView
import gli.project.tripmate.presentation.ui.screen.main.lobby.component.Nearby
import gli.project.tripmate.presentation.ui.screen.main.lobby.component.SearchBar
import gli.project.tripmate.presentation.ui.theme.size_20
import gli.project.tripmate.presentation.ui.theme.size_200
import gli.project.tripmate.presentation.ui.theme.size_30

@Composable
fun LobbyScreen(
    onDetailClick: () -> Unit
) {
    Scaffold(
        topBar = {
            Box(
                modifier = Modifier
                    .background(color = MaterialTheme.colorScheme.primary)
                    .height(size_30)
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
                            .clip(RoundedCornerShape(bottomEnd = size_30, bottomStart = size_30))
                            .background(color = MaterialTheme.colorScheme.primary)
                            .height(size_200)
                            .fillMaxWidth(),
                    )
                    Column {
                        Greeting()
                        SearchBar()
                    }
                }
            }
            item {
                Spacer(modifier = Modifier.size(size_20))
                Feature()
            }
            item {
                HistoryView(
                    onDetailClick = onDetailClick
                )
            }
            item {
                Nearby(
                    onDetailClick = onDetailClick
                )
            }
        }
    }
}

@Preview
@Composable
fun LobbyScreenPreview() {
    LobbyScreen(
        onDetailClick = {}
    )
}