private fun getPositions(input: String): Sequence<List<Pair<Int, Int>>> {
    // input: "target area: x=20..30, y=-10..-5"
    val area = input.drop("target area: x=".length).split(", y=").map { it.split("..") }
    val areaX = IntRange(area[0][0].toInt(), area[0][1].toInt())
    val areaY = IntRange(area[1][0].toInt(), area[1][1].toInt())

    // TODO: currently assumed that target area is to the right somewhere
    // calculate set x coordinates which have at least one position within areaX
    // example: x positions with velocity 5 after each step: 0, 5, 5+4, 5+4+3, 5+4+3+2, 5+4+3+2+1
    val horizontalPositions = IntRange(1, areaX.last).map { velocity ->
        buildList {
            var previousPositionX = 0
            add(previousPositionX)
            for (v in velocity downTo 1) {
                add(previousPositionX + v)
                previousPositionX += v
            }
        }
    }.filter { positions -> positions.any { it in areaX } }

    // generate initial y accelerations
    // TODO: think of a way to calculate the bounds...
    val lowerBound = -500
    val upperBound = lowerBound * -2
    return generateSequence(lowerBound) { it + 1 }.take(upperBound).flatMap { velocity ->
        buildList {
            addAll(horizontalPositions.map { xPositions ->
                val list = mutableListOf<Pair<Int, Int>>()
                var currentPositionY = 0
                var currentVelocityY = velocity
                var lastPositionX = 0
                for (xPos in xPositions) {
                    list.add(Pair(xPos, currentPositionY))
                    currentPositionY += currentVelocityY
                    currentVelocityY--
                    lastPositionX = xPos
                }
                // even if x doesn't change anymore, y can still fall lower -> keep adding positions until we're
                // definitely below the target
                while (currentPositionY >= areaY.first) {
                    list.add(Pair(lastPositionX, currentPositionY))
                    currentPositionY += currentVelocityY
                    currentVelocityY--
                }
                list
            })
        }
    }.filter { it.any { pos -> pos.first in areaX && pos.second in areaY } }
}

private fun part1(input: String) = getPositions(input).map { positions -> positions.maxOf { it.second } }.maxOf { it }

private fun part2(input: String) = getPositions(input).count()

fun main() {

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day17_test").first()
    check(part1(testInput) == 45)
    check(part2(testInput) == 112)

    val input = readInput("Day17").first()
    println(part1(input))
    println(part2(input))
}
