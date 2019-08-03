package day07

import java.io.File

typealias Step = Char
typealias Time = Int
typealias Predecessors = Map<Step, Set<Step>>

val regexFirstNext = """\b([A-Z])\b.*\b([A-Z])\b""".toRegex()

fun main(args: Array<String>) {
    // Parse file
    val stepToPredecessors = hashMapOf<Step, MutableSet<Step>>()

    File("Input/07.txt").forEachLine { line ->
        val (first, next) = regexFirstNext.find(line)!!.destructured.toList().map { it.first() }
        stepToPredecessors.getOrPut(next) { hashSetOf() }.add(first)
        stepToPredecessors.putIfAbsent(first, hashSetOf())
    }

    // Part 1
    val stepOrder = stepOrder(stepToPredecessors).joinToString("")
    println("Part 1: $stepOrder")

    // Part 2
    val duration = timeToComplete(stepToPredecessors, workerCount = 5, stepDefaultTime = 60)
    println("Part 2: $duration")
}


fun stepOrder(stepToPredecessors: Predecessors): Set<Step> {
    val completedSteps = mutableSetOf<Step>()
    val remainingSteps = stepToPredecessors.keys.toMutableSet()

    fun Set<Step>.next() = filterNot { step ->
        stepToPredecessors[step]!!.any { it in remainingSteps }
    }.min()!!

    while (remainingSteps.isNotEmpty()) {
        val step = remainingSteps.next()
        remainingSteps.remove(step)
        completedSteps.add(step)
    }

    return completedSteps
}


fun timeToComplete(predecessorsOfStep: Predecessors, workerCount: Int, stepDefaultTime: Int = 0): Time {
    val remainingSteps = predecessorsOfStep.keys.toMutableSet()
    val timeToCompletion = mutableMapOf<Time, MutableSet<Step>>()

    var time = 0

    fun Step.predecessors() = predecessorsOfStep[this]!!

    fun Set<Step>.next() = filterNot { step ->
        step.predecessors().any { it in remainingSteps || it in timeToCompletion.flatMap { it.value } }
    }.min()

    fun Step.duration() = stepDefaultTime + ('A'..'Z').indexOf(this) + 1

    while (remainingSteps.isNotEmpty()) {
        val step = remainingSteps.next()

        if (step != null && timeToCompletion.values.sumBy { it.size } < workerCount) {
            timeToCompletion.putIfAbsent(time + step.duration(), mutableSetOf<Step>())
            timeToCompletion[time + step.duration()]?.add(step)
            remainingSteps.remove(step)
        } else {
            time = timeToCompletion.keys.min()!!
            timeToCompletion.remove(time)
        }
    }

    return timeToCompletion.keys.max()!!
}