package gli.project.tripmate.presentation.ui.screen.auth.welcome

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.EaseOutQuad
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandIn
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import gli.project.tripmate.R
import gli.project.tripmate.presentation.ui.component.common.CustomTopBar
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

data class OnboardingPage(
    val lottieRes: Int,
    val title: String,
    val subtitle: String
)

@Composable
fun WelcomeScreen(
    onNavigateToLogin: () -> Unit
) {
    val pages = listOf(
        OnboardingPage(
            lottieRes = R.raw.ai_assistant,
            title = "AI-Powered Trip Assistant",
            subtitle = "Plan smarter and faster with the help of our intelligent AI assistant that gives you personalized recommendations and travel routes."
        ),
        OnboardingPage(
            lottieRes = R.raw.find_nearby,
            title = "Explore Nearby Destination",
            subtitle = "Enable location access and GPS to discover recommended tourist spots and hidden gems around you in real-time."
        ),
        OnboardingPage(
            lottieRes = R.raw.customer_service,
            title = "Cross-Platform Call Customer Service",
            subtitle = "Easily reach our support team through voice calls, available on both android and iOS platforms anytime you need help."
        ),
        OnboardingPage(
            lottieRes = R.raw.collection_travel,
            title = "Customizable Trip Collection",
            subtitle = "Save and manage your favorite destinations in a personalized collection to plan and revisit your dream trips effortlessly."
        )
    )

    val pagerState = rememberPagerState(pageCount = { pages.size })
    val coroutineScope = rememberCoroutineScope()

    var isContentVisible by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = true) {
        isContentVisible = true
    }

    Scaffold(
        topBar = {
            CustomTopBar()
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            // ViewPager
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize()
            ) { page ->
                OnboardingPage(
                    onboardingPage = pages[page],
                    isVisible = isContentVisible
                )
            }

            // Bottom navigation
            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 32.dp)
            ) {
                // Page indicator
                Row(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    repeat(pages.size) { iteration ->
                        val width by animateDpAsState(
                            targetValue = if (pagerState.currentPage == iteration) 24.dp else 8.dp,
                            animationSpec = spring(
                                dampingRatio = Spring.DampingRatioMediumBouncy,
                                stiffness = Spring.StiffnessLow
                            )
                        )

                        Box(
                            modifier = Modifier
                                .padding(horizontal = 4.dp)
                                .height(8.dp)
                                .width(width)
                                .clip(CircleShape)
                                .background(
                                    if (pagerState.currentPage == iteration)
                                        MaterialTheme.colorScheme.primary
                                    else
                                        MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
                                )
                                .clickable {
                                    coroutineScope.launch {
                                        pagerState.animateScrollToPage(iteration)
                                    }
                                }
                        )
                    }
                }

                // Bottom buttons
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(
                        onClick = onNavigateToLogin
                    ) {
                        Text(
                            text = "Lewati",
                            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f),
                            fontWeight = FontWeight.Medium
                        )
                    }

                    Button(
                        onClick = {
                            coroutineScope.launch {
                                if (pagerState.currentPage < pages.size - 1) {
                                    pagerState.animateScrollToPage(
                                        pagerState.currentPage + 1,
                                        animationSpec = tween(400)
                                    )
                                } else {
                                    onNavigateToLogin()
                                }
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        ),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text(
                            text = if (pagerState.currentPage == pages.size - 1) "Mulai" else "Lanjut",
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowForward,
                            contentDescription = "Next"
                        )
                    }
                }
            }

            // Logo di pojok kiri atas
            Row(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.app_logo),
                    contentDescription = "App Logo",
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "TripMate",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Composable
fun OnboardingPage(
    onboardingPage: OnboardingPage,
    isVisible: Boolean
) {
    val lottieComposition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(onboardingPage.lottieRes)
    )

    val progress by animateLottieCompositionAsState(
        composition = lottieComposition,
        isPlaying = true,
        iterations = Int.MAX_VALUE,
        speed = 1f
    )

    var isLottieVisible by remember { mutableStateOf(false) }
    var isTitleVisible by remember { mutableStateOf(false) }
    var isSubtitleVisible by remember { mutableStateOf(false) }

    LaunchedEffect(isVisible, onboardingPage) {
        // Reset visibility states when page changes or visibility changes
        isLottieVisible = false
        isTitleVisible = false
        isSubtitleVisible = false

        if (isVisible) {
            delay(100)
            isLottieVisible = true
            delay(300)
            isTitleVisible = true
            delay(200)
            isSubtitleVisible = true
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(modifier = Modifier.height(60.dp))

        // Lottie Animation
        AnimatedVisibility(
            visible = isLottieVisible,
            enter = fadeIn(animationSpec = tween(700)) +
                    expandIn(
                        animationSpec = tween(700),
                        expandFrom = Alignment.Center
                    )
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                LottieAnimation(
                    composition = lottieComposition,
                    progress = { progress },
                    modifier = Modifier.size(300.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Title with slide up and fade in animation
        AnimatedVisibility(
            visible = isTitleVisible,
            enter = slideInVertically(
                initialOffsetY = { 40 },
                animationSpec = tween(durationMillis = 800, easing = EaseOutQuad)
            ) + fadeIn(
                animationSpec = tween(durationMillis = 800, easing = EaseOutQuad)
            )
        ) {
            Text(
                text = onboardingPage.title,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Subtitle with slide up and fade in animation
        AnimatedVisibility(
            visible = isSubtitleVisible,
            enter = slideInVertically(
                initialOffsetY = { 40 },
                animationSpec = tween(durationMillis = 900, easing = EaseOutQuad)
            ) + fadeIn(
                animationSpec = tween(durationMillis = 900, easing = EaseOutQuad)
            )
        ) {
            Text(
                text = onboardingPage.subtitle,
                style = MaterialTheme.typography.bodyMedium,
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                lineHeight = 24.sp,
                modifier = Modifier.padding(horizontal = 24.dp)
            )
        }

        Spacer(modifier = Modifier.height(100.dp))
    }
}