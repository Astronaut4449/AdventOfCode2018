import java.io.File

fun main(args: Array<String>) {

    val boxes = File("Input/02.txt").lines

    // Part 1
    val doubles = boxes.map { hasNCommonChars(it, 2) }.count { it }
    val triples = boxes.map { hasNCommonChars(it, 3) }.count { it }
    val checksum = doubles * triples
    println("Part 1: $checksum")


    // Part 2
    lateinit var commonChars: String
    compareAll@ for (i in 0 until boxes.size) for (j in i + 1 until boxes.size) {
        if (countUncommonChars(boxes[i], boxes[j]) == 1) {
            commonChars = charsInCommon(boxes[i], boxes[j])
            break@compareAll
        }
    }
    println("Part 2: $commonChars")

}

private fun hasNCommonChars(str: String, n: Int) =
    str.groupingBy { it }.eachCount().filter { it.value == n }.isNotEmpty()

private fun countUncommonChars(str1: String, str2: String): Int {
    var uncommonChars = 0
    for (i in 0 until str1.length) {
        uncommonChars += (str1[i] != str2[i]).toInt()
    }
    return uncommonChars
}

fun charsInCommon(str1: String, str2: String) = str1.filterIndexed { i, _ -> str1[i] == str2[i] }
