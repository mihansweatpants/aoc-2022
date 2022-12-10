import java.lang.IllegalArgumentException
import kotlin.math.abs

fun main() {
    fun part1(moves: List<RopeMove>): Int {
        val visitedPositions = mutableSetOf<RopePosition>()

        var headRopePosition = RopePosition(0, 0)
        var tailRopePosition = RopePosition(0, 0)

        visitedPositions.add(tailRopePosition)

        for ((direction, steps) in moves) {
            repeat(steps) {
                headRopePosition = headRopePosition.move(direction)
                tailRopePosition = tailRopePosition.catchUp(headRopePosition)
                visitedPositions.add(tailRopePosition)
            }
        }

        return visitedPositions.size
    }

    fun part2(input: List<RopeMove>): Int {
        return input.size
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

private data class RopePosition(
    val x: Int,
    val y: Int
) {
    fun move(direction: String, steps: Int = 1): RopePosition {
        return when (direction) {
            "U" -> this.copy(y = y + steps)
            "D" -> this.copy(y = y - steps)
            "L" -> this.copy(x = x - steps)
            "R" -> this.copy(x = x + steps)
            else -> throw IllegalArgumentException("Unknown direction $direction")
        }
    }

    fun catchUp(other: RopePosition): RopePosition {
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

    fun catchUpDiagonally(other: RopePosition): List<RopeMove> {
        val toX = catchUpHorizontally(other)
        val toY = catchUpVertically(other)
        return if (toX.steps < toY.steps) {
            listOf(
                toX.copy(steps = toX.steps + 1),
                toY
            )
        } else {
            listOf(
                toX,
                toY.copy(steps = toY.steps + 1)
            )
        }
    }

    fun catchUpHorizontally(other: RopePosition): RopeMove {
        val direction = if (other.x > this.x) "R" else "L"
        var distance = abs(this.x - other.x)
        if (distance > 0) distance -= 1

        return RopeMove(direction, distance)
    }

    fun catchUpVertically(other: RopePosition): RopeMove {
        val direction = if (other.y > this.y) "U" else "D"
        var distance = abs(this.y - other.y)
        if (distance > 0) distance -= 1

        return RopeMove(direction, distance)
    }
}