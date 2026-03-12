package com.shimmita.full_gospel

import PostContentModalSheet
import User
import ViewModelFactory
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.shimmita.full_gospel.auth.DataStoreManager
import com.shimmita.full_gospel.auth.LoginModalSheet
import com.shimmita.full_gospel.details.DetailSheet
import com.shimmita.full_gospel.details.MemberSheet
import com.shimmita.full_gospel.routing.ScreenRoutes
import com.shimmita.full_gospel.screens.*
import com.shimmita.full_gospel.ui.theme.Full_GospelTheme
import com.shimmita.full_gospel.viewmodel.ViewModel
import kotlinx.coroutines.launch

/* ─── Palette ──────────────────────────────────────────────────────────── */
private val NavyDeep    = Color(0xFF0D1B2A)
private val NavyMid     = Color(0xFF162236)
private val GoldAccent  = Color(0xFFF5C842)
private val GoldDeep    = Color(0xFFB8860B)
private val GoldLight   = Color(0xFFFFD77E)
private val IvoryWhite  = Color(0xFFF8F4EC)
private val SilverGray  = Color(0xFF8FA6BA)

/* ─── MainActivity ──────────────────────────────────────────────────────── */

class MainActivity : ComponentActivity() {
    private lateinit var viewModel: ViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Full_GospelTheme {
                val context          = LocalContext.current
                val dataStoreManager = remember { DataStoreManager(context) }

                viewModel = ViewModelProvider(
                    this, ViewModelFactory(dataStoreManager)
                )[ViewModel::class.java]

                val navController     = rememberNavController()
                val coroutineScope    = rememberCoroutineScope()
                val drawerState       = rememberDrawerState(DrawerValue.Closed)
                var currentItemIndex  by rememberSaveable { mutableIntStateOf(0) }

                val isLoggedIn            by viewModel.isLoggedIn.collectAsState()
                var showLoginSheet        by remember { mutableStateOf(false) }
                var showDetailsSheet      by remember { mutableStateOf(false) }
                var showPostContentSheet  by remember { mutableStateOf(false) }
                var showMembersList       by remember { mutableStateOf(false) }
                var selectedItem          by remember { mutableStateOf<HomeCarouselItem?>(null) }

                val snackbarHostState = remember { SnackbarHostState() }
                val authReady         by viewModel.authReady.collectAsState()
                val allMembers        by viewModel.allUsers.collectAsState()
                val isLoading         by viewModel.isLoading.collectAsState()

                LaunchedEffect(authReady) {
                    if (authReady) viewModel.loadUsersData()
                }

                ModalNavigationDrawer(
                    drawerContent = {
                        DrawerScreen(
                            drawerState  = drawerState,
                            isLoggedIn   = isLoggedIn,
                            handleShowLoginSheet = {
                                coroutineScope.launch {
                                    drawerState.close()
                                    showLoginSheet = true
                                }
                            },
                            viewModel = viewModel
                        )
                    },
                    drawerState = drawerState,
                    scrimColor  = NavyDeep.copy(alpha = 0.75f)
                ) {
                    Scaffold(
                        modifier              = Modifier.fillMaxSize(),
                        snackbarHost          = {
                            SnackbarHost(hostState = snackbarHostState) { data ->
                                SacredSnackbar(data)
                            }
                        },
                        floatingActionButtonPosition = FabPosition.End,
                        floatingActionButton  = {
                            SacredFAB(
                                onClick = {
                                    if (!isLoggedIn) showLoginSheet = true
                                    else showPostContentSheet = true
                                }
                            )
                        },
                        topBar = {
                            SacredTopBar(
                                isLoggedIn            = isLoggedIn,
                                bellIconClicked       = {
                                    if (!isLoggedIn) showLoginSheet = true
                                    else viewModel.loadStarterScreenData()
                                },
                                registerAccountClicked = { showLoginSheet = true },
                                shareApp               = {
                                    val intent = Intent(Intent.ACTION_SEND).apply {
                                        type = "text/plain"
                                        putExtra(Intent.EXTRA_SUBJECT, "FULL GOSPEL MINISTRY App")
                                        putExtra(
                                            Intent.EXTRA_TEXT,
                                            "Download the FULL GOSPEL MINISTRY app: " +
                                                    "https://play.google.com/store/apps/details?id=${context.packageName}"
                                        )
                                    }
                                    context.startActivity(Intent.createChooser(intent, "Share via"))
                                },
                                handleToggleDrawer = {
                                    coroutineScope.launch {
                                        if (drawerState.isOpen) drawerState.close()
                                        else drawerState.open()
                                    }
                                }
                            )
                        },
                        bottomBar = {
                            SacredBottomBar(
                                currentIndex  = currentItemIndex,
                                isLoggedIn    = isLoggedIn,
                                onItemClick   = { index, navItem ->
                                    when {
                                        !isLoggedIn && index != 0 -> showLoginSheet = true
                                        index == 2 -> coroutineScope.launch {
                                            snackbarHostState.showSnackbar(
                                                "✝  Live TV Program Coming Soon!"
                                            )
                                        }
                                        index == 1 -> showMembersList = true
                                        else -> {
                                            currentItemIndex = index
                                            navController.navigate(navItem.routePath)
                                        }
                                    }
                                }
                            )
                        },
                        containerColor = NavyDeep
                    ) { innerPadding ->

                        NavHost(
                            navController    = navController,
                            startDestination = ScreenRoutes.Home,
                            modifier         = Modifier.padding(innerPadding)
                        ) {
                            composable(ScreenRoutes.Home) {
                                StarterScreen(
                                    viewModel         = viewModel,
                                    triggerShowLogin  = {
                                        if (isLoggedIn) {
                                            showLoginSheet       = false
                                            showPostContentSheet = true
                                        } else showLoginSheet = true
                                    },
                                    triggerShowContent = { item ->
                                        selectedItem     = item
                                        showDetailsSheet = true
                                    }
                                )
                            }
                            composable(ScreenRoutes.Members)          { AllMembersView() }
                            composable(ScreenRoutes.EventMissionView) { EventsMissionView() }
                            composable(ScreenRoutes.LiveTV)           { LiveStreamView() }
                            composable(ScreenRoutes.DetailsView)      { DetailScreen() }
                        }

                        /* ── Sheets ── */
                        when {
                            showLoginSheet -> LoginModalSheet(viewModel = viewModel)
                            !isLoggedIn   -> LoginModalSheet(viewModel = viewModel)
                        }

                        if (showPostContentSheet)
                            PostContentModalSheet(
                                onDismiss = { showPostContentSheet = false },
                                viewModel = viewModel
                            )

                        if (showDetailsSheet)
                            DetailSheet(
                                selectedItem = selectedItem,
                                onDismiss    = { showDetailsSheet = false }
                            )

                        if (showMembersList) {
                            if (isLoading) FullScreenLoader()
                            else MemberSheet(allMembers, onDismiss = { showMembersList = false })
                        }
                    }
                }
            }
        }
    }
}

/* ─── Sacred Top App Bar ────────────────────────────────────────────────── */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SacredTopBar(
    isLoggedIn: Boolean,
    bellIconClicked: () -> Unit,
    registerAccountClicked: () -> Unit,
    shareApp: () -> Unit,
    handleToggleDrawer: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Brush.horizontalGradient(
                    listOf(NavyDeep, Color(0xFF152234), NavyMid)
                )
            )
    ) {
        /* subtle gold bottom border */
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .align(Alignment.BottomCenter)
                .background(
                    Brush.horizontalGradient(
                        listOf(Color.Transparent, GoldAccent.copy(alpha = 0.5f), Color.Transparent)
                    )
                )
        )

        TopAppBar(
            title = {
                Column {
                    Text(
                        text          = "FULL GOSPEL MINISTRY",
                        fontSize      = 15.sp,
                        fontWeight    = FontWeight.ExtraBold,
                        letterSpacing = 2.sp,
                        color         = IvoryWhite
                    )
                    Text(
                        text      = "\"The Spirit of the Lord is upon me\"",
                        fontSize  = 9.sp,
                        fontStyle = FontStyle.Italic,
                        color     = GoldLight.copy(alpha = 0.65f)
                    )
                }
            },
            navigationIcon = {
                IconButton(onClick = handleToggleDrawer) {
                    Icon(
                        imageVector        = Icons.Filled.Menu,
                        contentDescription = "Menu",
                        tint               = GoldAccent,
                        modifier           = Modifier.size(24.dp)
                    )
                }
            },
            actions = {
                /* Cross ornament */
                Text(
                    text     = "✝",
                    fontSize = 16.sp,
                    color    = GoldAccent.copy(alpha = 0.7f),
                    modifier = Modifier.padding(end = 4.dp)
                )

                if (!isLoggedIn) {
                    IconButton(onClick = registerAccountClicked) {
                        Icon(
                            painter            = painterResource(R.drawable.baseline_person_add_alt_1_24),
                            contentDescription = "Register",
                            tint               = GoldAccent,
                            modifier           = Modifier.size(22.dp)
                        )
                    }
                } else {
                    IconButton(onClick = bellIconClicked) {
                        Icon(
                            imageVector        = Icons.Rounded.Refresh,
                            contentDescription = "Refresh",
                            tint               = GoldAccent,
                            modifier           = Modifier.size(22.dp)
                        )
                    }
                }

                IconButton(onClick = shareApp) {
                    Icon(
                        imageVector        = Icons.Filled.Share,
                        contentDescription = "Share",
                        tint               = SilverGray,
                        modifier           = Modifier.size(20.dp)
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.Transparent
            )
        )
    }
}

/* ─── Sacred Bottom Navigation Bar ─────────────────────────────────────── */

@Composable
fun SacredBottomBar(
    currentIndex: Int,
    isLoggedIn: Boolean,
    onItemClick: (Int, NavItems) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(NavyMid)
    ) {
        /* gold top border */
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .align(Alignment.TopCenter)
                .background(
                    Brush.horizontalGradient(
                        listOf(Color.Transparent, GoldAccent.copy(alpha = 0.45f), Color.Transparent)
                    )
                )
        )

        NavigationBar(
            containerColor = Color.Transparent,
            tonalElevation = 0.dp
        ) {
            itemsBottomNav.forEachIndexed { index, navItem ->
                val isSelected = currentIndex == index

                NavigationBarItem(
                    selected = isSelected,
                    onClick  = { onItemClick(index, navItem) },
                    icon     = {
                        Box(contentAlignment = Alignment.Center) {
                            /* gold glow indicator pill for active item */
                            if (isSelected) {
                                Box(
                                    modifier = Modifier
                                        .width(40.dp)
                                        .height(3.dp)
                                        .offset(y = (-22).dp)
                                        .clip(RoundedCornerShape(50))
                                        .background(
                                            Brush.horizontalGradient(
                                                listOf(GoldDeep, GoldAccent)
                                            )
                                        )
                                )
                            }
                            Icon(
                                painter            = painterResource(
                                    if (isSelected) navItem.selectedIcon
                                    else navItem.unselectedIcon
                                ),
                                contentDescription = navItem.title,
                                tint               = if (isSelected) GoldAccent else SilverGray.copy(alpha = 0.6f),
                                modifier           = Modifier.size(22.dp)
                            )
                        }
                    },
                    label = {
                        Text(
                            text          = navItem.title,
                            fontSize      = 10.sp,
                            fontWeight    = if (isSelected) FontWeight.ExtraBold else FontWeight.Normal,
                            letterSpacing = if (isSelected) 0.5.sp else 0.sp,
                            color         = if (isSelected) GoldAccent else SilverGray.copy(alpha = 0.6f)
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        indicatorColor = GoldAccent.copy(alpha = 0.12f)
                    )
                )
            }
        }
    }
}

/* ─── Sacred FAB ─────────────────────────────────────────────────────────── */

@Composable
fun SacredFAB(onClick: () -> Unit) {
    val infiniteTransition = rememberInfiniteTransition(label = "fab_glow")
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue  = 0.9f,
        animationSpec = infiniteRepeatable(
            tween(1400, easing = FastOutSlowInEasing),
            RepeatMode.Reverse
        ),
        label = "fab_glow"
    )

    Box(contentAlignment = Alignment.Center) {
        /* pulsing glow ring */
        Box(
            modifier = Modifier
                .size(62.dp)
                .clip(CircleShape)
                .background(GoldAccent.copy(alpha = glowAlpha * 0.18f))
        )
        FloatingActionButton(
            onClick          = onClick,
            shape            = CircleShape,
            containerColor   = GoldDeep,
            contentColor     = NavyDeep,
            elevation        = FloatingActionButtonDefaults.elevation(
                defaultElevation = 8.dp
            ),
            modifier         = Modifier.size(52.dp)
        ) {
            Icon(
                imageVector        = Icons.Filled.Add,
                contentDescription = "Post",
                tint               = NavyDeep,
                modifier           = Modifier.size(26.dp)
            )
        }
    }
}

/* ─── Sacred Snackbar ───────────────────────────────────────────────────── */

@Composable
fun SacredSnackbar(data: SnackbarData) {
    Box(
        modifier = Modifier
            .padding(horizontal = 24.dp, vertical = 8.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(
                Brush.horizontalGradient(listOf(NavyMid, Color(0xFF1E3A5F)))
            )
            .shadow(elevation = 8.dp, shape = RoundedCornerShape(12.dp))
    ) {
        /* gold left accent */
        Box(
            modifier = Modifier
                .width(3.dp)
                .fillMaxHeight()
                .background(
                    Brush.verticalGradient(listOf(GoldDeep, GoldAccent))
                )
        )
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text      = data.visuals.message,
                color     = IvoryWhite,
                fontSize  = 13.sp,
                modifier  = Modifier.weight(1f)
            )
            data.visuals.actionLabel?.let { label ->
                Spacer(Modifier.width(12.dp))
                TextButton(onClick = { data.performAction() }) {
                    Text(label, color = GoldAccent, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

/* ─── Nav Items ─────────────────────────────────────────────────────────── */

val itemsBottomNav = listOf(
    NavItems(
        title         = "Home",
        selectedIcon  = R.drawable.baseline_home_24,
        unselectedIcon = R.drawable.outline_home_24,
        routePath     = ScreenRoutes.Home
    ),
    NavItems(
        title         = "Members",
        selectedIcon  = R.drawable.baseline_group_24,
        unselectedIcon = R.drawable.outline_group_24,
        routePath     = ScreenRoutes.Members
    ),
    NavItems(
        title         = "Events",
        selectedIcon  = R.drawable.baseline_live_tv_24,
        unselectedIcon = R.drawable.baseline_live_tv_24,
        routePath     = ScreenRoutes.EventMissionView
    ),
    NavItems(
        title         = "Live TV",
        selectedIcon  = R.drawable.baseline_live_tv_24,
        unselectedIcon = R.drawable.baseline_live_tv_24,
        routePath     = ScreenRoutes.LiveTV
    )
)

data class NavItems(
    val title: String,
    val selectedIcon: Int,
    val unselectedIcon: Int,
    val routePath: String,
    val isDisabled: Boolean = false
)