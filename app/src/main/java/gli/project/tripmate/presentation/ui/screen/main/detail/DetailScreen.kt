package gli.project.tripmate.presentation.ui.screen.main.detail

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.RateReview
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import gli.project.tripmate.domain.util.constants.DataConstants
import gli.project.tripmate.presentation.state.main.RatingsState
import gli.project.tripmate.presentation.ui.component.common.CustomShimmer
import gli.project.tripmate.presentation.ui.component.common.error.CustomNoConnectionPlaceholder
import gli.project.tripmate.presentation.ui.screen.main.detail.component.BackDropImage
import gli.project.tripmate.presentation.ui.screen.main.detail.component.DetailActionButton
import gli.project.tripmate.presentation.ui.screen.main.detail.component.DetailInformation
import gli.project.tripmate.presentation.ui.screen.main.detail.component.tab.about.AboutTab
import gli.project.tripmate.presentation.ui.screen.main.detail.component.tab.gallery.GalleryTab
import gli.project.tripmate.presentation.ui.screen.main.detail.component.tab.review.ReviewTab
import gli.project.tripmate.presentation.ui.screen.main.detail.component.tab.review.component.RatingDialog
import gli.project.tripmate.presentation.util.ErrorMessageHelper
import gli.project.tripmate.presentation.util.extensions.HandlerResponseCompose
import gli.project.tripmate.presentation.viewmodel.main.FavoriteViewModel
import gli.project.tripmate.presentation.viewmodel.main.PlacesViewModel
import gli.project.tripmate.presentation.viewmodel.main.RatingViewModel
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DetailScreen(
    placeId : String,
    placeName: String,
    onBackClick: () -> Unit,
) {
    val viewModel : PlacesViewModel = hiltViewModel()
    val favoriteViewModel: FavoriteViewModel = hiltViewModel()
    val placeDetailState by viewModel.placesState.collectAsState()
    val userRange = viewModel.placesState.map { it.placeRange }.collectAsState(0.0).value

    // rating handler
    val ratingViewModel = hiltViewModel<RatingViewModel>()
    val ratingsState by ratingViewModel.ratingsState.collectAsState()
    val averageRating by ratingViewModel.averageRating.collectAsState()
    val ratingDistribution by ratingViewModel.ratingDistribution.collectAsState()
    val ratings = when (ratingsState) {
        is RatingsState.Success ->
            (ratingsState as RatingsState.Success).ratings
        else -> emptyList()
    }

    LaunchedEffect(Unit) {
        viewModel.getDetailPlaces(placeId)
        viewModel.getPexelDetailImage(placeName)
        favoriteViewModel.checkFavoriteStatus(placeId)
    }

    // load ratings data
    LaunchedEffect(placeId) {
        ratingViewModel.loadRatings(placeId)
    }

    /**
     * image and scroll behavior
     */
    val maxImageHeight = 400.dp
    val minImageHeight = 200.dp
    var currentImageHeight by remember { mutableStateOf(maxImageHeight) }

    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                // Calculate the change in image height based on scroll delta
                val delta = available.y
                val newImageHeight = currentImageHeight + delta.dp
                val previousImageHeight = currentImageHeight

                // Constrain the image height within the allowed bounds
                currentImageHeight = newImageHeight.coerceIn(minImageHeight, maxImageHeight)
                val consumed = currentImageHeight - previousImageHeight

                // Return the consumed scroll amount
                return if (currentImageHeight > minImageHeight || delta < 0) {
                    Offset(0f, consumed.value)
                } else {
                    Offset.Zero
                }
            }
        }
    }

    val tabs = DataConstants.tabName
    val scope = rememberCoroutineScope()

    // favorite
    val snackBarHostState = remember { SnackbarHostState() }
    val isFavorite by favoriteViewModel.isFavorite.collectAsState()

    var selectedTabIndex by remember { mutableIntStateOf(0) }

    // rating dialog
    var showRatingDialog by remember { mutableStateOf(false) }

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackBarHostState)
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showRatingDialog = true }
            ) {
                Icon(
                    imageVector = Icons.Default.RateReview,
                    contentDescription = ""
                )
            }
        }
    ) { innerPadding ->
        HandlerResponseCompose(
            response = placeDetailState.detailPlace,
            onLoading = {
                CustomShimmer(
                    modifier = Modifier.fillMaxSize()
                )
            },
            onSuccess = { detailData ->
                // get range between user and place
                viewModel.getPlaceRangeLocation(latPlace = detailData.lat, lonPlace = detailData.lon)

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .nestedScroll(nestedScrollConnection)
                ) {
                    BackDropImage(
                        currentImageHeight = currentImageHeight,
                        imagePexelState = placeDetailState.detailImage
                    )

                    DetailActionButton(
                        modifier = Modifier.padding(innerPadding),
                        onBackClick = onBackClick,
                        onFavoriteClick = {
                            // favorite action
                            favoriteViewModel.toggleFavorite(detailData)

                            // snack bar show action
                            scope.launch {

                                val message = if (isFavorite) {
                                    "Berhasil Dihapus"
                                } else {
                                    "Berhasil Ditambahkan"
                                }

                                val result = snackBarHostState.showSnackbar(
                                    message = message,
                                    actionLabel = "undo",
                                    duration = SnackbarDuration.Short
                                )

                                when (result) {
                                    SnackbarResult.ActionPerformed -> {
                                        favoriteViewModel.toggleFavorite(detailData)
                                    }
                                    SnackbarResult.Dismissed -> {}
                                }
                            }
                        },
                        favIcon = if (isFavorite) Icons.Filled.Favorite else Icons.Default.FavoriteBorder
                    )

                    /**
                     * detail Content
                     */
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .offset {
                                IntOffset(0, currentImageHeight.roundToPx())
                            },
                        contentPadding = PaddingValues(bottom = currentImageHeight)
                    ) {
                        item {
                            DetailInformation(
                                data = detailData,
                                placeName = placeName,
                                totalReview = ratings.size,
                                ratingStats = averageRating
                            )
                        }

                        stickyHeader {
                            TabRow(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                containerColor = MaterialTheme.colorScheme.background,
                                selectedTabIndex = selectedTabIndex,
                                contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                indicator = { tabPositions ->
                                    Box(
                                        Modifier
                                            .tabIndicatorOffset(tabPositions[selectedTabIndex])
                                            .height(5.dp)
                                            .padding(horizontal = 16.dp)
                                            .width(tabPositions[selectedTabIndex].width)
                                            .background(
                                                color = MaterialTheme.colorScheme.primary,
                                                shape = RoundedCornerShape(10.dp)
                                            )
                                    )
                                }
                            ) {
                                tabs.forEachIndexed { index, title ->
                                    Tab(
                                        text = { Text(title) },
                                        selected = selectedTabIndex == index,
                                        onClick = {
                                            scope.launch {
                                                selectedTabIndex = index
                                            }
                                        }
                                    )
                                }
                            }
                        }

                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                            ) {
                                AnimatedContent(
                                    targetState = selectedTabIndex,
                                    transitionSpec = {
                                        if (targetState > initialState) {
                                            slideInHorizontally { width -> width } togetherWith
                                                    slideOutHorizontally { width -> -width }
                                        } else {
                                            slideInHorizontally { width -> -width } togetherWith
                                                    slideOutHorizontally { width -> width }
                                        }
                                    }
                                ) { targetTabIndex ->
                                    when(targetTabIndex) {
                                        0 -> {
                                            AboutTab(
                                                detailData = detailData,
                                                userRange = userRange
                                            )
                                        }
                                        1 -> GalleryTab(
                                            viewModel = viewModel,
                                            detailPlaceName = detailData.name
                                        )
                                        2 -> ReviewTab(
                                            ratingsState = ratingsState,
                                            averageRating = averageRating,
                                            ratingDistribution = ratingDistribution,
                                            totalReviews = ratings
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            },
            onError = { errorStatus ->
                CustomNoConnectionPlaceholder(
                    onRetry = { viewModel.getDetailPlaces(placeId) },
                    title = "Error Acquired",
                    desc = ErrorMessageHelper.getUiErrorMessage(errorStatus, LocalContext.current)
                )
            }
        )

        RatingDialog(
            showDialog = showRatingDialog,
            onDismiss = { showRatingDialog = false },
            onRatingSubmitted = { rating, description ->
                ratingViewModel.updateNewRatingValue(rating)
                ratingViewModel.updateNewRatingDescription(description)
                ratingViewModel.addRating(placeId)
                showRatingDialog = false
            }
        )
    }
}

@Preview
@Composable
fun DetailScreenPreview() {
    DetailScreen(
        placeId = "",
        placeName = "",
        onBackClick = {}
    )
}