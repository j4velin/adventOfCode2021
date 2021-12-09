/**
 * Transforms the input in a 2D Byte array representing the heights at each position
 */
private fun getHeightMap(input: List<String>) =
    input.map { str -> str.map { it.digitToInt().toByte() }.toByteArray() }.toTypedArray()

/**
 * Represents a point in a height map
 * @property x the x-coordinate
 * @property y the y-coordinate
 * @property height the height at that point
 */
private data class HeightMapPoint(val x: Int, val y: Int, val height: Byte)

/**
 * @param point a point within this height map to get the neighbouring points for
 * @return a list of all the neighbours of the given point (horizontal & vertical neighbours only)
 */
private fun Array<ByteArray>.getNeighboursOf(point: HeightMapPoint) = buildList {
    val x = point.x
    val y = point.y
    val map = this@getNeighboursOf
    // points on the edged and the corners have only 2 or 3 neighbours
    if (x > 0) {
        add(HeightMapPoint(x - 1, y, map[x - 1][y]))
    }
    if (x < map.size - 1) {
        add(HeightMapPoint(x + 1, y, map[x + 1][y]))
    }
    if (y > 0) {
        add(HeightMapPoint(x, y - 1, map[x][y - 1]))
    }
    if (y < map[x].size - 1) {
        add(HeightMapPoint(x, y + 1, map[x][y + 1]))
    }
}

/**
 * @return all the 'low points' in this height map
 */
private fun Array<ByteArray>.getLowPoints() = sequence {
    val map = this@getLowPoints
    for (x in map.indices) {
        for (y in map[x].indices) {
            val current = HeightMapPoint(x, y, map[x][y])
            val neighbours = map.getNeighboursOf(current)
            if (neighbours.all { it.height > current.height }) {
                yield(current)
            }
        }
    }
}

private fun part1(input: List<String>) = getHeightMap(input).getLowPoints().map { it.height + 1 }.sum()

private fun part2(input: List<String>): Int {
    val heightMap = getHeightMap(input)
    return heightMap.getLowPoints().map { lowPoint ->
        val basin = mutableSetOf<HeightMapPoint>()
        val queue = mutableListOf(lowPoint)
        while (queue.isNotEmpty()) {
            val current = queue.removeFirst()
            if (basin.add(current)) {
                queue.addAll(heightMap.getNeighboursOf(current).filter { it.height < 9 })
            }
        }
        basin.count() // value of the basin is the amount of its points, ignoring their height
    }.sortedDescending().take(3).reduce { acc, i -> acc * i }
}

fun main() {

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day09_test")
    check(part1(testInput) == 15)
    check(part2(testInput) == 1134)

    val input = readInput("Day09")
    println(part1(input))
    println(part2(input))
}
