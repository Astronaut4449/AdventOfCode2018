package day04

import java.io.File

fun main() {
    val sleepTimesByGuardId: Map<Int, List<IntRange>> = parseFile("Input/04.txt")

    // Part 1
    val sleepiestGuardId = sleepTimesByGuardId
        .mapValues { (_, sleepTimes) -> sleepTimes.sumBy { it.last - it.first } }
        .maxBy { (_, minutesAsleep) -> minutesAsleep }!!
        .key

    val sleepiestMinuteOfSleepiestGuard = sleepTimesByGuardId[sleepiestGuardId]!!
        .overlappingMinutes()
        .maxBy { it.value }!!
        .key

    println("Part 1: ${sleepiestMinuteOfSleepiestGuard * sleepiestGuardId}")

    // Part 2
    val (mostReliableGuard, countByMinute) = sleepTimesByGuardId
        .mapValues { (_, sleepTimes) ->
            sleepTimes.overlappingMinutes().maxBy { it.value }!!
        }
        .maxBy { it.value.value }!!

    val mostReliableMinute = countByMinute.key

    println("Part 2: ${mostReliableGuard * mostReliableMinute}")

}

fun List<IntRange>.overlappingMinutes() = flatMap { minutes -> minutes.toList() }.groupingBy { it }.eachCount()

fun parseFile(filename: String) = File(filename).readLines().sorted().run {
    val sleepTimesByGuardId = hashMapOf<Int, MutableList<IntRange>>()

    val re = """.*:(\d{2})] (\w+) .(\d+)?.*""".toRegex()
    var currGuardId = 0
    var startSleep = 0

    forEach { line ->
        val (time, eventType, id) = re.matchEntire(line)!!.destructured

        when (eventType) {
            "Guard" -> currGuardId = id.toInt()
            "falls" -> startSleep = time.toInt()
            "wakes" -> sleepTimesByGuardId.getOrPut(currGuardId, { mutableListOf() }).add(startSleep until time.toInt())
        }
    }

    return@run sleepTimesByGuardId
}