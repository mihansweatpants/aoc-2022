fun main() {
    fun part1(input: List<String>): String {
        val (stacks, moves) = parseInput(input)

        for (move in moves) {
            val fromStack = stacks[move.from]!!
            val toStack = stacks[move.to]!!
            repeat(move.amount) {
                toStack += fromStack.removeLast()
            }
        }

        return stacks.keys.sorted()
            .filter { stacks[it]?.isNotEmpty() ?: false }
            .joinToString("") { stacks[it]?.last().toString() }
    }

    fun part2(input: List<String>): Int {
        TODO()
    }

    val input = readInput("Day05")
    println(part1(input))
    println(part2(input))
}

private fun parseInput(input: List<String>): Pair<MutableMap<Int, MutableList<Char>>, List<Move>> {
    val splitIndex = input.indexOfFirst { it.isBlank() }

    val stackNumberConfig = input[splitIndex - 1]
    val stackConfig = input.slice(0 until splitIndex - 1).asReversed()

    val stacks = mutableMapOf<Int, MutableList<Char>>()

    for (line in stackConfig) {
        var currIndex = 1
        while (currIndex < line.length) {
            val stack = stacks.computeIfAbsent(stackNumberConfig[currIndex].digitToInt()) { mutableListOf() }
            if (line[currIndex] != ' ') {
                stack.add(line[currIndex])
            }
            currIndex += 4
        }
    }

    val movesConfig = input.takeLast(input.size - splitIndex - 1)

    val moves = movesConfig.map { it.parseMove() }

    return Pair(stacks, moves)
}

private data class Move(
    val amount: Int,
    val from: Int,
    val to: Int
)

private val numsRegex = Regex("[0-9]+")

private fun String.parseMove(): Move {
    val (amount, from, to) = numsRegex.findAll(this)
        .map(MatchResult::value)
        .toList()

    return Move(amount = amount.toInt(), from = from.toInt(), to = to.toInt())
}