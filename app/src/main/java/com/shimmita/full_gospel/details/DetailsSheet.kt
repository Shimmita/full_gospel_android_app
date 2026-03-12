package com.shimmita.full_gospel.details

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shimmita.full_gospel.screens.HomeCarouselItem

/* ─── Palette ──────────────────────────────────────────────────────────── */
private val SheetBg    = Color(0xFF0F1C2E)
private val GoldAccent = Color(0xFFF5C842)
private val GoldDeep   = Color(0xFFB8860B)
private val GoldLight  = Color(0xFFFFD77E)
private val IvoryWhite = Color(0xFFF8F4EC)
private val SilverGray = Color(0xFF8FA6BA)
private val BlockBg    = Color(0xFF162236)
private val DividerGold = Color(0x33F5C842)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailSheet(
    selectedItem: HomeCarouselItem?,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    /* staggered content reveal */
    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(selectedItem) { visible = true }

    ModalBottomSheet(
        onDismissRequest  = onDismiss,
        sheetState        = sheetState,
        shape             = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        containerColor    = SheetBg,
        dragHandle        = {
            Box(
                modifier = Modifier
                    .padding(top = 12.dp, bottom = 4.dp)
                    .size(width = 40.dp, height = 4.dp)
                    .background(GoldAccent.copy(alpha = 0.5f), RoundedCornerShape(50))
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp)
                .padding(bottom = 40.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(8.dp))

            /* ── Crown / cross ornament ── */
            AnimatedVisibility(
                visible = visible,
                enter   = fadeIn(tween(400)) + slideInVertically(tween(400)) { -20 }
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "✝", fontSize = 36.sp, color = GoldAccent)
                    Spacer(Modifier.height(6.dp))
                    Text(
                        text          = "· · · · · · · · · ·",
                        color         = GoldDeep.copy(alpha = 0.5f),
                        fontSize      = 10.sp,
                        letterSpacing = 2.sp
                    )
                }
            }

            Spacer(Modifier.height(12.dp))

            /* ── Author chip ── */
            AnimatedVisibility(
                visible = visible,
                enter   = fadeIn(tween(500, delayMillis = 80))
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(50.dp))
                            .background(
                                Brush.horizontalGradient(
                                    listOf(GoldDeep.copy(alpha = 0.25f), GoldAccent.copy(alpha = 0.15f))
                                )
                            )
                            .drawBehind {
                                drawLine(
                                    color       = GoldAccent.copy(alpha = 0.4f),
                                    start       = Offset(0f, size.height),
                                    end         = Offset(size.width, size.height),
                                    strokeWidth = 1f
                                )
                            }
                            .padding(horizontal = 20.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text       = "${selectedItem?.author ?: "—"}  ·  ${selectedItem?.position ?: ""}",
                            fontSize   = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color      = IvoryWhite
                        )
                    }
                }
            }

            Spacer(Modifier.height(20.dp))

            /* ── Gold divider ── */
            HorizontalDivider(thickness = 1.dp, color = DividerGold)

            Spacer(Modifier.height(24.dp))

            /* ── Message block ── */
            AnimatedVisibility(
                visible = visible,
                enter   = fadeIn(tween(500, delayMillis = 160)) + slideInVertically(tween(500, 160)) { 30 }
            ) {
                SacredSection(
                    icon    = "📖",
                    heading = "MESSAGE",
                    body    = selectedItem?.mainText ?: ""
                )
            }

            Spacer(Modifier.height(20.dp))

            /* ── Bible verse block ── */
            AnimatedVisibility(
                visible = visible,
                enter   = fadeIn(tween(500, delayMillis = 260)) + slideInVertically(tween(500, 260)) { 30 }
            ) {
                SacredSection(
                    icon    = "📜",
                    heading = "BIBLE REFERENCE",
                    body    = selectedItem?.bibleVerse ?: ""
                )
            }

            Spacer(Modifier.height(20.dp))

            /* ── Contact block ── */
            AnimatedVisibility(
                visible = visible,
                enter   = fadeIn(tween(500, delayMillis = 340)) + slideInVertically(tween(500, 340)) { 30 }
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(
                            Brush.linearGradient(
                                listOf(
                                    GoldDeep.copy(alpha = 0.18f),
                                    GoldAccent.copy(alpha = 0.08f)
                                )
                            )
                        )
                        .padding(16.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Rounded.Phone,
                            contentDescription = "Phone",
                            tint   = GoldAccent,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(Modifier.width(12.dp))
                        Column {
                            Text(
                                text          = "PHONE CONTACT",
                                fontSize      = 10.sp,
                                fontWeight    = FontWeight.Bold,
                                letterSpacing = 1.5.sp,
                                color         = GoldAccent.copy(alpha = 0.8f)
                            )
                            Spacer(Modifier.height(4.dp))
                            Text(
                                text     = selectedItem?.phoneNumber ?: "—",
                                fontSize = 15.sp,
                                color    = IvoryWhite,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.height(28.dp))

            /* ── Footer ornament ── */
            AnimatedVisibility(
                visible = visible,
                enter   = fadeIn(tween(600, delayMillis = 420))
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text          = "· · · ✦ · · ·",
                        color         = GoldDeep.copy(alpha = 0.5f),
                        fontSize      = 12.sp,
                        letterSpacing = 3.sp
                    )
                    Spacer(Modifier.height(10.dp))
                    Text(
                        text      = "\"Thy Word is a lamp unto my feet\"  — Psalm 119:105",
                        fontSize  = 11.sp,
                        fontStyle = FontStyle.Italic,
                        color     = SilverGray.copy(alpha = 0.5f),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

/* ─── Sacred Section Block ──────────────────────────────────────────────── */

@Composable
fun SacredSection(
    icon: String,
    heading: String,
    body: String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(BlockBg)
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = icon, fontSize = 16.sp)
            Spacer(Modifier.width(8.dp))
            Text(
                text          = heading,
                fontSize      = 10.sp,
                fontWeight    = FontWeight.ExtraBold,
                letterSpacing = 2.sp,
                color         = GoldAccent
            )
        }
        HorizontalDivider(
            modifier  = Modifier.padding(vertical = 10.dp),
            thickness = 1.dp,
            color     = DividerGold
        )
        Text(
            text       = body,
            fontSize   = 14.sp,
            lineHeight = 22.sp,
            color      = SilverGray.copy(alpha = 0.9f)
        )
    }
}