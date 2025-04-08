package gli.project.tripmate.presentation.ui.screen.auth.welcome

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import gli.project.tripmate.R
import gli.project.tripmate.presentation.ui.component.common.CustomImageLoader
import gli.project.tripmate.presentation.ui.component.common.CustomTopBar
import gli.project.tripmate.presentation.util.iconUrl
import gli.project.tripmate.presentation.util.tripImage

@Composable
fun WelcomeScreen(
    onNavigateToLogin: () -> Unit,
) {
    Column(
        modifier = Modifier
            .background(color = Color.White)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        CustomTopBar()
        Row(
            modifier = Modifier.padding(top = 50.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            CustomImageLoader(
                url = iconUrl,
                desc = "icon Logo",
                modifier = Modifier.size(40.dp)
            )
            Spacer(modifier = Modifier.size(10.dp))
            Text(
                text = stringResource(R.string.app_name),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.ExtraBold
            )
        }
        CustomImageLoader(
            url = tripImage,
            desc = "",
            modifier = Modifier
                .padding(top = 20.dp)
                .size(340.dp)
        )
        Text(
            text = "Temukan Perjalanan Terbaikmu!",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.secondary,
            fontSize = 20.sp,
            fontWeight = FontWeight.ExtraBold
        )
        Spacer(modifier = Modifier.size(10.dp))
        Text(
            text = "Jelajahi destinasi impian dan dapatkan rekomendasi perjalanan yang pas hanya untukmu.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.outline,
            fontSize = 12.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 40.dp)
        )

        Spacer(modifier = Modifier.size(50.dp))

        Button(
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            ),
            elevation = ButtonDefaults.buttonElevation(
                defaultElevation = 10.dp,
                pressedElevation = 15.dp,
                disabledElevation = 0.dp
            ),
            shape = RoundedCornerShape(10.dp),
            onClick = onNavigateToLogin
        ) {
            Text(
                text = "Mulai Sekarang",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.secondary
            )
        }
    }
}

@Preview
@Composable
fun WelcomePreview() {
    WelcomeScreen(
        onNavigateToLogin = {}
    )
}