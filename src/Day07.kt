private const val PARENT_DIR = ".."

fun main() {
    // go through input
    // keep a pointer to rootDir & parentDir & currentDir
    // for each command:
    //      ls:
    //          for each item in list create a node in current dir
    //      cd:
    //          ..: set currentDir to parentDir
    //           X: set parentDir to currentDir, currentDir = X

    fun part1(input: List<String>): Int {
        val rootDir = Directory("/")
        var currentDir = rootDir

        for (currentLineIndex in 1..input.indices.last) {
            val currentLine = input[currentLineIndex]

            when {
                currentLine.startsWith("$ ls") -> currentDir.ls()

                currentLine.startsWith("$ cd") -> currentDir = currentDir.cd(parseDirName(currentLine))

                else -> currentDir.add(parseNode(currentLine, currentDir))
            }
        }

        var sum = 0

        val queue = ArrayDeque<Directory>()
        queue.add(rootDir)

        while (queue.isNotEmpty()) {
            val currDir = queue.removeFirst()
            val currDirSize = currDir.size
            if (currDirSize <= 100_000) {
                sum += currDirSize
            }
            currDir.children
                .filterIsInstance<Directory>()
                .forEach(queue::addLast)
        }

        return sum
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    val input = readInput("Day07").lines()
    println(part1(input))
    println(part2(input))
}

interface Node {
    val size: Int
    val name: String
}

class Directory(
    override val name: String,
    private val parent: Directory? = null,
    val children: MutableList<Node> = mutableListOf(),
) : Node {
    override val size: Int
        get() = children.sumOf { it.size }

    override fun toString(): String {
        return "$name (dir)"
    }

    fun ls() {
        println("Reading directory $this contents")
    }

    fun cd(dirName: String): Directory {
        val newDir = when (dirName) {
            PARENT_DIR -> this.parent ?: throw IllegalArgumentException("Dir $this doesn't have parent")
            else -> findChildDirectory(dirName)
        }
        println("Changing directory to $newDir")
        return newDir
    }

    private fun findChildDirectory(name: String): Directory {
        val child = children.find { it.name == name }
        if (child == null || child !is Directory) {
            throw IllegalArgumentException("Directory ${this.name} doesn't contain child directory $name")
        }
        return child
    }

    fun add(node: Node) {
        println("Enumerating $node in $this")
        children.add(node)
    }
}

class File(
    override val size: Int,
    override val name: String
) : Node {
    override fun toString(): String {
        return "$name (file, size=$size)"
    }
}

fun parseNode(line: String, parent: Directory): Node {
    val components = line.split(" ")
    return if (components.first() == "dir") {
        Directory(name = components.last(), parent)
    } else {
        File(size = components.first().toInt(), name = components.last())
    }
}

fun parseDirName(line: String) =
    line.split(" ").last()
