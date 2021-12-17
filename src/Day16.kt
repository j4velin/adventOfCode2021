
private fun part1(input: List<String>) = 0

private fun part2(input: List<String>) = 0

fun main() {

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day16_test")
    check(part1(testInput) == 0)
    check(part2(testInput) == 0)

    val input = readInput("Day16")
    println(part1(input))
    println(part2(input))
}
