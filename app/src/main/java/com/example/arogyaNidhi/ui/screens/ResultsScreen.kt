package com.example.arogyaNidhi.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.arogyaNidhi.data.models.Scheme
import com.example.arogyaNidhi.data.models.UserInput
import com.example.arogyaNidhi.data.repository.DataRepository
import com.example.arogyaNidhi.ui.theme.*

@Composable
fun ResultsScreen(
    userInput: UserInput,
    onViewDocuments: (String) -> Unit,
    onFindHospitals: () -> Unit,
    onRetakeQuiz: () -> Unit
) {
    val context = LocalContext.current
    val repo = remember { DataRepository(context) }
    val allSchemes = remember { repo.loadSchemes() }
    val eligibleSchemes = remember(userInput) { repo.checkEligibility(userInput, allSchemes) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(SurfaceWhite),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        // ── Result Header ───────────────────────────────────────
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(
                            colors = if (eligibleSchemes.isNotEmpty())
                                listOf(ArogyaGreenDark, ArogyaGreen)
                            else
                                listOf(Color(0xFF424242), Color(0xFF616161))
                        )
                    )
                    .padding(24.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .background(Color.White.copy(alpha = 0.15f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (eligibleSchemes.isNotEmpty()) "🎉" else "😔",
                            fontSize = 40.sp
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = if (eligibleSchemes.isNotEmpty())
                            "You qualify for ${eligibleSchemes.size} scheme${if (eligibleSchemes.size > 1) "s" else ""}!"
                        else "No schemes matched",
                        style = MaterialTheme.typography.headlineMedium,
                        color = Color.White,
                        fontWeight = FontWeight.ExtraBold,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = if (eligibleSchemes.isNotEmpty())
                            "Based on your profile, you are eligible for the following government health schemes"
                        else "Try changing your inputs or contact your local health officer",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White.copy(alpha = 0.85f),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        // ── Summary Card ────────────────────────────────────────
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = BackgroundGreen),
                elevation = CardDefaults.cardElevation(0.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Your Profile Summary",
                        style = MaterialTheme.typography.titleMedium,
                        color = ArogyaGreenDark,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        SummaryItem("Income", "₹${"%,d".format(userInput.income)}")
                        SummaryItem("State", userInput.state.ifBlank { "N/A" })
                        SummaryItem("BPL", if (userInput.bplStatus) "Yes ✓" else "No")
                        SummaryItem("Family", "${userInput.familySize} members")
                    }
                }
            }
        }

        // ── Scheme Cards ────────────────────────────────────────
        if (eligibleSchemes.isNotEmpty()) {
            item {
                Text(
                    text = "Eligible Schemes",
                    style = MaterialTheme.typography.titleLarge,
                    color = TextDark,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
                )
            }
            itemsIndexed(eligibleSchemes) { index, scheme ->
                var visible by remember { mutableStateOf(false) }
                LaunchedEffect(Unit) {
                    kotlinx.coroutines.delay(index * 100L)
                    visible = true
                }
                AnimatedVisibility(
                    visible = visible,
                    enter = fadeIn() + slideInVertically(initialOffsetY = { it / 2 })
                ) {
                    SchemeCard(
                        scheme = scheme,
                        onViewDocuments = { onViewDocuments(scheme.id) }
                    )
                }
            }
        } else {
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF8E1))
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("💡", fontSize = 40.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Suggestions",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF5D4037)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "• Try selecting a different state\n• Check if you have a BPL card\n• Contact your local Panchayat office\n• Visit nearest Common Service Centre",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color(0xFF795548)
                        )
                    }
                }
            }
        }

        // ── Action Buttons ──────────────────────────────────────
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Button(
                    onClick = onFindHospitals,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = ArogyaTeal)
                ) {
                    Icon(Icons.Default.LocalHospital, null, tint = Color.White)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        "Find Empaneled Hospitals",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
                OutlinedButton(
                    onClick = onRetakeQuiz,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = ArogyaGreen)
                ) {
                    Icon(Icons.Default.Refresh, null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Retake Quiz", fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
}

@Composable
private fun SummaryItem(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            style = MaterialTheme.typography.titleSmall,
            color = ArogyaGreenDark,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge,
            color = TextLight
        )
    }
}

@Composable
private fun SchemeCard(scheme: Scheme, onViewDocuments: () -> Unit) {
    val schemeColor = try {
        Color(android.graphics.Color.parseColor(scheme.color))
    } catch (e: Exception) {
        ArogyaGreen
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(schemeColor.copy(alpha = 0.12f), RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text("🏥", fontSize = 24.sp)
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = scheme.name,
                        style = MaterialTheme.typography.titleMedium,
                        color = TextDark,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = scheme.benefitAmount,
                        style = MaterialTheme.typography.bodySmall,
                        color = schemeColor,
                        fontWeight = FontWeight.SemiBold
                    )
                }
                Box(
                    modifier = Modifier
                        .background(Color(0xFFE8F5E9), RoundedCornerShape(8.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "✓ Eligible",
                        style = MaterialTheme.typography.labelLarge,
                        color = ArogyaGreenDark,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = scheme.description,
                style = MaterialTheme.typography.bodyMedium,
                color = TextMedium
            )
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    Icons.Default.Description,
                    null,
                    tint = TextLight,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "${scheme.documents.size} documents required",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextLight,
                    modifier = Modifier.weight(1f)
                )
                Button(
                    onClick = onViewDocuments,
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = schemeColor),
                    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp),
                    modifier = Modifier.height(36.dp)
                ) {
                    Text(
                        text = "View Documents",
                        style = MaterialTheme.typography.labelLarge,
                        color = Color.White
                    )
                }
            }
        }
    }
}