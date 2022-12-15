import java.lang.RuntimeException

fun main() {
    val input = readInput("Day12").lines()

    fun part1(input: List<String>): Int {
        return shortestPathLength(input, getPoint(input, 'S'), getPoint(input, 'E'))
            ?: throw RuntimeException("Path not found")
    }

    fun part2(input: List<String>): Int {
        return getPoints(input, setOf('a', 'S'))
            .mapNotNull { shortestPathLength(input, it, getPoint(input, 'E')) }
            .minOrNull() ?: throw RuntimeException("Path not found")
    }

    println(part1(input))
    println(part2(input))
}

private fun shortestPathLength(input: List<String>, startPoint: Point, endPoint: Point): Int? {
    val visited = mutableSetOf<Point>()
    val queue = ArrayDeque<Pair<Point, Int>>().apply { add(startPoint to 0) }

    while (queue.isNotEmpty()) {
        val (currentPoint, currentStepsTaken) = queue.removeFirst()

        if (currentPoint in visited) continue
        visited.add(currentPoint)

        if (currentPoint == endPoint) {
            return currentStepsTaken
        }

        currentPoint.getAdjacentPoints(input).forEach { adjacentPoint ->
            if (currentPoint.canClimb(adjacentPoint) && adjacentPoint !in visited) {
                queue.add(adjacentPoint to currentStepsTaken + 1)
            }
        }
    }

    return null
}

private data class Point(
    val x: Int,
    val y: Int,
    val elevation: Char
) {
    fun getHeight(): Int {
        return when (elevation) {
            'E' -> 'z'
            'S' -> 'a'
            else -> elevation
        }.code
    }

    fun canClimb(to: Point) =
        to.getHeight() <= getHeight() + 1

    fun getAdjacentPoints(grid: List<String>): List<Point> {
        val maxX = grid[0].lastIndex
        val maxY = grid.lastIndex
        return mutableListOf<Point>().apply {
            if (x + 1 <= maxX) add(copy(x = x + 1, elevation = grid[y][x + 1]))
            if (x - 1 >= 0) add(copy(x = x - 1, elevation = grid[y][x - 1]))
            if (y + 1 <= maxY) add(copy(y = y + 1, elevation = grid[y + 1][x]))
            if (y - 1 >= 0) add(copy(y = y - 1, elevation = grid[y - 1][x]))
        }
    }
}

private fun getPoint(input: List<String>, target: Char): Point =
    getPoints(input, setOf(target)).first()

private fun getPoints(input: List<String>, targets: Set<Char>): List<Point> =
    input.flatMapIndexed { y, row ->
        row.mapIndexedNotNull { x, char ->
            if (char in targets) Point(x, y, char) else null
        }
    }
