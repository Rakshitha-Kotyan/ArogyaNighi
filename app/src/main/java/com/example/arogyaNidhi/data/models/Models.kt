package com.example.arogyaNidhi.data.models

import com.google.gson.annotations.SerializedName

data class Scheme(
    val id: String,
    val name: String,
    val description: String,
    @SerializedName("benefit_amount") val benefitAmount: String,
    val eligibility: EligibilityRule,
    val documents: List<String>,
    val color: String,
    val icon: String
)

data class EligibilityRule(
    @SerializedName("max_income") val maxIncome: Long,
    @SerializedName("bpl_required") val bplRequired: Boolean,
    val occupations: List<String>,
    val states: List<String>,
    @SerializedName("max_family_size") val maxFamilySize: Int,
    val special: String? = null
)

data class Hospital(
    val id: Int,
    val name: String,
    val district: String,
    val pincode: String,
    val address: String,
    val phone: String,
    @SerializedName("schemes_accepted") val schemesAccepted: List<String>,
    val specialities: List<String>,
    val type: String
)

data class UserInput(
    val income: Long = 0L,
    val occupation: String = "",
    val bplStatus: Boolean = false,
    val state: String = "",
    val familySize: Int = 1
)

data class QuizQuestion(
    val id: Int,
    val title: String,
    val subtitle: String,
    val type: QuestionType,
    val options: List<String> = emptyList()
)

enum class QuestionType {
    INCOME_INPUT, SINGLE_CHOICE, BOOLEAN_CHOICE, STATE_PICKER, NUMBER_INPUT
}

data class AppUser(
    val username: String,
    val email: String,
    val password: String
)