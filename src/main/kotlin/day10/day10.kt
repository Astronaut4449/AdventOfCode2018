package day10

import java.io.File

fun main() {
    val regex = """(-?\d+),\s+(-?\d+).*?(-?\d+),\s+(-?\d+)""".toRegex()

    val particles = File("Input/10.txt").readLines().map { line ->
        val (x, y, vx, vy) = regex.find(line)!!.destructured.toList().map { it.toInt() }
        Particle(x, y, vx, vy)
    }

    var time = 0
    var lastHeight = Int.MAX_VALUE

    while (true) {
        time += 1

        val currState = particles.map { it.posAt(time) }
        val yMin = currState.map { it.second }.min()!!
        val yMax = currState.map { it.second }.max()!!
        val height = yMax - yMin + 1

        if (height > lastHeight) {
            time -= 1
            break
        } else {
            lastHeight = height
        }
    }

    val message = messageAt(time, particles)
    println("Part 1:\n$message")
    println("Part 2: $time")
}

data class Particle(val x: Int, val y: Int, val vx: Int, val vy: Int) {
    fun posAt(time: Int) = Pair(x + time * vx, y + time * vy)
}

fun messageAt(time: Int, particles: List<Particle>) = StringBuilder().apply {
    val currState = particles.map { it.posAt(time) }
    val yMin = currState.map { it.second }.min()!!
    val yMax = currState.map { it.second }.max()!!
    val xMin = currState.map { it.first }.min()!!
    val xMax = currState.map { it.first }.max()!!

    for (y in yMin..yMax) {
        for (x in xMin..xMax) {
            if (Pair(x, y) in currState) {
                append("#")
            } else {
                append(".")
            }
        }
        append("\n")
    }
}.toString()
