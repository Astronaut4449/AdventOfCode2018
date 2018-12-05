import java.io.File
import java.lang.Integer.max

fun main(args: Array<String>) {
    val polymer = File("Input/05.txt").readText()

    // Part 1
    val finalLength = polymer.react().length
    println("Part 1: $finalLength")

    // Part 2
    val improvements = mutableMapOf<Char, Int>()
    for (char in 'a'..'z') {
        val reducedPolymer = polymer.replace(char.toString(), "", ignoreCase = true)
        val reducedLength = reducedPolymer.react().length
        improvements[char] = reducedLength
    }
    val bestImprovement = improvements.minBy { it.value }
    println("Part 2: $bestImprovement")
}


private fun String.react(): String {
    fun String.doesReact() = this[0].toLowerCase() == this[1].toLowerCase() && this[0] != this[1]

    var polymer = this
    var i = 0
    while (i < polymer.length - 1) {
        if (polymer.substring(i..(i + 1)).doesReact()) {
            polymer = polymer.removeRange(i..(i + 1))
            i = max(i - 1, 0)
        } else {
            i += 1
        }
    }
    return polymer
}
