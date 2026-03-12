package com.shimmita.full_gospel.auth

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shimmita.full_gospel.viewmodel.ViewModel

/* ─── Palette ──────────────────────────────────────────────────────────── */
private val SheetBg    = Color(0xFF0F1C2E)
private val SheetMid   = Color(0xFF162236)
private val GoldAccent = Color(0xFFF5C842)
private val GoldDeep   = Color(0xFFB8860B)
private val GoldLight  = Color(0xFFFFD77E)
private val IvoryWhite = Color(0xFFF8F4EC)
private val SilverGray = Color(0xFF8FA6BA)
private val FieldBg    = Color(0xFF1B2E45)
private val ErrorRed   = Color(0xFFE57373)

/* ─── Text-field colours helper ─────────────────────────────────────────── */
@Composable
private fun sacredFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedBorderColor        = GoldAccent,
    unfocusedBorderColor      = GoldDeep.copy(alpha = 0.5f),
    focusedLabelColor         = GoldAccent,
    unfocusedLabelColor       = SilverGray,
    cursorColor               = GoldAccent,
    focusedTextColor          = IvoryWhite,
    unfocusedTextColor        = IvoryWhite.copy(alpha = 0.85f),
    focusedContainerColor     = FieldBg,
    unfocusedContainerColor   = FieldBg.copy(alpha = 0.8f),
    focusedPlaceholderColor   = SilverGray.copy(alpha = 0.6f),
    unfocusedPlaceholderColor = SilverGray.copy(alpha = 0.4f)
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginModalSheet(viewModel: ViewModel) {
    val context = LocalContext.current

    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
        confirmValueChange = { newState -> newState != SheetValue.Hidden }
    )

    var mode             by remember { mutableStateOf("login") }
    var username         by remember { mutableStateOf("") }
    var password         by remember { mutableStateOf("") }
    var firstName        by remember { mutableStateOf("") }
    var lastName         by remember { mutableStateOf("") }
    var phone            by remember { mutableStateOf("") }
    var selectedPosition by remember { mutableStateOf("") }
    var expanded         by remember { mutableStateOf(false) }
    var isProcessing     by remember { mutableStateOf(false) }

    val churchPositions = remember { churchRolesList() }

    ModalBottomSheet(
        onDismissRequest = {},
        sheetState       = sheetState,
        shape            = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        containerColor   = SheetBg,
        dragHandle       = {
            Box(
                modifier = Modifier
                    .padding(top = 12.dp, bottom = 4.dp)
                    .size(width = 40.dp, height = 4.dp)
                    .background(
                        GoldAccent.copy(alpha = 0.5f),
                        RoundedCornerShape(50)
                    )
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp)
                .padding(bottom = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            /* ── Sacred header ── */
            Spacer(Modifier.height(8.dp))
            Text(text = "✝", fontSize = 34.sp, color = GoldAccent)
            Spacer(Modifier.height(8.dp))

            Text(
                text = when (mode) {
                    "login"    -> "Shalom, Welcome"
                    "register" -> "Join the Family"
                    else       -> "Account"
                },
                fontSize   = 22.sp,
                fontWeight = FontWeight.ExtraBold,
                color      = IvoryWhite,
                letterSpacing = 1.sp
            )

            Spacer(Modifier.height(4.dp))
            Text(
                text = when (mode) {
                    "login"    -> "Sign in to continue your journey of faith"
                    "register" -> "Create your account and walk with us"
                    else       -> ""
                },
                fontSize  = 12.sp,
                fontStyle = FontStyle.Italic,
                color     = GoldLight.copy(alpha = 0.7f),
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(6.dp))

            /* gold divider */
            HorizontalDivider(
                thickness = 1.dp,
                color     = GoldAccent.copy(alpha = 0.25f),
                modifier  = Modifier.padding(vertical = 14.dp)
            )

            /* ══════════════ LOGIN ══════════════ */
            AnimatedVisibility(
                visible = mode == "login",
                enter   = fadeIn() + slideInVertically { it / 4 },
                exit    = fadeOut() + slideOutVertically { -it / 4 }
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value         = username,
                        onValueChange = { username = it },
                        label         = { Text("Email Address") },
                        placeholder   = { Text("your@email.com") },
                        singleLine    = true,
                        colors        = sacredFieldColors(),
                        shape         = RoundedCornerShape(12.dp),
                        modifier      = Modifier.fillMaxWidth()
                    )
                    Spacer(Modifier.height(12.dp))
                    OutlinedTextField(
                        value               = password,
                        onValueChange       = { password = it },
                        label               = { Text("Password") },
                        visualTransformation = PasswordVisualTransformation(),
                        singleLine          = true,
                        colors              = sacredFieldColors(),
                        shape               = RoundedCornerShape(12.dp),
                        modifier            = Modifier.fillMaxWidth()
                    )

                    Spacer(Modifier.height(20.dp))

                    GoldGradientButton(
                        text       = if (isProcessing) "Signing In…" else "Sign In",
                        enabled    = !isProcessing,
                        onClick    = {
                            isProcessing = true
                            viewModel.login(username, password) { success, message ->
                                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                                isProcessing = false
                                if (success) viewModel.loadStarterScreenData()
                            }
                        }
                    )

                    Spacer(Modifier.height(10.dp))

                    TextButton(
                        onClick  = { mode = "register" },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            "Don't have an account?  Create one",
                            color    = GoldLight.copy(alpha = 0.85f),
                            fontSize = 13.sp
                        )
                    }
                }
            }

            /* ══════════════ REGISTER ══════════════ */
            AnimatedVisibility(
                visible = mode == "register",
                enter   = fadeIn() + slideInVertically { it / 4 },
                exit    = fadeOut() + slideOutVertically { -it / 4 }
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier             = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        OutlinedTextField(
                            value         = firstName,
                            onValueChange = { firstName = it },
                            label         = { Text("First Name") },
                            singleLine    = true,
                            colors        = sacredFieldColors(),
                            shape         = RoundedCornerShape(12.dp),
                            modifier      = Modifier.weight(1f)
                        )
                        OutlinedTextField(
                            value         = lastName,
                            onValueChange = { lastName = it },
                            label         = { Text("Last Name") },
                            singleLine    = true,
                            colors        = sacredFieldColors(),
                            shape         = RoundedCornerShape(12.dp),
                            modifier      = Modifier.weight(1f)
                        )
                    }
                    Spacer(Modifier.height(12.dp))
                    OutlinedTextField(
                        value         = username,
                        onValueChange = { username = it },
                        label         = { Text("Email Address") },
                        singleLine    = true,
                        colors        = sacredFieldColors(),
                        shape         = RoundedCornerShape(12.dp),
                        modifier      = Modifier.fillMaxWidth()
                    )
                    Spacer(Modifier.height(12.dp))
                    OutlinedTextField(
                        value               = password,
                        onValueChange       = { password = it },
                        label               = { Text("Password") },
                        visualTransformation = PasswordVisualTransformation(),
                        singleLine          = true,
                        colors              = sacredFieldColors(),
                        shape               = RoundedCornerShape(12.dp),
                        modifier            = Modifier.fillMaxWidth()
                    )
                    Spacer(Modifier.height(12.dp))
                    OutlinedTextField(
                        value         = phone,
                        onValueChange = { phone = it },
                        label         = { Text("Phone Number") },
                        singleLine    = true,
                        colors        = sacredFieldColors(),
                        shape         = RoundedCornerShape(12.dp),
                        modifier      = Modifier.fillMaxWidth()
                    )
                    Spacer(Modifier.height(12.dp))

                    /* Church position dropdown */
                    ExposedDropdownMenuBox(
                        expanded         = expanded,
                        onExpandedChange = { expanded = !expanded }
                    ) {
                        OutlinedTextField(
                            value         = selectedPosition.ifBlank { "" },
                            onValueChange = {},
                            readOnly      = true,
                            label         = { Text("Church Position / Role") },
                            placeholder   = { Text("Select your role") },
                            trailingIcon  = {
                                ExposedDropdownMenuDefaults.TrailingIcon(
                                    expanded = expanded
                                )
                            },
                            colors   = sacredFieldColors(),
                            shape    = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth()
                        )
                        ExposedDropdownMenu(
                            expanded         = expanded,
                            onDismissRequest = { expanded = false },
                            modifier         = Modifier.background(SheetMid)
                        ) {
                            churchPositions.forEach { role ->
                                DropdownMenuItem(
                                    text    = { Text(role, color = IvoryWhite, fontSize = 14.sp) },
                                    onClick = { selectedPosition = role; expanded = false }
                                )
                            }
                        }
                    }

                    Spacer(Modifier.height(20.dp))

                    GoldGradientButton(
                        text    = if (isProcessing) "Creating…" else "Create Account",
                        enabled = !isProcessing,
                        onClick = {
                            isProcessing = true
                            viewModel.register(
                                username, password, phone,
                                firstName, lastName, selectedPosition
                            ) { success, message ->
                                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                                isProcessing = false
                                if (success) mode = "login"
                            }
                        }
                    )

                    Spacer(Modifier.height(10.dp))

                    TextButton(
                        onClick  = { mode = "login" },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            "Already have an account?  Sign In",
                            color    = GoldLight.copy(alpha = 0.85f),
                            fontSize = 13.sp
                        )
                    }
                }
            }

            Spacer(Modifier.height(12.dp))

            /* footer verse */
            Text(
                text      = "\"Where two or three gather in my name…\"  — Matt 18:20",
                fontSize  = 11.sp,
                fontStyle = FontStyle.Italic,
                color     = SilverGray.copy(alpha = 0.5f),
                textAlign = TextAlign.Center
            )
        }
    }
}

/* ─── Reusable Gold Gradient Button ─────────────────────────────────────── */

@Composable
fun GoldGradientButton(
    text: String,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    Button(
        onClick  = onClick,
        enabled  = enabled,
        shape    = RoundedCornerShape(12.dp),
        colors   = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
        contentPadding = PaddingValues(),
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    if (enabled)
                        Brush.horizontalGradient(listOf(GoldDeep, GoldAccent, GoldDeep))
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
            Text(
                text       = text,
                color      = Color(0xFF0D1B2A),
                fontWeight = FontWeight.ExtraBold,
                fontSize   = 15.sp,
                letterSpacing = 1.sp
            )
        }
    }
}

/* ─── Helper ─────────────────────────────────────────────────────────────── */

private fun churchRolesList(): List<String> = listOf(
    "Member", "Pastor", "Youth", "Deacon", "Reverend",
    "Apostle", "Bishop", "Pianist", "Drummist",
    "Praise Team", "Praise Leader", "Worship Leader",
    "Worship Team", "Priest", "Treasurer", "Financier",
    "Secretary", "Chairlady", "Chairman", "Coordinator",
    "Usher", "Youth Leader"
).sorted()