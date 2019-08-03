package day11

import java.io.File
import kotlin.math.max

fun power(x: Int, y: Int): Int {
    if (x == 0 || y == 0) return 0

    val rackId = x + 10
    var power = rackId * y
    power += serialNumber
    power *= rackId
    power = power / 100 % 10
    power -= 5

    return power
}

private val serialNumber = File("Input/11.txt").readText().toInt()

operator fun Array<Array<Int>>.get(x: Int, y: Int) = this[x][y]
operator fun Array<Array<Int>>.set(x: Int, y: Int, v: Int) = run { this[x][y] = v }

fun main() {
    val powerGrid = Array(301) { x -> Array(301) { y -> power(x, y) } }

    val summed = Array(301) { Array(301) { 0 } }

    for (y in 1..300) {
        for (x in 1..300) {
            summed[x, y] = powerGrid[x, y] + summed[x - 1, y] + summed[x, y - 1] - summed[x - 1, y - 1]
        }
    }

    // Part 1
    run {
        var maxPower = Int.MIN_VALUE
        var maxPos = Pair(0, 0)
        val size = 3

        for (x in 1..297) {
            for (y in 1..297) {
                val power = summed[x - 1, y - 1] + summed[x - 1 + size, y - 1 + size] -
                        summed[x - 1, y - 1 + size] - summed[x - 1 + size, y - 1]

                if (power > maxPower) {
                    maxPower = power
                    maxPos = Pair(x, y)
                }
            }
        }

        println("Part 1: ${maxPos.first},${maxPos.second}")
    }

    // Part 2
    run {
        var maxPower = Int.MIN_VALUE
        var maxPos = Pair(0, 0)
        var maxSize = 0

        for (x in 1..300) {
            for (y in 1..300) {
                for (size in 1..(301 - max(x, y))) {
                    val power = summed[x - 1, y - 1] + summed[x - 1 + size, y - 1 + size] -
                            summed[x - 1, y - 1 + size] - summed[x - 1 + size, y - 1]

                    if (power > maxPower) {
                        maxPower = power
                        maxPos = Pair(x, y)
                        maxSize = size
                    }
                }
            }
        }

        println("Part 2: ${maxPos.first},${maxPos.second},$maxSize")
    }

}