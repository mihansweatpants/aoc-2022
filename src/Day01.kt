import java.util.*
import kotlin.math.max

fun main() {
    fun part1(input: List<String>): Int {
        var maxCalories = 0
        var currentElfCalories = 0

        for (line in input) {
            if (line.isBlank()) {
                maxCalories = max(maxCalories, currentElfCalories)
                currentElfCalories = 0
            } else {
                currentElfCalories += line.toInt()
            }
        }

        return maxCalories
    }

    fun part2(input: List<String>): Int {
        val heap = PriorityQueue<Int>()

        var currentElfCalories = 0
        for (line in input) {
            if (line.isNotBlank()) {
                currentElfCalories += line.toInt()
            } else {
                heap.offer(currentElfCalories)
                currentElfCalories = 0
            }

            if (heap.size > 3) {
                heap.poll()
            }
        }

        return heap.sum()
    }

    val input = readInput("Day01").lines()
    println(part1(input))
    println(part2(input))
}
