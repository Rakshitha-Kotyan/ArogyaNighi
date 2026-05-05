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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.arogyaNidhi.ui.theme.*
import com.example.arogyaNidhi.data.repository.AuthRepository
import com.example.arogyaNidhi.ui.theme.ArogyaGreen
import com.example.arogyaNidhi.ui.theme.ArogyaTeal
import com.example.arogyaNidhi.ui.theme.ErrorRed
import com.example.arogyaNidhi.ui.theme.TextDark
import kotlinx.coroutines.launch

@Composable
fun RegisterScreen(onRegisterSuccess: () -> Unit, onNavigateToLogin: () -> Unit) {
    val context = LocalContext.current
    val authRepo = remember { AuthRepository(context) }
    val scope = rememberCoroutineScope()
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { visible = true }

    Box(modifier = Modifier.fillMaxSize().background(Brush.verticalGradient(colors = listOf(ArogyaTeal, ArogyaGreen), endY = 350f))) {
        Column(modifier = Modifier.fillMaxWidth().padding(top = 48.dp, start = 24.dp, end = 24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text("✨", fontSize = 44.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text("Create Account", style = MaterialTheme.typography.headlineLarge, color = Color.White, fontWeight = FontWeight.ExtraBold)
            Text("Join and discover your health benefits", style = MaterialTheme.typography.bodyLarge, color = Color.White.copy(alpha = 0.8f))
        }
        AnimatedVisibility(visible = visible, enter = fadeIn() + slideInVertically(initialOffsetY = { it / 2 }), modifier = Modifier.align(Alignment.BottomCenter)) {
            Card(modifier = Modifier.fillMaxWidth().fillMaxHeight(0.75f), shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(28.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    Text("Sign Up", style = MaterialTheme.typography.headlineMedium, color = TextDark, fontWeight = FontWeight.Bold)
                    OutlinedTextField(value = username, onValueChange = { username = it; errorMessage = "" }, label = { Text("Full Name") }, leadingIcon = { Icon(Icons.Default.Person, null, tint = ArogyaGreen) }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp), colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = ArogyaGreen, focusedLabelColor = ArogyaGreen), singleLine = true)
                    OutlinedTextField(value = email, onValueChange = { email = it; errorMessage = "" }, label = { Text("Email Address") }, leadingIcon = { Icon(Icons.Default.Email, null, tint = ArogyaGreen) }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email), modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp), colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = ArogyaGreen, focusedLabelColor = ArogyaGreen), singleLine = true)
                    OutlinedTextField(value = password, onValueChange = { password = it; errorMessage = "" }, label = { Text("Password") }, leadingIcon = { Icon(Icons.Default.Lock, null, tint = ArogyaGreen) }, trailingIcon = { IconButton(onClick = { passwordVisible = !passwordVisible }) { Icon(if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility, null, tint = TextLight) } }, visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password), modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp), colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = ArogyaGreen, focusedLabelColor = ArogyaGreen), singleLine = true)
                    OutlinedTextField(value = confirmPassword, onValueChange = { confirmPassword = it; errorMessage = "" }, label = { Text("Confirm Password") }, leadingIcon = { Icon(Icons.Default.LockReset, null, tint = ArogyaGreen) }, visualTransformation = PasswordVisualTransformation(), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password), modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp), colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = ArogyaGreen, focusedLabelColor = ArogyaGreen), singleLine = true)
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
                        when {
                            username.isBlank() || email.isBlank() || password.isBlank() -> errorMessage = "Please fill all fields"
                            !email.contains("@") -> errorMessage = "Enter a valid email address"
                            password.length < 6 -> errorMessage = "Password must be at least 6 characters"
                            password != confirmPassword -> errorMessage = "Passwords do not match"
                            else -> scope.launch {
                                isLoading = true
                                authRepo.register(username.trim(), email.trim(), password).fold(onSuccess = { onRegisterSuccess() }, onFailure = { errorMessage = it.message ?: "Registration failed" })
                                isLoading = false
                            }
                        }
                    }, modifier = Modifier.fillMaxWidth().height(52.dp), shape = RoundedCornerShape(12.dp), colors = ButtonDefaults.buttonColors(containerColor = ArogyaGreen), enabled = !isLoading) {
                        if (isLoading) CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp), strokeWidth = 2.dp)
                        else Text("Create Account", color = Color.White, fontWeight = FontWeight.SemiBold)
                    }
                    TextButton(onClick = onNavigateToLogin, modifier = Modifier.fillMaxWidth()) {
                        Text("Already have an account? Sign In", color = ArogyaGreen)
                    }
                }
            }
        }
    }
}