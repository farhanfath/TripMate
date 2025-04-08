package gli.project.tripmate.presentation.ui.screen.main.home.profile

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import compose.icons.LineAwesomeIcons
import compose.icons.TablerIcons
import compose.icons.lineawesomeicons.LockSolid
import compose.icons.tablericons.Logout
import gli.project.tripmate.presentation.ui.component.common.CustomImageLoader
import gli.project.tripmate.presentation.ui.component.common.CustomShimmer
import gli.project.tripmate.presentation.ui.screen.main.home.profile.component.ProfileSectionItem
import gli.project.tripmate.presentation.util.touristIcon
import gli.project.tripmate.presentation.viewmodel.auth.UserViewModel

@Composable
fun ProfileScreen(
    onUserLogout: () -> Unit = {}
) {
    val userViewModel : UserViewModel = hiltViewModel()

    var showDialog by remember { mutableStateOf(false) }

    val userState by userViewModel.authState.collectAsState()

    LazyColumn {
        item {
            Text(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
                text = "Profile",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            )
        }
        // profile section
        item {
            Column(
                modifier = Modifier
                    .padding(top = 50.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                CustomImageLoader(
                    url = touristIcon,
                    desc = "Profile Icon",
                    modifier = Modifier
                        .padding(bottom = 10.dp)
                        .border(
                            width = 1.dp,
                            shape = CircleShape,
                            color = MaterialTheme.colorScheme.outlineVariant
                        )
                        .clip(CircleShape)
                        .size(80.dp)
                )
                if (userState.onProcess) {
                    CustomShimmer(
                        modifier = Modifier.width(100.dp).height(30.dp)
                    )
                    CustomShimmer(
                        modifier = Modifier.width(60.dp).height(12.dp)
                    )
                } else {
                    Text(
                        text = "${userState.currentUser?.userName}",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "${userState.currentUser?.email}",
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            }
        }

        // account setting section
        item {
            Column(
                Modifier
                    .padding(top = 20.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = "Pengaturan Akun",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(horizontal = 24.dp)
                )
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 12.dp),
                    border = BorderStroke(
                        color = MaterialTheme.colorScheme.outlineVariant,
                        width = 1.dp
                    )
                ){
                    ProfileSectionItem(
                        icon = LineAwesomeIcons.LockSolid,
                        title = "Pengaturan Kata Sandi",
                        arrowVisible = true,
                        onItemClick = {
//                            onChangePassClick()
                        }
                    )

                    HorizontalDivider()

                    ProfileSectionItem(
                        icon = LineAwesomeIcons.LockSolid,
                        title = "Pengaturan Kata Sandi",
                        arrowVisible = true,
                        onItemClick = {
//                            onChangePassClick()
                        }
                    )
                }
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 12.dp),
                    border = BorderStroke(
                        color = MaterialTheme.colorScheme.outlineVariant,
                        width = 1.dp
                    )
                ){
                    ProfileSectionItem(
                        icon = LineAwesomeIcons.LockSolid,
                        title = "Pengaturan Kata Sandi",
                        arrowVisible = true,
                        onItemClick = {
//                            onChangePassClick()
                        }
                    )
                }
            }
        }
        // footer and logout
        item {
            Column(
                Modifier
                    .padding(top = 4.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = "v.1.0.0",
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.outline,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                )
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 12.dp),
                    border = BorderStroke(
                        color = MaterialTheme.colorScheme.outlineVariant,
                        width = 1.dp
                    )
                ) {
                    ProfileSectionItem(
                        icon = TablerIcons.Logout,
                        title = "Logout",
                        arrowVisible = false,
                        containerColor = MaterialTheme.colorScheme.errorContainer,
                        contentColor = MaterialTheme.colorScheme.onErrorContainer,
                        onItemClick = {
                            showDialog = true
                        }
                    )
                }

            }
        }
    }

    if (showDialog) {
        AlertDialog(
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f),
                    shape = RoundedCornerShape(16.dp)
                ),
            containerColor = MaterialTheme.colorScheme.surface,
            tonalElevation = 6.dp,
            shape = RoundedCornerShape(16.dp),
            onDismissRequest = { showDialog = false },
            title = {
                Text(
                    text = "Keluar",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Apakah Anda yakin ingin keluar dari akun ini?",
                        style = MaterialTheme.typography.titleSmall
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDialog = false
                        userViewModel.userLogout()
                        onUserLogout()
                    }
                ) {
                    Text(
                        text = "Keluar",
                        color = MaterialTheme.colorScheme.error
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showDialog = false }
                ) {
                    Text(
                        text = "Batal",
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        )
    }
}