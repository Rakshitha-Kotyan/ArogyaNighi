package com.example.arogyaNidhi.ui.screens


import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.arogyaNidhi.data.repository.AuthRepository
import com.example.arogyaNidhi.ui.theme.*
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(onNavigateToLogin: () -> Unit, onNavigateToQuiz: () -> Unit) {
    val context = LocalContext.current
    val authRepo = remember { AuthRepository(context) }
    val scaleAnim = remember { Animatable(0f) }
    val alphaAnim = remember { Animatable(0f) }
    val textAlphaAnim = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        scaleAnim.animateTo(1f, animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow))
        alphaAnim.animateTo(1f, animationSpec = tween(600))
        textAlphaAnim.animateTo(1f, animationSpec = tween(800, delayMillis = 300))
        delay(2000)
        if (authRepo.isLoggedIn()) onNavigateToQuiz() else onNavigateToLogin()
    }

    Box(
        modifier = Modifier.fillMaxSize().background(
            Brush.verticalGradient(colors = listOf(ArogyaGreenDark, ArogyaGreen, ArogyaTeal))
        ),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier.size(120.dp).scale(scaleAnim.value).background(Color.White, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text("🏥", fontSize = 56.sp)
            }
            Spacer(modifier = Modifier.height(28.dp))
            Text("Arogya Nidhi", style = MaterialTheme.typography.displayMedium, color = Color.White, fontWeight = FontWeight.ExtraBold, modifier = Modifier.alpha(alphaAnim.value))
            Spacer(modifier = Modifier.height(8.dp))
            Text("आरोग्य निधि", style = MaterialTheme.typography.titleLarge, color = Color.White.copy(alpha = 0.85f), modifier = Modifier.alpha(alphaAnim.value))
            Spacer(modifier = Modifier.height(16.dp))
            Text("Health Scheme Eligibility Checker", style = MaterialTheme.typography.bodyLarge, color = Color.White.copy(alpha = 0.75f), textAlign = TextAlign.Center, modifier = Modifier.alpha(textAlphaAnim.value).padding(horizontal = 32.dp))
            Spacer(modifier = Modifier.height(8.dp))
            Text("Know your rights. Access your benefits.", style = MaterialTheme.typography.bodyMedium, color = ArogyaGold, fontWeight = FontWeight.Medium, modifier = Modifier.alpha(textAlphaAnim.value))
        }
        Text("Free • Offline • Secure", style = MaterialTheme.typography.labelLarge, color = Color.White.copy(alpha = 0.75f), modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 40.dp).alpha(textAlphaAnim.value))
    }
}