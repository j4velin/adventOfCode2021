private fun Boolean.toInt() = if (this) 1 else 0

/**
 * @param input the "image enhancement algorithm"
 */
private class ImageEnhancer(input: String) {

    private val enhancement = input.map { it == '#' }.toBooleanArray()

    /**
     * Enhances the given image
     * @param inputImage the image to enhance
     * @return the enhanced images
     */
    fun apply(inputImage: Image): Image {
        val maxX = inputImage.maxX + 2
        val maxY = inputImage.maxY + 2
        val array = Array(maxY) { BooleanArray(maxX) }
        for (y in 0 until maxY) {
            for (x in 0 until maxX) {
                val xInInput = x - 1
                val yInInput = y - 1
                val index = buildString {
                    for (i in -1..1) {
                        for (j in -1..1) {
                            append(inputImage[xInInput + j, yInInput + i].toInt())
                        }
                    }
                }.toInt(2)
                array[y][x] = enhancement[index]
            }
        }
        // if the first character of the enhancement algorithm is true (e.g. '#'), then enhancing an image will enlighten
        // every 'dark area' from the input image - as we deal with infinite images, this will be an infinite amount of
        // pixels for every odd enhancement. We only store the 'outside pixel' once though
        return Image(array, if (enhancement.first()) !inputImage.outside else false)
    }
}

/**
 * Represents a black/white image (light/dark pixels)
 * @param array the input pixels
 * @property outside true, if the pixel outside this image's border should be considered to be "on", false by default
 */
class Image(private val array: Array<BooleanArray>, val outside: Boolean = false) {

    val maxX = array.firstOrNull()?.size ?: 0
    val maxY = array.size

    companion object {
        fun fromString(input: List<String>) =
            Image(input.map { line -> line.map { it == '#' }.toBooleanArray() }.toTypedArray())
    }

    operator fun get(x: Int, y: Int): Boolean {
        return if (y in array.indices && x in array[y].indices) {
            array[y][x]
        } else {
            outside
        }
    }

    /**
     * @return the total number of "on"/"lit" pixels in this image. Might be [Double.POSITIVE_INFINITY]
     */
    fun countLitPixels(): Number {
        if (outside) {
            return Double.POSITIVE_INFINITY
        }
        var count = 0
        for (x in 0..maxX) {
            for (y in 0..maxY) {
                if (get(x, y)) {
                    count++
                }
            }
        }
        return count
    }

    override fun toString(): String {
        val width = array.firstOrNull()?.size ?: 0
        val height = array.size
        return buildString {
            for (y in 0 until height) {
                for (x in 0 until width) {
                    if (array[y][x]) {
                        append("#")
                    } else {
                        append(".")
                    }
                }
                appendLine()
            }
        }
    }
}

private fun part1(input: List<String>): Int {
    val enhancer = ImageEnhancer(input.first())
    var image = Image.fromString(input.drop(2))

    repeat(2) { image = enhancer.apply(image) }

    return image.countLitPixels().toInt()
}

private fun part2(input: List<String>): Int {
    val enhancer = ImageEnhancer(input.first())
    var image = Image.fromString(input.drop(2))

    repeat(50) { image = enhancer.apply(image) }

    return image.countLitPixels().toInt()
}

fun main() {

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day20_test")
    check(part1(testInput) == 35)
    check(part2(testInput) == 3351)

    val input = readInput("Day20")
    println(part1(input))
    println(part2(input))
}
