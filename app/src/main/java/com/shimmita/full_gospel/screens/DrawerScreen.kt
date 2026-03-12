package com.shimmita.full_gospel.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.rounded.ExitToApp
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shimmita.full_gospel.viewmodel.ViewModel
import kotlinx.coroutines.launch

/* ─── Palette ──────────────────────────────────────────────────────────── */
private val DrawerBg      = Color(0xFF0D1B2A)
private val DrawerMid     = Color(0xFF162236)
private val DrawerTop     = Color(0xFF1B2E45)
private val GoldAccent    = Color(0xFFF5C842)
private val GoldDeep      = Color(0xFFB8860B)
private val GoldLight     = Color(0xFFFFD77E)
private val IvoryWhite    = Color(0xFFF8F4EC)
private val SilverGray    = Color(0xFF8FA6BA)
private val DividerGold   = Color(0x33F5C842)

@Composable
fun DrawerScreen(
    drawerState: DrawerState,
    isLoggedIn: Boolean,
    handleShowLoginSheet: () -> Unit,
    viewModel: ViewModel
) {
    val currentUser by viewModel.globalUserState.collectAsState()
    val scope       = rememberCoroutineScope()

    /* staggered entrance */
    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { visible = true }

    /* avatar ring pulse */
    val infiniteTransition = rememberInfiniteTransition(label = "ring")
    val ringAlpha by infiniteTransition.animateFloat(
        initialValue  = 0.35f,
        targetValue   = 0.85f,
        animationSpec = infiniteRepeatable(tween(1600, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label         = "ring"
    )

    Column(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth(0.80f)
            .background(DrawerBg)
            .verticalScroll(rememberScrollState())
            .drawBehind {
                /* right-edge gold accent line */
                drawLine(
                    color       = GoldAccent.copy(alpha = 0.3f),
                    start       = Offset(size.width, 0f),
                    end         = Offset(size.width, size.height),
                    strokeWidth = 1.5f
                )
            }
    ) {

        /* ── Hero header band ── */
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(listOf(DrawerTop, DrawerMid, DrawerBg))
                )
                .padding(top = 60.dp, bottom = 32.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {

                /* ── Avatar ── */
                AnimatedVisibility(
                    visible = visible,
                    enter   = fadeIn() + slideInHorizontally { -40 }
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        /* pulsing outer ring */
                        Box(
                            modifier = Modifier
                                .size(148.dp)
                                .clip(CircleShape)
                                .background(GoldAccent.copy(alpha = ringAlpha * 0.12f))
                        )
                        /* gold border ring */
                        Box(
                            modifier = Modifier
                                .size(136.dp)
                                .clip(CircleShape)
                                .border(
                                    width  = 2.dp,
                                    brush  = Brush.sweepGradient(
                                        listOf(GoldDeep, GoldAccent, GoldDeep)
                                    ),
                                    shape  = CircleShape
                                )
                                .background(DrawerTop)
                        ) {
                            Icon(
                                imageVector        = Icons.Filled.AccountCircle,
                                contentDescription = "avatar",
                                tint               = if (isLoggedIn) GoldAccent else SilverGray.copy(0.4f),
                                modifier           = Modifier
                                    .fillMaxSize()
                                    .padding(8.dp)
                            )
                        }
                    }
                }

                Spacer(Modifier.height(16.dp))

                /* ── Name / role ── */
                AnimatedVisibility(
                    visible = visible,
                    enter   = fadeIn() + slideInHorizontally { -30 }
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        if (isLoggedIn) {
                            Text(
                                text       = "${currentUser?.first_name} ${currentUser?.last_name}",
                                fontSize   = 20.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color      = IvoryWhite,
                                letterSpacing = 0.5.sp
                            )
                            Spacer(Modifier.height(4.dp))

                            /* role pill */
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(50))
                                    .background(GoldAccent.copy(alpha = 0.12f))
                                    .border(
                                        width  = 1.dp,
                                        color  = GoldDeep.copy(alpha = 0.5f),
                                        shape  = RoundedCornerShape(50)
                                    )
                                    .padding(horizontal = 14.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text       = "Church ${currentUser?.role ?: "Member"}",
                                    fontSize   = 12.sp,
                                    color      = GoldLight,
                                    fontWeight = FontWeight.SemiBold,
                                    letterSpacing = 0.5.sp
                                )
                            }

                            Spacer(Modifier.height(6.dp))
                            Text(
                                text      = currentUser?.username ?: "",
                                fontSize  = 11.sp,
                                fontStyle = FontStyle.Italic,
                                color     = SilverGray.copy(alpha = 0.6f)
                            )
                        } else {
                            Text(
                                text          = "GUEST",
                                fontSize      = 22.sp,
                                fontWeight    = FontWeight.ExtraBold,
                                letterSpacing = 4.sp,
                                color         = SilverGray.copy(alpha = 0.7f)
                            )
                            Spacer(Modifier.height(4.dp))
                            Text(
                                text      = "You are not signed in",
                                fontSize  = 12.sp,
                                fontStyle = FontStyle.Italic,
                                color     = SilverGray.copy(alpha = 0.45f)
                            )
                        }
                    }
                }
            }
        }

        /* gold divider */
        HorizontalDivider(thickness = 1.dp, color = DividerGold)

        Spacer(Modifier.height(32.dp))

        /* ── Church branding block ── */
        AnimatedVisibility(
            visible = visible,
            enter   = fadeIn() + slideInHorizontally { -50 }
        ) {
            Column(
                modifier              = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 28.dp),
                horizontalAlignment   = Alignment.CenterHorizontally
            ) {
                Text(
                    text          = "✝",
                    fontSize      = 28.sp,
                    color         = GoldAccent.copy(alpha = 0.8f)
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    text          = "FULL GOSPEL MINISTRY",
                    fontSize      = 12.sp,
                    fontWeight    = FontWeight.ExtraBold,
                    letterSpacing = 2.sp,
                    color         = IvoryWhite,
                    textAlign     = TextAlign.Center
                )
                Spacer(Modifier.height(6.dp))
                Text(
                    text      = "\"The Spirit of the Lord is upon me\"\n— Luke 4:18",
                    fontSize  = 11.sp,
                    fontStyle = FontStyle.Italic,
                    color     = GoldLight.copy(alpha = 0.55f),
                    textAlign = TextAlign.Center,
                    lineHeight = 18.sp
                )
            }
        }

        Spacer(Modifier.height(36.dp))
        HorizontalDivider(
            thickness = 1.dp,
            color     = DividerGold,
            modifier  = Modifier.padding(horizontal = 24.dp)
        )
        Spacer(Modifier.height(32.dp))

        /* ── Action button ── */
        AnimatedVisibility(
            visible = visible,
            enter   = fadeIn() + slideInHorizontally { -60 }
        ) {
            Box(
                modifier            = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 28.dp),
                contentAlignment    = Alignment.Center
            ) {
                if (isLoggedIn) {
                    /* Logout — outlined red-tinted button */
                    OutlinedButton(
                        onClick = {
                            scope.launch {
                                viewModel.logout()
                                viewModel.loadStarterScreenData()
                                handleShowLoginSheet()
                                drawerState.close()
                            }
                        },
                        shape  = RoundedCornerShape(12.dp),
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            Brush.horizontalGradient(
                                listOf(Color(0xFFB71C1C), Color(0xFFEF5350))
                            )
                        ),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = Color(0xFFEF9A9A)
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                    ) {
                        Icon(
                            imageVector        = Icons.Rounded.ExitToApp,
                            contentDescription = "Logout",
                            modifier           = Modifier.size(18.dp)
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(
                            text          = "Sign Out",
                            fontWeight    = FontWeight.Bold,
                            fontSize      = 14.sp,
                            letterSpacing = 0.5.sp
                        )
                    }
                } else {
                    /* Login — gold gradient button */
                    Button(
                        onClick        = { handleShowLoginSheet() },
                        shape          = RoundedCornerShape(12.dp),
                        colors         = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                        contentPadding = PaddingValues(),
                        modifier       = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    Brush.horizontalGradient(listOf(GoldDeep, GoldAccent, GoldDeep))
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector        = Icons.Rounded.Person,
                                    contentDescription = "Login",
                                    tint               = DrawerBg,
                                    modifier           = Modifier.size(18.dp)
                                )
                                Spacer(Modifier.width(8.dp))
                                Text(
                                    text          = "Sign In",
                                    color         = DrawerBg,
                                    fontWeight    = FontWeight.ExtraBold,
                                    fontSize      = 15.sp,
                                    letterSpacing = 1.sp
                                )
                            }
                        }
                    }
                }
            }
        }

        Spacer(Modifier.height(40.dp))

        /* ── Footer ornament ── */
        AnimatedVisibility(visible = visible, enter = fadeIn()) {
            Column(
                modifier            = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text          = "· · · ✦ · · ·",
                    color         = GoldDeep.copy(alpha = 0.4f),
                    fontSize      = 11.sp,
                    letterSpacing = 3.sp
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    text      = "\"I am the way, the truth\nand the life\"  — John 14:6",
                    fontSize  = 10.sp,
                    fontStyle = FontStyle.Italic,
                    color     = SilverGray.copy(alpha = 0.38f),
                    textAlign = TextAlign.Center,
                    lineHeight = 16.sp
                )
                Spacer(Modifier.height(24.dp))
            }
        }
    }
}