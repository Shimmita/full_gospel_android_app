package com.shimmita.full_gospel.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shimmita.full_gospel.R
import com.shimmita.full_gospel.model.PostResponse
import com.shimmita.full_gospel.viewmodel.ViewModel
import kotlinx.coroutines.launch

/* ─── Palette ──────────────────────────────────────────────────────────── */
private val NavyDeep    = Color(0xFF0D1B2A)
private val NavyMid     = Color(0xFF1B2E45)
private val GoldLight   = Color(0xFFFFD77E)
private val GoldDeep    = Color(0xFFB8860B)
private val GoldAccent  = Color(0xFFF5C842)
private val IvoryWhite  = Color(0xFFF8F4EC)
private val SilverGray  = Color(0xFFB0BEC5)
private val CardBg      = Color(0xFF162031)
private val DividerGold = Color(0x44F5C842)

/* ─── StarterScreen ─────────────────────────────────────────────────────── */

@Composable
fun StarterScreen(
    viewModel: ViewModel,
    triggerShowLogin: () -> Unit,
    triggerShowContent: (HomeCarouselItem) -> Unit
) {
    val dailyPrayers    by viewModel.dailyPrayers.collectAsState()
    val weeklyPrayers   by viewModel.weeklyPrayers.collectAsState()
    val events          by viewModel.events.collectAsState()
    val testimonials    by viewModel.testimonials.collectAsState()
    val natureTalents   by viewModel.natureTalents.collectAsState()
    val announcements   by viewModel.announcements.collectAsState()
    val sundayDiaries   by viewModel.sundayDiaries.collectAsState()
    val authReady       by viewModel.authReady.collectAsState()
    val isLoading       by viewModel.isLoading.collectAsState()

    var refreshing      by remember { mutableStateOf(false) }
    val coroutineScope  = rememberCoroutineScope()

    LaunchedEffect(authReady) {
        if (authReady) viewModel.loadStarterScreenData()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(NavyDeep, Color(0xFF101E30), NavyDeep)
                )
            )
            .pointerInput(Unit) {
                detectVerticalDragGestures { _, dragAmount ->
                    if (dragAmount > 20f && !refreshing) {
                        refreshing = true
                        coroutineScope.launch {
                            viewModel.loadStarterScreenData()
                            refreshing = false
                        }
                    }
                }
            }
    ) {
        /* subtle radial glow top-center */
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
                .align(Alignment.TopCenter)
                .background(
                    Brush.radialGradient(
                        colors = listOf(Color(0x22F5C842), Color.Transparent),
                        center = Offset(Float.POSITIVE_INFINITY / 2, 0f),
                        radius = 600f
                    )
                )
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(28.dp)
        ) {
            /* ── App header banner ── */
            item { AppHeaderBanner() }

            if (dailyPrayers.isNotEmpty()) item {
                SectionTitle("✦  Daily Prayers")
                HomeHorizontalPager(
                    triggerShowContent = triggerShowContent,
                    items = dailyPrayers.map { it.toHomeCarouselItem(triggerShowLogin) }
                )
            }
            if (weeklyPrayers.isNotEmpty()) item {
                SectionTitle("✦  Weekly Prayers")
                HomeHorizontalPager(
                    triggerShowContent = triggerShowContent,
                    items = weeklyPrayers.map { it.toHomeCarouselItem(triggerShowLogin) }
                )
            }
            if (events.isNotEmpty()) item {
                SectionTitle("✦  Church Events")
                HomeHorizontalPager(
                    triggerShowContent = triggerShowContent,
                    items = events.map { it.toHomeCarouselItem(triggerShowLogin) }
                )
            }
            if (sundayDiaries.isNotEmpty()) item {
                SectionTitle("✦  Sunday Diaries")
                HomeHorizontalPager(
                    triggerShowContent = triggerShowContent,
                    items = sundayDiaries.map { it.toHomeCarouselItem(triggerShowLogin) }
                )
            }
            if (natureTalents.isNotEmpty()) item {
                SectionTitle("✦  Nature Talents")
                HomeHorizontalPager(
                    triggerShowContent = triggerShowContent,
                    items = natureTalents.map { it.toHomeCarouselItem(triggerShowLogin) }
                )
            }
            if (announcements.isNotEmpty()) item {
                SectionTitle("✦  Announcements")
                HomeHorizontalPager(
                    triggerShowContent = triggerShowContent,
                    items = announcements.map { it.toHomeCarouselItem(triggerShowLogin) }
                )
            }
            if (testimonials.isNotEmpty()) item {
                SectionTitle("✦  Testimonials")
                HomeHorizontalPager(
                    triggerShowContent = triggerShowContent,
                    items = testimonials.map { it.toHomeCarouselItem(triggerShowLogin) }
                )
            }

            item { Spacer(Modifier.height(24.dp)) }
        }

        if (isLoading || refreshing) FullScreenLoader()
    }
}

/* ─── App Header Banner ─────────────────────────────────────────────────── */

@Composable
fun AppHeaderBanner() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(
                Brush.linearGradient(listOf(NavyMid, Color(0xFF1E3A5F)))
            )
            .drawBehind {
                // subtle golden bottom border line
                drawLine(
                    color = GoldAccent.copy(alpha = 0.6f),
                    start = Offset(0f, size.height),
                    end = Offset(size.width, size.height),
                    strokeWidth = 2f
                )
            }
            .padding(horizontal = 24.dp, vertical = 20.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "✝",
                fontSize = 28.sp,
                color = GoldAccent
            )
            Spacer(Modifier.height(6.dp))
            Text(
                text = "FULL GOSPEL CHURCH",
                fontSize = 18.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 3.sp,
                color = IvoryWhite
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = "\"The Spirit of the Lord is upon me\"  — Luke 4:18",
                fontSize = 11.sp,
                fontStyle = FontStyle.Italic,
                color = GoldLight.copy(alpha = 0.85f),
                letterSpacing = 0.5.sp
            )
        }
    }
}

/* ─── Full Screen Loader ────────────────────────────────────────────────── */

@Composable
fun FullScreenLoader() {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulse by infiniteTransition.animateFloat(
        initialValue = 0.6f, targetValue = 1f,
        animationSpec = infiniteRepeatable(
            tween(900, easing = FastOutSlowInEasing),
            RepeatMode.Reverse
        ),
        label = "pulse"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(NavyDeep.copy(alpha = 0.82f)),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "✝",
                fontSize = 40.sp,
                color = GoldAccent.copy(alpha = pulse),
                modifier = Modifier.alpha(pulse)
            )
            Spacer(Modifier.height(14.dp))
            CircularProgressIndicator(
                color = GoldAccent,
                strokeWidth = 3.dp,
                modifier = Modifier.size(36.dp)
            )
            Spacer(Modifier.height(12.dp))
            Text(
                text = "Loading...",
                color = SilverGray,
                fontSize = 13.sp,
                letterSpacing = 1.sp
            )
        }
    }
}

/* ─── Section Title ─────────────────────────────────────────────────────── */

@Composable
fun SectionTitle(title: String) {
    Row(
        modifier = Modifier
            .padding(start = 20.dp, bottom = 10.dp, end = 20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 2.sp,
            color = GoldAccent
        )
        Spacer(Modifier.width(10.dp))
        HorizontalDivider(
            modifier = Modifier.weight(1f),
            thickness = 1.dp,
            color = DividerGold
        )
    }
}

/* ─── Horizontal Pager ──────────────────────────────────────────────────── */

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeHorizontalPager(
    triggerShowContent: (HomeCarouselItem) -> Unit,
    items: List<HomeCarouselItem>
) {
    if (items.isEmpty()) return

    val pagerState = rememberPagerState(initialPage = 0) { items.size }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        HorizontalPager(
            state = pagerState,
            pageSize = PageSize.Fill,
            contentPadding = PaddingValues(horizontal = 16.dp),
            pageSpacing = 12.dp,
            modifier = Modifier.fillMaxWidth()
        ) { page ->
            CardHomeComposable(item = items[page], triggerShowContent = triggerShowContent)
        }

        Spacer(Modifier.height(10.dp))

        /* gold dot indicators */
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            repeat(items.size) { index ->
                val isActive = pagerState.currentPage == index
                val size by animateDpAsState(
                    if (isActive) 10.dp else 6.dp, label = "dot"
                )
                Box(
                    modifier = Modifier
                        .padding(horizontal = 3.dp)
                        .size(size)
                        .clip(CircleShape)
                        .background(
                            if (isActive) GoldAccent
                            else GoldDeep.copy(alpha = 0.4f)
                        )
                )
            }
        }
    }
}

/* ─── Card ──────────────────────────────────────────────────────────────── */

@Composable
fun CardHomeComposable(
    item: HomeCarouselItem,
    triggerShowContent: (HomeCarouselItem) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CardBg),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        /* gold top accent bar */
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(3.dp)
                .background(
                    Brush.horizontalGradient(listOf(Color.Transparent, GoldAccent, Color.Transparent))
                )
        )

        Column(modifier = Modifier.padding(16.dp)) {
            /* verse / title pill */
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(GoldAccent.copy(alpha = 0.12f))
                    .padding(horizontal = 12.dp, vertical = 4.dp)
            ) {
                Text(
                    text = item.topTitle,
                    fontSize = 11.sp,
                    fontStyle = FontStyle.Italic,
                    color = GoldLight,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = item.author,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = IvoryWhite
                    )
                    if (item.position.isNotBlank()) {
                        Text(
                            text = item.position,
                            fontSize = 11.sp,
                            color = GoldLight.copy(alpha = 0.7f),
                            letterSpacing = 0.5.sp
                        )
                    }

                    Spacer(Modifier.height(8.dp))

                    Text(
                        text = item.mainText.truncateWords(30),
                        fontSize = 13.sp,
                        lineHeight = 20.sp,
                        color = SilverGray
                    )

                    Spacer(Modifier.height(14.dp))

                    /* gold outlined button */
                    OutlinedButton(
                        onClick = { triggerShowContent(item) },
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = GoldAccent
                        ),
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            Brush.horizontalGradient(listOf(GoldDeep, GoldAccent))
                        )
                    ) {
                        Text(
                            text = item.buttonText,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            letterSpacing = 1.sp
                        )
                    }
                }

                Spacer(Modifier.width(12.dp))

                /* image with golden frame */
                Box(
                    modifier = Modifier
                        .size(130.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(GoldDeep.copy(alpha = 0.2f))
                ) {
                    Image(
                        painter = painterResource(item.imageRes),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(12.dp)),
                        contentScale = ContentScale.Crop
                    )
                    /* subtle gold overlay on image edge */
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(12.dp))
                            .background(
                                Brush.verticalGradient(
                                    listOf(Color.Transparent, NavyDeep.copy(alpha = 0.4f))
                                )
                            )
                    )
                }
            }
        }

        /* bottom cross accent */
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 10.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "· · ✦ · ·",
                fontSize = 10.sp,
                color = GoldDeep.copy(alpha = 0.5f),
                letterSpacing = 2.sp
            )
        }
    }
}

/* ─── Extensions + Models ──────────────────────────────────────────────── */

fun String.truncateWords(maxWords: Int): String =
    split(" ").let {
        if (it.size <= maxWords) this
        else it.take(maxWords).joinToString(" ") + "…"
    }

data class HomeCarouselItem(
    val topTitle: String,
    val author: String,
    val position: String,
    val mainText: String,
    val imageRes: Int,
    val bibleVerse: String,
    val phoneNumber: String,
    val buttonText: String,
    val onClick: () -> Unit
)

fun PostResponse.toHomeCarouselItem(onClick: () -> Unit): HomeCarouselItem =
    HomeCarouselItem(
        topTitle    = verse ?: "Message",
        author      = author ?: "Unknown",
        position    = role ?: "",
        mainText    = details ?: "",
        bibleVerse  = verse,
        phoneNumber = phone,
        imageRes    = availableDrawables.random(),
        buttonText  = "Read More",
        onClick     = onClick
    )

private val availableDrawables = listOf(
    R.drawable.cross, R.drawable.angel_sword, R.drawable.church,
    R.drawable.church1, R.drawable.bible, R.drawable.bible1,
    R.drawable.grow, R.drawable.throne, R.drawable.sword,
    R.drawable.minrat, R.drawable.michael, R.drawable.lightening,
    R.drawable.holyspirit, R.drawable.holyspirit11, R.drawable.holyspirit2,
    R.drawable.crossess, R.drawable.cross_day, R.drawable.cross22,
    R.drawable.cross2, R.drawable.cross11, R.drawable.cross1,
    R.drawable.bible_n_cross, R.drawable.holyfire, R.drawable.trumpet1,
    R.drawable.cross222, R.drawable.holy_fire11, R.drawable.eagle,
    R.drawable.eagle1, R.drawable.king_jesu, R.drawable.king_jesus,
    R.drawable.king_christ, R.drawable.jesus_god, R.drawable.jesus_lord,
    R.drawable.jesus_love, R.drawable.jesus_lord11, R.drawable.lamb,
    R.drawable.lamb1, R.drawable.lamb11, R.drawable.lamb2, R.drawable.lamb22,
    R.drawable.lion, R.drawable.lion1, R.drawable.lion11, R.drawable.lion22,
)