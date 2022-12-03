import java.lang.IllegalArgumentException

fun main() {
    fun part1(input: List<String>): Int {
        var totalPoints = 0

        for (line in input) {
            val opponent = PlayerChoice.fromCode(line.first())
            val me = PlayerChoice.fromCode(line.last())

            totalPoints += me.playAgainst(opponent)
        }

        return totalPoints
    }

    fun part2(input: List<String>): Int {
        var totalPoints = 0

        for (line in input) {
            val opponent = PlayerChoice.fromCode(line.first())
            val me = when (line.last()) {
                'X' -> opponent.beats
                'Y' -> opponent
                'Z' -> opponent.loses
                else -> throw IllegalArgumentException("Unknown code ${line.last()}")
            }

            totalPoints += me.playAgainst(opponent)
        }

        return totalPoints
    }

    val input = readInput("Day02")
    println(part1(input))
    println(part2(input))
}

enum class PlayerChoice(private val weight: Int) {
    ROCK(1),
    PAPER(2),
    SCISSORS(3);

    companion object {
        fun fromCode(code: Char): PlayerChoice {
            return when (code) {
                'A', 'X' -> ROCK
                'B', 'Y' -> PAPER
                'C', 'Z' -> SCISSORS
                else -> throw IllegalArgumentException("Unknown code $code")
            }
        }
    }

    val beats: PlayerChoice
        get() = when(this) {
            ROCK -> SCISSORS
            PAPER -> ROCK
            SCISSORS -> PAPER
        }

    val loses: PlayerChoice
        get() = when(this) {
            ROCK -> PAPER
            PAPER -> SCISSORS
            SCISSORS -> ROCK
        }

    fun playAgainst(opponentChoice: PlayerChoice): Int {
        return if (this.beats == opponentChoice) {
            this.weight + 6
        } else if (this == opponentChoice) {
            return this.weight + 3
        } else {
            this.weight
        }
    }
}