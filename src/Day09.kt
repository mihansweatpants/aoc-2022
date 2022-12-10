import java.lang.IllegalArgumentException
import kotlin.math.abs

fun main() {
    fun part1(moves: List<RopeMove>): Int {
        val visitedPositions = mutableSetOf<KnotPosition>()

        var headKnotPosition = KnotPosition(0, 0)
        var tailKnotPosition = KnotPosition(0, 0)

        visitedPositions.add(tailKnotPosition)

        for ((direction, steps) in moves) {
            repeat(steps) {
                headKnotPosition = headKnotPosition.move(direction)
                tailKnotPosition = tailKnotPosition.catchUp(headKnotPosition)
                visitedPositions.add(tailKnotPosition)
            }
        }

        return visitedPositions.size
    }

    fun part2(moves: List<RopeMove>): Int {
        val visitedPositions = mutableSetOf<KnotPosition>()

        val rope = Array(10) { KnotPosition(0, 0) }
        visitedPositions.add(rope.last())

        for ((direction, steps) in moves) {
            repeat(steps) {
                rope[0] = rope[0].move(direction)
                for (i in 1..rope.lastIndex) {
                    rope[i] = rope[i].catchUp(rope[i - 1])
                }
                visitedPositions.add(rope.last())
            }
        }

        return visitedPositions.size
    }

    val input = readInput("Day09")
        .lines()
        .map { it.split(" ") }
        .map { (direction, steps) -> RopeMove(direction, steps.toInt()) }

    println(part1(input))
    println(part2(input))
}

private data class RopeMove(
    val direction: String,
    val steps: Int
)

private data class KnotPosition(
    val x: Int,
    val y: Int
) {
    fun move(direction: String, steps: Int = 1): KnotPosition {
        return when (direction) {
            "U" -> this.copy(y = y + steps)
            "D" -> this.copy(y = y - steps)
            "L" -> this.copy(x = x - steps)
            "R" -> this.copy(x = x + steps)
            else -> throw IllegalArgumentException("Unknown direction $direction")
        }
    }

    fun catchUp(other: KnotPosition): KnotPosition {
        val xDiff = abs(this.x - other.x)
        val yDiff = abs(this.y - other.y)

        val moves = if (xDiff == 0 && yDiff >= 2) {
            listOf(this.catchUpVertically(other))
        } else if (xDiff >= 2 && yDiff == 0) {
            listOf(this.catchUpHorizontally(other))
        } else if (xDiff >= 2 || yDiff >= 2) {
            catchUpDiagonally(other)
        } else {
            emptyList()
        }

        var newPosition = this

        for ((direction, steps) in moves) {
            newPosition = newPosition.move(direction, steps)
        }

        return newPosition
    }

    fun catchUpDiagonally(other: KnotPosition): List<RopeMove> {
        var toX = catchUpHorizontally(other)
        var toY = catchUpVertically(other)

        if (toX.steps < toY.steps) {
            toX = toX.copy(steps = toX.steps + 1)
        } else if (toX.steps > toY.steps) {
            toY = toY.copy(steps = toY.steps + 1)
        }

        return listOf(toX, toY)
    }

    fun catchUpHorizontally(other: KnotPosition): RopeMove {
        val direction = if (other.x > this.x) "R" else "L"
        var distance = abs(this.x - other.x)
        if (distance > 0) distance -= 1

        return RopeMove(direction, distance)
    }

    fun catchUpVertically(other: KnotPosition): RopeMove {
        val direction = if (other.y > this.y) "U" else "D"
        var distance = abs(this.y - other.y)
        if (distance > 0) distance -= 1

        return RopeMove(direction, distance)
    }
}