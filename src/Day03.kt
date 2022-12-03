fun main() {
    fun part1(input: List<String>): Int {
        return input
            .map { it.getCommonItem() }
            .sumOf { it.getPriority() }
    }

    fun part2(input: List<String>): Int {
        return input.withIndex()
            .groupBy(keySelector = { it.index / 3 }, valueTransform = { it.value })
            .values
            .map { it.getCommonItem() }
            .sumOf { it.getPriority() }
    }

    val input = readInput("Day03")
    println(part1(input))
    println(part2(input))
}

private fun String.getCommonItem(): Char {
    val firstHalf = this.take(this.length / 2).toSet()
    val secondHalf = this.takeLast(this.length / 2).toSet()
    return firstHalf.intersect(secondHalf).first()
}

private fun Iterable<String>.getCommonItem(): Char =
    this.map { it.toSet() }.reduce { acc, curr -> acc.intersect(curr) }.first()

private fun Char.getPriority(): Int {
    val basePriority = 1 + ('z' - 'a') - ('z' - this.lowercaseChar())

    if (this.isUpperCase()) {
        return 26 + basePriority
    }

    return basePriority
}