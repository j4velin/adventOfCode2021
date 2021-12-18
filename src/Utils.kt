import java.io.File
import java.math.BigInteger
import java.security.MessageDigest
import kotlin.math.absoluteValue

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String) = File("src", "$name.txt").readLines()

/**
 * Converts string to md5 hash.
 */
fun String.md5(): String = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray())).toString(16)

/**
 * Data class representing a point on a 2D area
 */
data class Point(val x: Int, val y: Int) {

    /**
     * @param withDiagonal true to also include diagonal neighbours
     * @param validGrid optional area (pair of top left and upper right corner) in which the neighbours must be within
     * @return the neighbouring points of this point
     */
    fun getNeighbours(withDiagonal: Boolean = false, validGrid: Pair<Point, Point>? = null) = buildSet {
        for (i in -1..1) {
            for (j in -1..1) {
                if ((i.absoluteValue == j.absoluteValue) && (i == 0 || !withDiagonal)) {
                    continue
                }
                val p = Point(x + i, y + j)
                if (validGrid == null || p.isWithin(validGrid)) {
                    add(p)
                }
            }
        }
    }

    /**
     * @param dx the delta in x direction
     * @param dy the delta in y direction
     * @return the new resulting point, which is created by moving this point along the given vector
     */
    fun move(dx: Int, dy: Int) = Point(x + dx, y + dy)

    private fun isWithin(grid: Pair<Point, Point>) =
        x >= grid.first.x && x <= grid.second.x && y >= grid.first.y && y <= grid.second.y
}
