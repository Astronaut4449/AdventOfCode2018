package day06

import java.io.File
import kotlin.math.abs

data class Point(val x: Int, val y: Int)

fun manhattanDistance(p1: Point, p2: Point) = abs(p2.x - p1.x) + abs(p2.y - p1.y)

val Map.Entry<Point, Any>.point get() = key
val Map.Entry<Point, Int>.distance get() = value
val Map.Entry<Point, MutableSet<Point>>.points get() = value
val Map<Point, Set<Point>>.points get() = values

fun main() {
    val initialPoints = File("Input/06.txt").readLines()
        .map { line -> line.split(", ").map { it.toInt() } }
        .map { Point(it[0], it[1]) }
        .toSet()

    val xMin = initialPoints.minBy { it.x }!!.x
    val xMax = initialPoints.maxBy { it.x }!!.x
    val yMin = initialPoints.minBy { it.y }!!.y
    val yMax = initialPoints.maxBy { it.y }!!.y


    // Part 1
    val growingAreasByPoint = initialPoints.associateWith { mutableSetOf(it) }

    for (x in (xMin - 1)..(xMax + 1)) {
        for (y in (yMin - 1)..(yMax + 1)) {
            val point = Point(x, y)
            val distancesByInitialPoint = initialPoints.associateWith { manhattanDistance(point, it) }
            val (closestPoint, minDistance) = distancesByInitialPoint.minBy { it.distance }!!
            if (distancesByInitialPoint.count { it.distance == minDistance} == 1) growingAreasByPoint[closestPoint]?.add(point)
        }
    }

    val areasInInitialFrameByPoint = growingAreasByPoint.mapValues { it.points.filter { p -> p.x in xMin..xMax && p.y in yMin..yMax } }
    val finiteAreasByPoint = growingAreasByPoint.filter { it.points.count() == areasInInitialFrameByPoint[it.point]!!.count() }
    val biggestFiniteArea = finiteAreasByPoint.points.map { it.count() }.max()

    println("Part 1: $biggestFiniteArea")


    // Part 2
    var count = 0
    for (x in xMin..xMax) {
        for (y in yMin..yMax) {
            val point = Point(x, y)
            val totalDistance = initialPoints.map { manhattanDistance(point, it) }.sum()
            if (totalDistance < 10000) count += 1
        }
    }

    println("Part 2: $count")

}