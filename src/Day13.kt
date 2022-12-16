import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.*
import java.lang.RuntimeException

fun main() {
    fun part1(input: String): Int {
        val packetPairs = input
            .split("\n\n")
            .map { it.split("\n") }
            .map { (left, right) -> Packet(Json.decodeFromString(left)) to Packet(Json.decodeFromString(right)) }

        return packetPairs
            .mapIndexed { index, (left, right) -> index + 1 to left.compareTo(right) }
            .mapNotNull { (index, check) -> if (check <= 0) index else null }
            .sum()
    }

    val input = readInput("Day13")

    println(part1(input))
}

private data class Packet(
    val payload: JsonArray
) : Comparable<Packet> {

    override fun compareTo(other: Packet): Int {
        return comparePayload(this.payload, other.payload)
    }

    private fun comparePayload(left: Any, right: Any): Int {
        return when {
            left is JsonPrimitive && right is JsonPrimitive -> left.int - right.int

            left is JsonArray && right is JsonPrimitive -> comparePayload(left, buildJsonArray { add(right) })

            left is JsonPrimitive && right is JsonArray -> comparePayload(buildJsonArray { add(left) }, right)

            left is JsonArray && right is JsonArray -> {
                if (left.isEmpty() && right.isEmpty()) return 0

                var index = 0
                while (index < minOf(left.size, right.size)) {
                    val check = comparePayload(left[index], right[index])
                    if (check != 0) return check
                    index++
                }

                if (left.size > right.size) return 1

                return -1
            }

            else -> throw RuntimeException("Left is ${left.javaClass} and right is ${right.javaClass}. What do i do???")
        }
    }
}
