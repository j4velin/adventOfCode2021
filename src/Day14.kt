/**
 * @param input the input polymer string
 * @param rules a map of polymer transformation rules. Key=Input elements, Value=Element to be inserted between the input elements
 * @return a polymer string constructed by taking the [input] string an applying all the [rules] simultaneously
 */
private fun applyRules(input: String, rules: Map<String, String>) =
    input.windowed(2).map { it[0] + rules.getOrDefault(it, it) + it[1] }.foldIndexed(StringBuilder()) { index, acc, s ->
        if (index == 0) acc.append(s) else acc.append(s.drop(1))
    }.toString()

/**
 * Parses the input for to a map of polymer transformation rules
 */
private fun parseRules(input: List<String>) = input.drop(2).map { it.split(" -> ") }.associate { Pair(it[0], it[1]) }

private fun calculateResult(resultPolymer: String): Long {
    val occurrences = resultPolymer.toCharArray().groupBy { it }.mapValues { it.value.size }
    return occurrences.values.maxOf { it.toLong() } - occurrences.values.minOf { it.toLong() }
}

private fun part1(input: List<String>): Int {
    var current = input.first()
    val rules = parseRules(input)
    repeat(10) { current = applyRules(current, rules) }
    return calculateResult(current).toInt()
}

private fun part2(input: List<String>): Long {
    var current = input.first()
    val rules = parseRules(input)
    repeat(40) {
        current = applyRules(current, rules)
        println("$it - "+current.length)
    }
    return calculateResult(current)
}

fun main() {

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day14_test")
    check(part1(testInput) == 1588)
    check(part2(testInput) == 2188189693529L)

    val input = readInput("Day14")
    println(part1(input))
    println(part2(input))
}
