import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.material3.ExposedDropdownMenuDefaults.TrailingIcon
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shimmita.full_gospel.viewmodel.ViewModel
import kotlinx.coroutines.launch

/* ─── Palette ──────────────────────────────────────────────────────────── */
private val SheetBg    = Color(0xFF0F1C2E)
private val SheetMid   = Color(0xFF162236)
private val BlockBg    = Color(0xFF1B2E45)
private val GoldAccent = Color(0xFFF5C842)
private val GoldDeep   = Color(0xFFB8860B)
private val GoldLight  = Color(0xFFFFD77E)
private val IvoryWhite = Color(0xFFF8F4EC)
private val SilverGray = Color(0xFF8FA6BA)
private val NavyDeep   = Color(0xFF0D1B2A)
private val DividerGold = Color(0x33F5C842)

/* ─── Field colours ─────────────────────────────────────────────────────── */
@Composable
private fun sacredFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedBorderColor        = GoldAccent,
    unfocusedBorderColor      = GoldDeep.copy(alpha = 0.5f),
    focusedLabelColor         = GoldAccent,
    unfocusedLabelColor       = SilverGray,
    cursorColor               = GoldAccent,
    focusedTextColor          = IvoryWhite,
    unfocusedTextColor        = IvoryWhite.copy(alpha = 0.85f),
    focusedContainerColor     = BlockBg,
    unfocusedContainerColor   = BlockBg.copy(alpha = 0.8f),
    focusedPlaceholderColor   = SilverGray.copy(alpha = 0.55f),
    unfocusedPlaceholderColor = SilverGray.copy(alpha = 0.38f)
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostContentModalSheet(
    viewModel: ViewModel,
    onDismiss: () -> Unit
) {
    val context           = LocalContext.current
    val scope             = rememberCoroutineScope()
    val sheetState        = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val categories = listOf(
        "Daily Prayer", "Weekly Prayer", "Church Event",
        "Nature Talent", "Testimonials", "Sunday Diaries", "Announcement"
    )

    var expanded         by remember { mutableStateOf(false) }
    var selectedCategory by remember { mutableStateOf(categories[0]) }
    var description      by remember { mutableStateOf("") }
    var bibleReference   by remember { mutableStateOf("") }
    var isProcessing     by remember { mutableStateOf(false) }

    val globalUserState  by viewModel.globalUserState.collectAsState()

    /* staggered entrance */
    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { visible = true }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState       = sheetState,
        shape            = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        containerColor   = SheetBg,
        dragHandle       = {
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
                .padding(bottom = 36.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(8.dp))

            /* ── Header ── */
            AnimatedVisibility(
                visible = visible,
                enter   = fadeIn() + slideInVertically { -16 }
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "✝", fontSize = 32.sp, color = GoldAccent)
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text          = "SHARE WITH THE COMMUNITY",
                        fontSize      = 14.sp,
                        fontWeight    = FontWeight.ExtraBold,
                        letterSpacing = 2.sp,
                        color         = IvoryWhite
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text      = "Post a message to the church fraternity",
                        fontSize  = 11.sp,
                        fontStyle = FontStyle.Italic,
                        color     = GoldLight.copy(alpha = 0.65f),
                        textAlign = TextAlign.Center
                    )
                }
            }

            Spacer(Modifier.height(6.dp))
            HorizontalDivider(
                thickness = 1.dp,
                color     = DividerGold,
                modifier  = Modifier.padding(vertical = 14.dp)
            )

            /* ── Category picker ── */
            AnimatedVisibility(
                visible = visible,
                enter   = fadeIn() + slideInVertically(initialOffsetY = { it / 5 })
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    SacredFieldLabel(icon = "📂", label = "POSTING ABOUT")
                    Spacer(Modifier.height(6.dp))

                    ExposedDropdownMenuBox(
                        expanded         = expanded,
                        onExpandedChange = { expanded = !expanded },
                        modifier         = Modifier.fillMaxWidth()
                    ) {
                        OutlinedTextField(
                            value         = selectedCategory,
                            onValueChange = {},
                            readOnly      = true,
                            label         = { Text("Select Category") },
                            trailingIcon  = { TrailingIcon(expanded = expanded) },
                            colors        = sacredFieldColors(),
                            shape         = RoundedCornerShape(12.dp),
                            modifier      = Modifier
                                .menuAnchor()
                                .fillMaxWidth()
                        )
                        ExposedDropdownMenu(
                            expanded         = expanded,
                            onDismissRequest = { expanded = false },
                            modifier         = Modifier.background(SheetMid)
                        ) {
                            categories.forEach { item ->
                                DropdownMenuItem(
                                    text    = {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Text(
                                                text  = categoryIcon(item),
                                                fontSize = 14.sp,
                                                modifier = Modifier.padding(end = 8.dp)
                                            )
                                            Text(item, color = IvoryWhite, fontSize = 14.sp)
                                        }
                                    },
                                    onClick = { selectedCategory = item; expanded = false }
                                )
                            }
                        }
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            /* ── Description ── */
            AnimatedVisibility(
                visible = visible,
                enter   = fadeIn() + slideInVertically(initialOffsetY = { it / 4 })
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    SacredFieldLabel(icon = "✍️", label = "YOUR MESSAGE")
                    Spacer(Modifier.height(6.dp))
                    OutlinedTextField(
                        value         = description,
                        onValueChange = { description = it },
                        label         = { Text("Description") },
                        placeholder   = { Text("Write your message, prayer or testimony here…") },
                        colors        = sacredFieldColors(),
                        shape         = RoundedCornerShape(12.dp),
                        minLines      = 4,
                        maxLines      = 7,
                        modifier      = Modifier.fillMaxWidth()
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            /* ── Bible reference ── */
            AnimatedVisibility(
                visible = visible,
                enter   = fadeIn() + slideInVertically(initialOffsetY = { it / 3 })
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    SacredFieldLabel(icon = "📜", label = "BIBLE REFERENCE")
                    Spacer(Modifier.height(6.dp))
                    OutlinedTextField(
                        value         = bibleReference,
                        onValueChange = { bibleReference = it },
                        label         = { Text("Bible Verse") },
                        placeholder   = { Text("e.g. John 3:16") },
                        singleLine    = true,
                        colors        = sacredFieldColors(),
                        shape         = RoundedCornerShape(12.dp),
                        modifier      = Modifier.fillMaxWidth()
                    )
                }
            }

            Spacer(Modifier.height(28.dp))

            /* ── Submit button ── */
            AnimatedVisibility(
                visible = visible,
                enter   = fadeIn() + slideInVertically(initialOffsetY = { it / 2 })
            ) {
                Column(
                    modifier              = Modifier.fillMaxWidth(),
                    horizontalAlignment   = Alignment.CenterHorizontally
                ) {
                    Button(
                        enabled        = !isProcessing,
                        onClick        = {
                            isProcessing = true
                            if (description.isBlank() || bibleReference.isBlank()) {
                                isProcessing = false
                                Toast.makeText(
                                    context,
                                    "Please fill in all fields!",
                                    Toast.LENGTH_SHORT
                                ).show()
                                return@Button
                            }
                            globalUserState?.let { user ->
                                scope.launch {
                                    val success = viewModel.createPost(
                                        category = selectedCategory,
                                        username = user.username,
                                        phone    = user.phone,
                                        author   = "${user.first_name} ${user.last_name}",
                                        role     = user.role,
                                        details  = description,
                                        verse    = bibleReference
                                    )
                                    isProcessing = false
                                    if (success) {
                                        viewModel.loadStarterScreenData()
                                        Toast.makeText(
                                            context,
                                            "✝  Posted successfully!",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        onDismiss()
                                    } else {
                                        Toast.makeText(
                                            context,
                                            "Failed — please try again.",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                            } ?: run {
                                isProcessing = false
                                Toast.makeText(
                                    context,
                                    "User not found. Please log in again.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        },
                        shape          = RoundedCornerShape(12.dp),
                        colors         = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                        contentPadding = PaddingValues(),
                        modifier       = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    if (!isProcessing)
                                        Brush.horizontalGradient(
                                            listOf(GoldDeep, GoldAccent, GoldDeep)
                                        )
                                    else
                                        Brush.horizontalGradient(
                                            listOf(
                                                GoldDeep.copy(alpha = 0.4f),
                                                GoldAccent.copy(alpha = 0.4f)
                                            )
                                        )
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            if (isProcessing) {
                                Row(
                                    verticalAlignment     = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    CircularProgressIndicator(
                                        color     = NavyDeep,
                                        strokeWidth = 2.dp,
                                        modifier  = Modifier.size(18.dp)
                                    )
                                    Text(
                                        text       = "Posting…",
                                        color      = NavyDeep,
                                        fontWeight = FontWeight.ExtraBold,
                                        fontSize   = 15.sp
                                    )
                                }
                            } else {
                                Text(
                                    text          = "✝  Post Entry",
                                    color         = NavyDeep,
                                    fontWeight    = FontWeight.ExtraBold,
                                    fontSize      = 15.sp,
                                    letterSpacing = 1.sp
                                )
                            }
                        }
                    }

                    Spacer(Modifier.height(14.dp))

                    /* cancel link */
                    TextButton(onClick = onDismiss) {
                        Text(
                            text     = "Cancel",
                            color    = SilverGray.copy(alpha = 0.6f),
                            fontSize = 13.sp
                        )
                    }
                }
            }

            Spacer(Modifier.height(8.dp))

            /* ── Footer verse ── */
            Text(
                text      = "· · · ✦ · · ·",
                color     = GoldDeep.copy(alpha = 0.45f),
                fontSize  = 11.sp,
                letterSpacing = 3.sp
            )
            Spacer(Modifier.height(6.dp))
            Text(
                text      = "\"Let your light shine before others\"  — Matt 5:16",
                fontSize  = 11.sp,
                fontStyle = FontStyle.Italic,
                color     = SilverGray.copy(alpha = 0.45f),
                textAlign = TextAlign.Center
            )
        }
    }
}

/* ─── Section label helper ──────────────────────────────────────────────── */

@Composable
private fun SacredFieldLabel(icon: String, label: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(text = icon, fontSize = 13.sp)
        Spacer(Modifier.width(6.dp))
        Text(
            text          = label,
            fontSize      = 10.sp,
            fontWeight    = FontWeight.ExtraBold,
            letterSpacing = 1.5.sp,
            color         = GoldAccent.copy(alpha = 0.85f)
        )
    }
}

/* ─── Category icon map ─────────────────────────────────────────────────── */

private fun categoryIcon(category: String): String = when (category) {
    "Daily Prayer"   -> "🙏"
    "Weekly Prayer"  -> "📿"
    "Church Event"   -> "⛪"
    "Nature Talent"  -> "🌿"
    "Testimonials"   -> "✨"
    "Sunday Diaries" -> "📖"
    "Announcement"   -> "📢"
    else             -> "✝"
}