import java.lang.IllegalStateException
import kotlin.math.abs

fun main() {
    fun part1(instructions: List<CPUInstruction>): Int {
        val interestingCycles = setOf(20, 60, 100, 140, 180, 220)
        var sumOfInterestingSignalStrengths = 0

        var register = 1
        var currentCycle = 0

        for (instruction in instructions) {
            repeat(instruction.cyclesToComplete) {
                currentCycle++
                if (currentCycle in interestingCycles) {
                    sumOfInterestingSignalStrengths += currentCycle * register
                }
            }

            when (instruction.operator) {
                "addx" -> register += instruction.argument
            }
        }

        return sumOfInterestingSignalStrengths
    }

    fun part2(instructions: List<CPUInstruction>): String {
        val crtRows = mutableListOf<List<String>>()

        var register = 1
        var currentCycle = 0

        var currentCrtRow = mutableListOf<String>()
        for (instruction in instructions) {
            repeat(instruction.cyclesToComplete) {
                val currentPixel = currentCrtRow.size
                if (abs(register - currentPixel) <= 1) {
                    currentCrtRow.add("#")
                } else {
                    currentCrtRow.add(".")
                }

                if (currentCrtRow.size == 40) {
                    crtRows.add(currentCrtRow)
                    currentCrtRow = mutableListOf()
                }

                currentCycle++
            }

            when (instruction.operator) {
                "addx" -> register += instruction.argument
            }
        }

        return crtRows.joinToString("\n") {
            it.joinToString()
                .replace(".", " ")
                .replace(",", " ")
        }
    }

    val input = readInput("Day10")
        .lines()
        .map { it.parseInstruction() }

    println(part1(input))
    println(part2(input))
}

private class CPUInstruction(
    val operator: String,
    val argument: Int = 0
) {
    val cyclesToComplete: Int
        get() = when (operator) {
            "addx" -> 2
            "noop" -> 1
            else -> throw IllegalStateException("Unknown operator $operator")
        }
}

private fun String.parseInstruction(): CPUInstruction {
    val components = this.split(" ")

    return if (components.size == 1) {
        CPUInstruction(components[0])
    } else {
        CPUInstruction(components[0], components[1].toInt())
    }
}