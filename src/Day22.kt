import java.util.*

private data class Instruction(
    val newState: Boolean, val xRange: IntRange, val yRange: IntRange, val zRange: IntRange
) {
    companion object {
        fun fromString(input: String): Instruction {
            val split = input.split(" ")
            val ranges = split[1].trim().split(",")
            return Instruction(
                split[0].trim() == "on",
                rangeFromString(ranges[0]),
                rangeFromString(ranges[1]),
                rangeFromString(ranges[2])
            )
        }
    }
}

private fun rangeFromString(input: String): IntRange {
    // x=-20..26,
    val split = input.drop(2).removeSuffix(",").split("..")
    return IntRange(split[0].toInt(), split[1].toInt())
}

/**
 * Applies the given instructions on part of the reactor
 * @param instructions the instructions to apply
 * @param xRange the range in x dimension to consider
 * @param yRange the range in y dimension to consider (default: same as [xRange])
 * @param zRange the range in z dimension to consider (default: same as [xRange])
 * @return the number of turned on cubes after applying all instructions on this subset
 */
private fun applyInstructions(
    instructions: List<Instruction>, xRange: IntRange, yRange: IntRange = xRange, zRange: IntRange = xRange
): Int {
    val size = xRange.count().toLong() * yRange.count() * zRange.count()
    if (size < 0 || size > Int.MAX_VALUE) {
        throw IllegalArgumentException("Range too big")
    }
    val xOffset = if (xRange.first < 0) xRange.first * -1 else 0
    val yOffset = if (yRange.first < 0) yRange.first * -1 else 0
    val zOffset = if (zRange.first < 0) zRange.first * -1 else 0
    val reactor = BitSet(size.toInt())
    val yMultiplier = xRange.count()
    val zMultiplier = xRange.count() * yRange.count()
    instructions.filter { it.xRange.intersect(xRange).isNotEmpty() }.filter { it.yRange.intersect(yRange).isNotEmpty() }
        .filter { it.zRange.intersect(zRange).isNotEmpty() }.forEach {
            for (x in it.xRange) {
                for (y in it.yRange) {
                    for (z in it.zRange) {
                        val xIndex = x + xOffset
                        val yIndex = y + yOffset
                        val zIndex = z + zOffset
                        val index = xIndex + yIndex * yMultiplier + zIndex * zMultiplier
                        reactor[index] = it.newState
                    }
                }
            }
        }
    return reactor.cardinality()
}

private fun part1(input: List<String>) =
    applyInstructions(input.map(Instruction.Companion::fromString), IntRange(-50, 50))

private fun part2(input: List<String>): Long {
    val instructions = input.map(Instruction.Companion::fromString)
    var turnedOnCubes = 0L

    val min = instructions.minOf { minOf(it.xRange.first, it.yRange.first, it.zRange.first) }
    val max = instructions.maxOf { maxOf(it.xRange.last, it.yRange.last, it.zRange.last) }

    val cubesPerDimensionPerCheck = 1000
    val checksPerDimension = (max - min) / cubesPerDimensionPerCheck

    for (xOffset in 0..checksPerDimension) {
        val xIndex = min + xOffset * cubesPerDimensionPerCheck
        for (yOffset in 0..checksPerDimension) {
            val yIndex = min + yOffset * cubesPerDimensionPerCheck
            for (zOffset in 0..checksPerDimension) {
                val zIndex = min + zOffset * cubesPerDimensionPerCheck
                turnedOnCubes += applyInstructions(
                    instructions,
                    IntRange(xIndex, xIndex + cubesPerDimensionPerCheck),
                    IntRange(yIndex, yIndex + cubesPerDimensionPerCheck),
                    IntRange(zIndex, zIndex + cubesPerDimensionPerCheck)
                )
            }
        }
    }

    return turnedOnCubes
}

fun main() {

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day22_test")
    check(part1(testInput) == 474140)
    //check(part2(testInput) == 2758514936282235)

    val input = readInput("Day22")
    println(part1(input))
    // println(part2(input))


}
