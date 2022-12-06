fun main() {
    fun part1(input: String): Int = findMarker(input, numOfDistinctChars = 4)

    fun part2(input: String): Int = findMarker(input, numOfDistinctChars = 14)

    val input = readInput("Day06")
    println(part1(input))
    println(part2(input))
}

fun findMarker(input: String, numOfDistinctChars: Int): Int {
    val charsInWindow = mutableSetOf<Char>()
    var left = 0

    for (right in input.indices) {
        while (input[right] in charsInWindow) {
            charsInWindow -= input[left++]
        }

        charsInWindow += input[right]

        if (charsInWindow.size == numOfDistinctChars) {
            return right + 1
        }
    }

    return -1
}
