package day01

import java.io.File

fun main() {
    val numbers = File("Input/01.txt").readLines().map { it.toInt() }

    // Part 1
    val sum = numbers.sum()
    println("Part 1: $sum")

    // Part 2
    var result = 0
    val results = mutableSetOf<Int>(0)
    cycleNumbers@ while (true) {
        for (number in numbers) {
            result += number
            if (!results.add(result)) break@cycleNumbers
        }
    }
    println("Part 2: $result")

}
