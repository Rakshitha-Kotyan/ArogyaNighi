package com.example.arogyaNidhi.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.arogyaNidhi.data.models.QuestionType
import com.example.arogyaNidhi.data.models.UserInput
import com.example.arogyaNidhi.data.repository.DataRepository
import com.example.arogyaNidhi.ui.theme.ArogyaGold
import com.example.arogyaNidhi.ui.theme.ArogyaGreen
import com.example.arogyaNidhi.ui.theme.ArogyaGreenDark
import com.example.arogyaNidhi.ui.theme.BackgroundGreen
import com.example.arogyaNidhi.ui.theme.ErrorRed
import com.example.arogyaNidhi.ui.theme.SurfaceWhite
import com.example.arogyaNidhi.ui.theme.TextDark
import com.example.arogyaNidhi.ui.theme.TextLight
import com.example.arogyaNidhi.ui.theme.TextMedium

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizScreen(onQuizComplete: (UserInput) -> Unit) {
    val context = LocalContext.current
    val repo = remember { DataRepository(context) }
    val questions = remember { repo.getQuizQuestions() }

    var currentStep by remember { mutableIntStateOf(0) }
    var income by remember { mutableStateOf("") }
    var occupation by remember { mutableStateOf("") }
    var bplStatus by remember { mutableStateOf(false) }
    var selectedState by remember { mutableStateOf("") }
    var familySize by remember { mutableStateOf("") }
    var errorText by remember { mutableStateOf("") }

    fun validateAndNext() {
        errorText = ""
        when (currentStep) {
            0 -> {
                val amt = income.toLongOrNull()
                if (amt == null || amt < 0) {
                    errorText = "Please enter a valid income amount"
                    return
                }
            }
            1 -> if (occupation.isBlank()) {
                errorText = "Please select your occupation"
                return
            }
            2 -> { /* boolean always valid */ }
            3 -> if (selectedState.isBlank()) {
                errorText = "Please select your state"
                return
            }
            4 -> {
                val size = familySize.toIntOrNull()
                if (size == null || size < 1) {
                    errorText = "Please enter a valid family size (min 1)"
                    return
                }
            }
        }
        if (currentStep < questions.size - 1) {
            currentStep++
        } else {
            onQuizComplete(
                UserInput(
                    income = income.toLongOrNull() ?: 0L,
                    occupation = repo.mapOccupation(occupation),
                    bplStatus = bplStatus,
                    state = selectedState,
                    familySize = familySize.toIntOrNull() ?: 1
                )
            )
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SurfaceWhite)
    ) {
        // ── Top Header ──────────────────────────────────────────
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(ArogyaGreenDark, ArogyaGreen)
                    )
                )
                .padding(horizontal = 24.dp, vertical = 20.dp)
        ) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.Assignment,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Eligibility Quiz",
                        style = MaterialTheme.typography.titleLarge,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    questions.forEachIndexed { index, _ ->
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(6.dp)
                                .clip(RoundedCornerShape(3.dp))
                                .background(
                                    when {
                                        index < currentStep -> ArogyaGold
                                        index == currentStep -> Color.White
                                        else -> Color.White.copy(alpha = 0.3f)
                                    }
                                )
                        )
                    }
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Question ${currentStep + 1} of ${questions.size}",
                    style = MaterialTheme.typography.labelLarge,
                    color = Color.White.copy(alpha = 0.8f)
                )
            }
        }

        // ── Question Content ────────────────────────────────────
        AnimatedContent(
            targetState = currentStep,
            transitionSpec = {
                if (targetState > initialState) {
                    slideInHorizontally { it } + fadeIn() togetherWith
                            slideOutHorizontally { -it } + fadeOut()
                } else {
                    slideInHorizontally { -it } + fadeIn() togetherWith
                            slideOutHorizontally { it } + fadeOut()
                }
            },
            label = "quiz_step",
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) { step ->
            val question = questions[step]
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(52.dp)
                        .background(BackgroundGreen, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "${step + 1}",
                        style = MaterialTheme.typography.headlineMedium,
                        color = ArogyaGreen,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
                Text(
                    text = question.title,
                    style = MaterialTheme.typography.headlineMedium,
                    color = TextDark,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = question.subtitle,
                    style = MaterialTheme.typography.bodyLarge,
                    color = TextMedium
                )
                Spacer(modifier = Modifier.height(4.dp))

                when (question.type) {

                    QuestionType.INCOME_INPUT -> {
                        OutlinedTextField(
                            value = income,
                            onValueChange = { income = it; errorText = "" },
                            label = { Text("Annual Income (₹)") },
                            leadingIcon = {
                                Text(
                                    "₹",
                                    modifier = Modifier.padding(start = 12.dp),
                                    color = ArogyaGreen,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp
                                )
                            },
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Number
                            ),
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = ArogyaGreen,
                                focusedLabelColor = ArogyaGreen
                            ),
                            placeholder = { Text("e.g. 120000") },
                            singleLine = true
                        )
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text(
                                "Common income brackets:",
                                style = MaterialTheme.typography.labelLarge,
                                color = TextLight
                            )
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                listOf("50,000", "1,00,000", "2,00,000").forEach { hint ->
                                    SuggestionChip(
                                        onClick = {
                                            income = hint.replace(",", "")
                                            errorText = ""
                                        },
                                        label = { Text("₹$hint") },
                                        colors = SuggestionChipDefaults.suggestionChipColors(
                                            containerColor = BackgroundGreen
                                        )
                                    )
                                }
                            }
                        }
                    }

                    QuestionType.SINGLE_CHOICE -> {
                        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            question.options.forEach { option ->
                                val isSelected = occupation == option
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            occupation = option
                                            errorText = ""
                                        }
                                        .border(
                                            width = if (isSelected) 2.dp else 1.dp,
                                            color = if (isSelected) ArogyaGreen
                                            else Color(0xFFE0E0E0),
                                            shape = RoundedCornerShape(12.dp)
                                        ),
                                    shape = RoundedCornerShape(12.dp),
                                    colors = CardDefaults.cardColors(
                                        containerColor = if (isSelected) BackgroundGreen
                                        else Color.White
                                    ),
                                    elevation = CardDefaults.cardElevation(
                                        if (isSelected) 2.dp else 0.dp
                                    )
                                ) {
                                    Row(
                                        modifier = Modifier.padding(16.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        RadioButton(
                                            selected = isSelected,
                                            onClick = {
                                                occupation = option
                                                errorText = ""
                                            },
                                            colors = RadioButtonDefaults.colors(
                                                selectedColor = ArogyaGreen
                                            )
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = option,
                                            style = MaterialTheme.typography.bodyLarge,
                                            color = if (isSelected) ArogyaGreenDark
                                            else TextDark,
                                            fontWeight = if (isSelected) FontWeight.SemiBold
                                            else FontWeight.Normal
                                        )
                                    }
                                }
                            }
                        }
                    }

                    QuestionType.BOOLEAN_CHOICE -> {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            listOf(
                                true to "Yes, I have BPL Card",
                                false to "No BPL Card"
                            ).forEach { (value, label) ->
                                val isSelected = bplStatus == value
                                Card(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clickable { bplStatus = value }
                                        .border(
                                            width = if (isSelected) 2.dp else 1.dp,
                                            color = if (isSelected) ArogyaGreen
                                            else Color(0xFFE0E0E0),
                                            shape = RoundedCornerShape(16.dp)
                                        ),
                                    shape = RoundedCornerShape(16.dp),
                                    colors = CardDefaults.cardColors(
                                        containerColor = if (isSelected) BackgroundGreen
                                        else Color.White
                                    )
                                ) {
                                    Column(
                                        modifier = Modifier.padding(20.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Text(
                                            text = if (value) "✅" else "❌",
                                            fontSize = 36.sp
                                        )
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Text(
                                            text = label,
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = if (isSelected) ArogyaGreenDark
                                            else TextDark,
                                            textAlign = TextAlign.Center,
                                            fontWeight = if (isSelected) FontWeight.SemiBold
                                            else FontWeight.Normal
                                        )
                                    }
                                }
                            }
                        }
                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = Color(0xFFFFF8E1)
                            ),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.Top
                            ) {
                                Icon(
                                    Icons.Default.Info,
                                    contentDescription = null,
                                    tint = ArogyaGold,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "BPL (Below Poverty Line) card is issued by state governments. Having it may unlock additional benefits.",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color(0xFF5D4037)
                                )
                            }
                        }
                    }

                    QuestionType.STATE_PICKER -> {
                        var expanded by remember { mutableStateOf(false) }
                        ExposedDropdownMenuBox(
                            expanded = expanded,
                            onExpandedChange = { expanded = !expanded }
                        ) {
                            OutlinedTextField(
                                value = selectedState,
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Select State") },
                                leadingIcon = {
                                    Icon(
                                        Icons.Default.LocationOn,
                                        null,
                                        tint = ArogyaGreen
                                    )
                                },
                                trailingIcon = {
                                    ExposedDropdownMenuDefaults.TrailingIcon(
                                        expanded = expanded
                                    )
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .menuAnchor(MenuAnchorType.PrimaryNotEditable),
                                shape = RoundedCornerShape(12.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = ArogyaGreen,
                                    focusedLabelColor = ArogyaGreen
                                )
                            )
                            ExposedDropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false }
                            ) {
                                question.options.forEach { state ->
                                    DropdownMenuItem(
                                        text = { Text(state) },
                                        onClick = {
                                            selectedState = state
                                            expanded = false
                                            errorText = ""
                                        }
                                    )
                                }
                            }
                        }
                    }

                    QuestionType.NUMBER_INPUT -> {
                        OutlinedTextField(
                            value = familySize,
                            onValueChange = { familySize = it; errorText = "" },
                            label = { Text("Number of Family Members") },
                            leadingIcon = {
                                Icon(
                                    Icons.Default.Group,
                                    null,
                                    tint = ArogyaGreen
                                )
                            },
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Number
                            ),
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = ArogyaGreen,
                                focusedLabelColor = ArogyaGreen
                            ),
                            placeholder = { Text("e.g. 4") },
                            singleLine = true
                        )
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            listOf("1", "2", "3", "4", "5", "6+").forEach { hint ->
                                SuggestionChip(
                                    onClick = {
                                        familySize = if (hint == "6+") "6" else hint
                                        errorText = ""
                                    },
                                    label = { Text(hint) },
                                    colors = SuggestionChipDefaults.suggestionChipColors(
                                        containerColor = BackgroundGreen
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }

        // ── Error + Navigation ──────────────────────────────────
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(horizontal = 24.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            if (errorText.isNotEmpty()) {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFFFEBEE)
                    ),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.Warning,
                            null,
                            tint = ErrorRed,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            errorText,
                            color = ErrorRed,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (currentStep > 0) {
                    OutlinedButton(
                        onClick = { currentStep--; errorText = "" },
                        modifier = Modifier
                            .weight(0.4f)
                            .height(52.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = ArogyaGreen
                        )
                    ) {
                        Icon(
                            Icons.Default.ArrowBack,
                            null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Back")
                    }
                }
                Button(
                    onClick = { validateAndNext() },
                    modifier = Modifier
                        .weight(1f)
                        .height(52.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ArogyaGreen
                    )
                ) {
                    if (currentStep == questions.size - 1) {
                        Icon(
                            Icons.Default.CheckCircle,
                            null,
                            modifier = Modifier.size(18.dp),
                            tint = Color.White
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            "Check Eligibility",
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    } else {
                        Text(
                            "Next",
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Icon(
                            Icons.Default.ArrowForward,
                            null,
                            modifier = Modifier.size(18.dp),
                            tint = Color.White
                        )
                    }
                }
            }
        }
    }
}