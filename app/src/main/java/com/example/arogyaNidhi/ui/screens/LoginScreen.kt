package com.example.arogyaNidhi.ui.screens


import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.arogyaNidhi.data.repository.AuthRepository
import com.example.arogyaNidhi.ui.theme.*
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(onLoginSuccess: () -> Unit, onNavigateToRegister: () -> Unit) {
    val context = LocalContext.current
    val authRepo = remember { AuthRepository(context) }
    val scope = rememberCoroutineScope()
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { visible = true }

    Box(modifier = Modifier.fillMaxSize().background(Brush.verticalGradient(colors = listOf(ArogyaGreenDark, ArogyaGreen), endY = 400f))) {
        Column(modifier = Modifier.fillMaxWidth().padding(top = 60.dp, start = 24.dp, end = 24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text("🏥", fontSize = 52.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text("Arogya Nidhi", style = MaterialTheme.typography.headlineLarge, color = Color.White, fontWeight = FontWeight.ExtraBold)
            Text("Welcome back", style = MaterialTheme.typography.bodyLarge, color = Color.White.copy(alpha = 0.8f))
        }
        AnimatedVisibility(visible = visible, enter = fadeIn() + slideInVertically(initialOffsetY = { it / 2 }), modifier = Modifier.align(Alignment.BottomCenter)) {
            Card(modifier = Modifier.fillMaxWidth().fillMaxHeight(0.68f), shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(28.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    Text("Sign In", style = MaterialTheme.typography.headlineMedium, color = TextDark, fontWeight = FontWeight.Bold)
                    Text("Access your health scheme information", style = MaterialTheme.typography.bodyMedium, color = TextLight)
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(value = email, onValueChange = { email = it; errorMessage = "" }, label = { Text("Email Address") }, leadingIcon = { Icon(Icons.Default.Email, null, tint = ArogyaGreen) }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email), modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp), colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = ArogyaGreen, focusedLabelColor = ArogyaGreen), singleLine = true)
                    OutlinedTextField(value = password, onValueChange = { password = it; errorMessage = "" }, label = { Text("Password") }, leadingIcon = { Icon(Icons.Default.Lock, null, tint = ArogyaGreen) }, trailingIcon = { IconButton(onClick = { passwordVisible = !passwordVisible }) { Icon(if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility, null, tint = TextLight) } }, visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password), modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp), colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = ArogyaGreen, focusedLabelColor = ArogyaGreen), singleLine = true)
                    if (errorMessage.isNotEmpty()) {
                        Card(colors = CardDefaults.cardColors(containerColor = Color(0xFFFFEBEE)), shape = RoundedCornerShape(8.dp)) {
                            Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Error, null, tint = ErrorRed, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(errorMessage, color = ErrorRed, style = MaterialTheme.typography.bodySmall)
                            }
                        }
                    }
                    Button(onClick = {
                        if (email.isBlank() || password.isBlank()) { errorMessage = "Please fill all fields"; return@Button }
                        scope.launch {
                            isLoading = true
                            authRepo.login(email.trim(), password).fold(onSuccess = { onLoginSuccess() }, onFailure = { errorMessage = it.message ?: "Login failed" })
                            isLoading = false
                        }
                    }, modifier = Modifier.fillMaxWidth().height(52.dp), shape = RoundedCornerShape(12.dp), colors = ButtonDefaults.buttonColors(containerColor = ArogyaGreen), enabled = !isLoading) {
                        if (isLoading) CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp), strokeWidth = 2.dp)
                        else Text("Sign In", style = MaterialTheme.typography.titleMedium, color = Color.White, fontWeight = FontWeight.SemiBold)
                    }
                    OutlinedButton(onClick = onNavigateToRegister, modifier = Modifier.fillMaxWidth().height(52.dp), shape = RoundedCornerShape(12.dp), colors = ButtonDefaults.outlinedButtonColors(contentColor = ArogyaGreen)) {
                        Text("Create New Account", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                    }
                    Text("Your data stays on your device. We never store personal information.", style = MaterialTheme.typography.bodySmall, color = TextLight, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
                }
            }
        }
    }
}