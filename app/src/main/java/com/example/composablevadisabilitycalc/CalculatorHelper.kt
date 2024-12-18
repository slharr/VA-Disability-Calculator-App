package com.example.composablevadisabilitycalc

import kotlin.math.ceil
import kotlin.math.floor

class CalculatorHelper {
    fun calculateFinalValue(sortedDisabilities: List<Disability>): Double {
        if (sortedDisabilities.isEmpty()) {
            println("Error: Input is empty.")
            return 0.0 // Return a default value (adjust as needed)
        }
        var finalValue = sortedDisabilities[0].percentage
        // Capture the first value in the array after sorting them
        var remainder = 100.0 - finalValue
        // Use this as a value holder as the remainder depreciates along the way.
        for (i in 1 until sortedDisabilities.size) {
            val percentage = sortedDisabilities[i].percentage / 100.0
            finalValue += percentage * remainder
            remainder = 100.0 - finalValue
        }  // Step through each value in the array and calculate.
        // Don't forget to round the final value and display it as finalFinal,
        // that can be done in a separate function below
        return finalValue
    } // End of function calculateFinalValue

    // Function to round a double to the nearest 10th increment
    fun roundToNearestTenth(value: Double): Double {

        return if (value % 10 < 5 ){
            floor( value / 10) * 10
        } else {
            ceil(value / 10.0) * 10.0
        }
    } // End of function roundToNearestTenth


} // End of CalculatorHelper class

data class Disability(
    val id: Long,
    val name: String,
    val percentage: Double
)
