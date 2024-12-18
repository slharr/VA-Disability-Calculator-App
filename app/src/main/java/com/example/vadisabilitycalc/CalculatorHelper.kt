package com.example.vadisabilitycalc

import java.util.*

class CalculatorHelper {

    data class Disability(val name: String, val percentage: Double)

    fun calculateFinalValue(sortedDisabilities: List<Disability>): Double {
        if (sortedDisabilities.isEmpty()) {
            println("Error: Input is empty.")
            return 0.0 // Return a default value (adjust as needed)
        }
        var finalValue = sortedDisabilities[0].percentage
        var remainder = 100.0 - finalValue
        for (i in 1 until sortedDisabilities.size) {
            val percentage = sortedDisabilities[i].percentage / 100.0
            finalValue += percentage * remainder
            remainder = 100.0 - finalValue
        }
        return finalValue
    } // Function to calculate the final value

    // this can be removed ***  NOTE ***
    fun notNecessary() {
        val disabilities = mutableListOf<Disability>()
        val scanner = Scanner(System.`in`)
        // Input loop
        while (true) {
            print("Enter the disability name (or 'x' to finish): ")
            val name = scanner.nextLine().lowercase(Locale.getDefault())
            if (name.isEmpty()) {
                println("Please enter a valid disability description.")
                continue
            }
            if (name == "x") {
                break
            }
            print("Enter the percentage (decimal allowed): ")
            val percentage = scanner.nextDouble()
            scanner.nextLine() // Consume the newline
            disabilities.add(Disability(name, percentage))
        }
        // REMOVE ABOVE *** NOTE ***

        fun sorter(): Double {
            disabilities.sortByDescending { it.percentage }
            val finalValue = calculateFinalValue(disabilities)
            return finalValue
            // Sort the disabilities by percentage in descending order
        }

        // Print names and percentages
        println("Disability\tPercentage")
        for (disability in disabilities) {
            println("${disability.name}\t${disability.percentage}")
        }
    }
}