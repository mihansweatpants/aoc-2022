import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.*

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

    fun part2(input: String): Int {
        val dividerPackets = setOf(
            Packet(Json.decodeFromString("[[2]]")),
            Packet(Json.decodeFromString("[[6]]"))
        )

        val packets = mutableListOf<Packet>().apply {
            addAll(dividerPackets)
        }

        input.lines()
            .filterNot { it.isBlank() }
            .mapTo(packets) { Packet(Json.decodeFromString(it)) }

        packets.sort()

        return dividerPackets
            .map { packets.indexOf(it) + 1 }
            .reduce(Int::times)
    }

    val input = readInput("Day13")

    part1(input).also {
        check(it == 5198)
        println(it)
    }

    part2(input).also {
        check(it == 22344)
        println(it)
    }
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

                return left.size - right.size
            }

            else -> throw RuntimeException("Left is ${left.javaClass} and right is ${right.javaClass}. What do i do???")
        }
    }
}
