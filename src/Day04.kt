fun main() {
    fun part1(input: List<String>): Int {
        return input.stream()
            .map { it.parseRegionPair() }
            .filter { (first, second) -> first.fullyContains(second) || second.fullyContains(first) }
            .count()
            .toInt()
    }

    fun part2(input: List<String>): Int {
        return input.stream()
            .map { it.parseRegionPair() }
            .filter { (first, second) -> first.overlaps(second) }
            .count()
            .toInt()
    }

    val input = readInput("Day04").lines()
    println(part1(input))
    println(part2(input))
}

private fun String.parseRegionPair(): Pair<IntRange, IntRange> {
    val (first, second) = this.split(",")

    return first.parseRegion() to second.parseRegion()
}

private fun String.parseRegion(): IntRange {
    val (start, end) = this.split("-")

    return start.toInt()..end.toInt()
}

private fun IntRange.fullyContains(other: IntRange): Boolean {
    return this.contains(other.first) && this.contains(other.last)
}

private fun IntRange.overlaps(other: IntRange): Boolean {
    if (other.first < this.first) return other.overlaps(this)

    return other.first <= this.last
}