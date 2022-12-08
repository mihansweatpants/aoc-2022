fun main() {
    fun part1(grid: List<List<Int>>): Int {
        var visibleTreesCount = 0

        for (rowIndex in grid.indices) {
            for (columnIndex in grid[rowIndex].indices) {
                if (isExteriorCell(rowIndex, columnIndex, grid)) {
                    visibleTreesCount++
                } else {
                    val currCell = grid[rowIndex][columnIndex]
                    val maxFromUp = grid.getMaxInColumn(columnIndex, 0, rowIndex - 1)
                    val maxFromDown = grid.getMaxInColumn(columnIndex, rowIndex + 1, grid.lastIndex)
                    val maxFromLeft = grid.getMaxInRow(rowIndex, 0, columnIndex - 1)
                    val maxFromRight = grid.getMaxInRow(rowIndex, columnIndex + 1, grid[rowIndex].lastIndex)
                    if (minOf(maxFromUp, maxFromDown, maxFromLeft, maxFromRight) < currCell) {
                        visibleTreesCount++
                    }
                }
            }
        }

        return visibleTreesCount
    }

    fun part2(grid: List<List<Int>>): Int {
        var highestScore = 0

        for (rowIndex in grid.indices) {
            for (columnIndex in grid.indices) {
                if (isExteriorCell(rowIndex, columnIndex, grid)) continue

                val up = grid.countVisibleTreesInColumn(rowIndex, columnIndex, rowIndex - 1 downTo 0)
                val down = grid.countVisibleTreesInColumn(rowIndex, columnIndex, rowIndex + 1..grid.lastIndex)
                val left = grid.countVisibleTreesInRow(rowIndex, columnIndex, columnIndex - 1 downTo 0)
                val right = grid.countVisibleTreesInRow(rowIndex, columnIndex, columnIndex + 1..grid[rowIndex].lastIndex)

                highestScore = maxOf(highestScore, up * down * left * right)
            }
        }

        return highestScore
    }

    val input = readInput("Day08")
        .lines()
        .map {
            it.split("")
                .filterNot(String::isBlank)
                .map(String::toInt)
        }

    println(part1(input))
    println(part2(input))
}

private fun isExteriorCell(rowIndex: Int, columnIndex: Int, grid: List<List<Int>>): Boolean {
    return rowIndex == grid.indices.first ||
            rowIndex == grid.indices.last ||
            columnIndex == grid[rowIndex].indices.first ||
            columnIndex == grid[rowIndex].indices.last
}

private fun List<List<Int>>.getMaxInRow(rowIndex: Int, leftBound: Int, rightBound: Int): Int {
    var max = Int.MIN_VALUE
    for (i in leftBound..rightBound) {
        max = maxOf(max, this[rowIndex][i])
    }
    return max
}

private fun List<List<Int>>.getMaxInColumn(columnIndex: Int, topBound: Int, bottomBound: Int): Int {
    var max = Int.MIN_VALUE
    for (i in topBound..bottomBound) {
        max = maxOf(max, this[i][columnIndex])
    }
    return max
}

private fun List<List<Int>>.countVisibleTreesInRow(
    rowIndex: Int,
    columnIndex: Int,
    indexRange: IntProgression
): Int {
    val currentTreeHeight = this[rowIndex][columnIndex]
    var count = 0

    for (i in indexRange) {
        count++

        if (currentTreeHeight <= this[rowIndex][i]) break
    }

    return count
}

private fun List<List<Int>>.countVisibleTreesInColumn(
    rowIndex: Int,
    columnIndex: Int,
    indexRange: IntProgression
): Int {
    val currentTreeHeight = this[rowIndex][columnIndex]
    var count = 0

    for (i in indexRange) {
        count++

        if (currentTreeHeight <= this[i][columnIndex]) break
    }

    return count
}

