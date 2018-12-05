import java.io.File

fun main(args: Array<String>) {

    val numbers = File("Input/01.txt").lines.map { it.toInt() }

    // Part 1
    val sum = numbers.sum()
    println("Part 1: $sum")


    // Part 2
    var result = 0
    val results = mutableListOf<Int>()
    outer@ while (true) {
        for (number in numbers) {
            result += number
            if (result in results) break@outer
            results.add(result)
        }
    }
    println("Part 2: $result")

}
