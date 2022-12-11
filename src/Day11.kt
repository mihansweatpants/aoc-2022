import java.util.LinkedList

fun main() {
    fun part1(monkeys: List<Monkey>): Long {
        repeat(20) {
            for (monkey in monkeys) {
                while (monkey.hasItems()) {
                    val (item, throwTo) = monkey.inspectAndThrowAnItem()
                    monkeys[throwTo].catchItem(item)
                }
            }
        }

        return monkeys
            .map { it.totalItemsInspected() }
            .sortedDescending()
            .take(2)
            .reduce(Long::times)
    }

    fun part2(monkeys: List<Monkey>): Long {
        val divisor = monkeys.map { it.worryLevelDivisor() }.reduce(Int::times)
        repeat(10_000) {
            for (monkey in monkeys) {
                while (monkey.hasItems()) {
                    val (item, throwTo) = monkey.inspectAndThrowAnItem { it % divisor }
                    monkeys[throwTo].catchItem(item)
                }
            }
        }

        return monkeys
            .map { it.totalItemsInspected() }
            .sortedDescending()
            .take(2)
            .reduce(Long::times)
    }

    val input = (readInput("Day11"))

    println(part1(parseInputMonkeys(input)))
    println(part2(parseInputMonkeys(input)))
}

private class Monkey(
    private val items: LinkedList<Long>,
    private val worryLevelFunction: (worryLevel: Long) -> Long,
    private val worryLevelDivisor: Int,
    private val throwStuffTo: Pair<Int, Int>,
    private var inspectedItemsCount: Long = 0
) {
    fun hasItems(): Boolean =
        items.isNotEmpty()

    fun inspectAndThrowAnItem(calmDown: (worryLevel: Long) -> Long = { it / 3 }): Pair<Long, Int> {
        val oldWorryLevel = items.removeFirst()
        val newWorryLevel = calmDown(worryLevelFunction(oldWorryLevel))

        inspectedItemsCount++

        return if (newWorryLevel % worryLevelDivisor == 0L) {
            Pair(newWorryLevel, throwStuffTo.first)
        } else {
            Pair(newWorryLevel, throwStuffTo.second)
        }
    }

    fun catchItem(item: Long) {
        items.addLast(item)
    }

    fun totalItemsInspected(): Long =
        inspectedItemsCount

    fun worryLevelDivisor(): Int =
        worryLevelDivisor
}

private fun parseInputMonkeys(input: String): List<Monkey> {
    val monkeyConfigs = mutableListOf<MutableList<String>>()

    monkeyConfigs.add(mutableListOf())

    for (line in input.lines()) {
        if (line.isBlank()) {
            monkeyConfigs.add(mutableListOf())
        } else {
            monkeyConfigs.last().add(line)
        }
    }

    return monkeyConfigs.map { it.parseMonkey() }
}

private fun List<String>.parseMonkey(): Monkey {
    val (startingItems, operation, test, ifTrue, ifFalse) = this.drop(1)

    val items = LinkedList<Long>()

    startingItems
        .slice(startingItems.indexOf(":") + 2..startingItems.lastIndex)
        .split(",")
        .map { it.trim() }
        .filterNot { it.isBlank() }
        .mapTo(items) { it.toLong() }

    val (operator, argument) = operation.slice(operation.indexOf("old") + 4..operation.lastIndex)
        .split(" ")

    val worryLevelFunction = createWorryLevelFunction(operator, argument)

    val worryLevelDivisor = test.substring(test.lastIndexOf(" ") + 1).toInt()

    val leftMonkey = ifTrue.substring(ifTrue.lastIndexOf(" ") + 1).toInt()
    val rightMonkey = ifFalse.substring(ifFalse.lastIndexOf(" ") + 1).toInt()

    return Monkey(
        items,
        worryLevelFunction,
        worryLevelDivisor,
        leftMonkey to rightMonkey
    )
}

private fun createWorryLevelFunction(operator: String, argument: String): (old: Long) -> Long {
    return {
        val arg = if (argument == "old") it else argument.toLong()

        when(operator) {
            "*" -> arg * it
            "+" -> arg + it
            else -> throw IllegalArgumentException("Unknown operator $operator")
        }
    }
}