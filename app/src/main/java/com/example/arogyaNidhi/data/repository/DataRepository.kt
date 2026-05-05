package com.example.arogyaNidhi.data.repository

import android.content.Context
import com.example.arogyaNidhi.data.models.Hospital
import com.example.arogyaNidhi.data.models.QuizQuestion
import com.example.arogyaNidhi.data.models.QuestionType
import com.example.arogyaNidhi.data.models.Scheme
import com.example.arogyaNidhi.data.models.UserInput
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class DataRepository(private val context: Context) {

    private val gson = Gson()

    fun loadSchemes(): List<Scheme> {
        return try {
            val json = context.assets.open("schemes.json")
                .bufferedReader()
                .use { it.readText() }
            val type = object : TypeToken<List<Scheme>>() {}.type
            gson.fromJson(json, type) ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }

    fun loadHospitals(): List<Hospital> {
        return try {
            val json = context.assets.open("hospitals.json")
                .bufferedReader()
                .use { it.readText() }
            val type = object : TypeToken<List<Hospital>>() {}.type
            gson.fromJson(json, type) ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }

    fun checkEligibility(userInput: UserInput, schemes: List<Scheme>): List<Scheme> {
        return schemes.filter { scheme ->
            val rule = scheme.eligibility
            val incomeOk = userInput.income <= rule.maxIncome
            val bplOk = if (rule.bplRequired) userInput.bplStatus else true
            val occupationOk = rule.occupations.contains("all") ||
                    rule.occupations.contains(userInput.occupation)
            val stateOk = rule.states.contains("all") ||
                    rule.states.contains(userInput.state)
            incomeOk && bplOk && occupationOk && stateOk
        }
    }

    fun filterHospitals(hospitals: List<Hospital>, query: String): List<Hospital> {
        if (query.isBlank()) return hospitals
        return hospitals.filter { hospital ->
            hospital.district.contains(query, ignoreCase = true) ||
                    hospital.name.contains(query, ignoreCase = true) ||
                    hospital.pincode.contains(query, ignoreCase = true)
        }
    }

    fun getQuizQuestions(): List<QuizQuestion> {
        return listOf(
            QuizQuestion(
                id = 1,
                title = "Annual Family Income",
                subtitle = "Enter your total yearly household income",
                type = QuestionType.INCOME_INPUT
            ),
            QuizQuestion(
                id = 2,
                title = "Occupation",
                subtitle = "What is the primary occupation of the earning member?",
                type = QuestionType.SINGLE_CHOICE,
                options = listOf(
                    "Daily Wage Worker",
                    "Farmer",
                    "Construction Worker",
                    "Domestic Worker",
                    "Street Vendor",
                    "Factory/Industrial Worker",
                    "Government Employee",
                    "Other"
                )
            ),
            QuizQuestion(
                id = 3,
                title = "BPL Card Status",
                subtitle = "Do you have a Below Poverty Line (BPL) card?",
                type = QuestionType.BOOLEAN_CHOICE
            ),
            QuizQuestion(
                id = 4,
                title = "Your State",
                subtitle = "Select the state where you currently reside",
                type = QuestionType.STATE_PICKER,
                options = listOf(
                    "Andhra Pradesh", "Arunachal Pradesh", "Assam", "Bihar",
                    "Chhattisgarh", "Goa", "Gujarat", "Haryana", "Himachal Pradesh",
                    "Jharkhand", "Karnataka", "Kerala", "Madhya Pradesh", "Maharashtra",
                    "Manipur", "Meghalaya", "Mizoram", "Nagaland", "Odisha", "Punjab",
                    "Rajasthan", "Sikkim", "Tamil Nadu", "Telangana", "Tripura",
                    "Uttar Pradesh", "Uttarakhand", "West Bengal",
                    "Delhi", "Jammu & Kashmir", "Ladakh"
                )
            ),
            QuizQuestion(
                id = 5,
                title = "Family Size",
                subtitle = "How many members are there in your family?",
                type = QuestionType.NUMBER_INPUT
            )
        )
    }

    fun mapOccupation(displayName: String): String {
        return when (displayName) {
            "Daily Wage Worker" -> "daily_wage"
            "Farmer" -> "farmer"
            "Construction Worker" -> "construction_worker"
            "Domestic Worker" -> "domestic_worker"
            "Street Vendor" -> "street_vendor"
            "Factory/Industrial Worker" -> "factory_worker"
            "Government Employee" -> "government_employee"
            else -> "other"
        }
    }
}