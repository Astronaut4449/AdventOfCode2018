import java.io.File
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun main(args: Array<String>) {

    // Parse File
    val lines = File("Input/04.txt").lines
    lines.sort()

    val events = mutableListOf<Event>()
    var currentId = 0
    lines.forEach {
        val (timestamp, type, id) = parseLine(it)
        currentId = id ?: currentId
        events.add(Event(timestamp, currentId, type))
    }


    // Part 1
    val eventsByGuardId = events.groupBy { it.guardId }
    val timeRangesByGuardId = eventsByGuardId.mapValues { it.value.toTimeRanges() }
    val guard1: Map.Entry<Id, Int> = timeRangesByGuardId.mapValues {
        it.value.sumBy { timeRange -> timeRange.duration.toMinutes().toInt() }
    }.maxBy { it.value }!!

    val rangesGuard1 = timeRangesByGuardId[guard1.key]!!
    val bestMinute = rangesGuard1.bestMinute()

    println("Part 1: Guard ${guard1.key} * minute $bestMinute = ${guard1.key * bestMinute}")


    // Part 2
    val guard2: Map.Entry<Id, Pair<Minute, Int>> = timeRangesByGuardId.mapValues { rangesEntry ->
        val bestMinute = rangesEntry.value.bestMinute()
        rangesEntry.value.filter { it.inMinute(bestMinute) }.count() to bestMinute
    }.maxBy { it.value.first }!!

    println("Part 2: Guard ${guard2.key} sleeps ${guard2.value.first} times in minute ${guard2.value.second} = ${guard2.key * guard2.value.second}")
}


// Types and classes
typealias Id = Int
typealias Minute = Int

enum class EventType {
    CHANGE_SHIFT, WAKE_UP, FALL_ASLEEP
}

data class Event(val time: LocalDateTime, val guardId: Int, val type: EventType)

class TimeRange(val start: LocalDateTime, val end: LocalDateTime) {
    val duration = Duration.between(start, end)

    fun inMinute(minute: Minute): Boolean {
        return minute >= start.minute.toInt() && minute < end.minute.toInt()
    }
}


// Utility functions
fun List<TimeRange>.bestMinute(): Minute {
    var bestMinute = -1
    var bestOccurances = 0
    var currOccurances = 0
    for (minute in 0..59) {
        currOccurances = this.filter { it.inMinute(minute) }.count()
        if (currOccurances > bestOccurances) {
            bestOccurances = currOccurances
            bestMinute = minute
        }
    }
    return bestMinute
}

fun List<Event>.toTimeRanges(): List<TimeRange> = mutableListOf<TimeRange>().apply {
    lateinit var startTime: LocalDateTime
    for (event in this@toTimeRanges) {
        if (event.type == EventType.FALL_ASLEEP) startTime = event.time
        if (event.type == EventType.WAKE_UP) add(TimeRange(startTime, event.time))
    }
}

private fun parseLine(str: String): Triple<LocalDateTime, EventType, Int?> {
    val re = """\[(\d{4}-\d{2}-\d{2} \d{2}:\d{2})] (\w+) .(\d+)?.*""".toRegex()
    val match = re.matchEntire(str)!!

    val time = LocalDateTime.parse(match.groups[1]!!.value, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
    val type: EventType = when (match.groups[2]!!.value) {
        "Guard" -> EventType.CHANGE_SHIFT
        "falls" -> EventType.FALL_ASLEEP
        "wakes" -> EventType.WAKE_UP
        else -> error("Unknown event type encountered during parsing.")
    }
    val id = match.groups[3]?.value?.toInt()

    return Triple(time, type, id)
}
