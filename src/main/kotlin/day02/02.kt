package day02

import util.toInt
import java.io.File

fun main() {

    val boxes = File("Input/02.txt").readLines()

    // Part 1
    val doubles = boxes.map { it.hasNCommonChars(2) }.count { it }
    val triples = boxes.map { it.hasNCommonChars(3) }.count { it }
    val checksum = doubles * triples
    println("Part 1: $checksum")


    // Part 2
    lateinit var commonChars: String
    compareAll@ for (i in 0 until boxes.size) for (j in i + 1 until boxes.size) {
        if (uncommonCharCount(boxes[i], boxes[j]) == 1) {
            commonChars = commonCharCount(boxes[i], boxes[j])
            break@compareAll
        }
    }
    println("Part 2: $commonChars")

}

private fun String.hasNCommonChars(n: Int) = groupingBy { char -> char }
    .eachCount()
    .filter { it.value == n }
    .isNotEmpty()

private fun uncommonCharCount(str1: String, str2: String) = str1.foldIndexed(0) { index, acc, _ ->
    acc + (str1[index] != str2[index]).toInt()
}

fun commonCharCount(str1: String, str2: String) = str1.filterIndexed { i, _ -> str1[i] == str2[i] }
