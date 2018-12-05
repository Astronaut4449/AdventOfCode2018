import java.io.File

fun main(args: Array<String>) {

    // Parse File
    val sleepyTimes = mutableListOf<SleepInterval>()
    with (File("Input/04.txt").lines) {
        sort()

        val re = """.*:(\d{2})] (\w+) .(\d+)?.*""".toRegex()
        var currGuard = 0
        var startSleep = 0

        for (line in this) {
            val match = re.matchEntire(line)!!

            when (match.groups[2]!!.value) {
                "Guard" -> currGuard = match.groups[3]!!.value.toInt()
                "falls" -> startSleep = match.groups[1]!!.value.toInt()
                "wakes" -> sleepyTimes.add(SleepInterval(currGuard, startSleep, match.groups[1]!!.value.toInt()))
            }
        }

    }

    // Part 1
    val guard1 = sleepyTimes
            .groupBy { it.guard }
            .mapValues { it.value.sumBy { it.duration } }
            .maxBy { it.value }!!
            .key

    val minute1 = sleepyTimes.filter { it.guard == guard1 }.bestMinute()

    println("Part 1: Guard $guard1 * minute $minute1 = ${guard1 * minute1}")


    // Part 2
    val guard2 = sleepyTimes
            .groupBy { it.guard }
            .mapValues {
                val minute = it.value.bestMinute()
                it.value.filter { minute in it }.count()
            }
            .maxBy { it.value }!!.key

    val minute2 = sleepyTimes
            .filter { it.guard == guard2 }
            .bestMinute()

    println("Part 2: Guard $guard2 * minute $minute1 = ${guard2 * minute2}")

}

class SleepInterval(val guard: Int, val start: Int, val end: Int) {
    val duration = end - start

    operator fun contains(minute: Int) = minute >= start && minute < end
}

fun List<SleepInterval>.bestMinute(): Int {
    var bestMinute = -1
    var bestOccurances = 0
    var currOccurances = 0
    for (minute in 0..59) {
        currOccurances = this.filter { minute in it }.count()
        if (currOccurances > bestOccurances) {
            bestOccurances = currOccurances
            bestMinute = minute
        }
    }
    return bestMinute
}
