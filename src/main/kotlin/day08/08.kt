package day08

import java.io.File
import java.util.*

class Node(entriesCount: Int) {
    val children = mutableListOf<Node>()
    val entries = Array<Int>(entriesCount) { 0 }
}

fun createNodes(numbers: Queue<Int>): Node {
    val countChildren = numbers.remove()
    val countEntries = numbers.remove()

    val node = Node(countEntries)

    for (i in 0 until countChildren) {
        node.children.add(createNodes(numbers))
    }

    for (i in 0 until countEntries) {
        node.entries[i] = numbers.remove()
    }

    return node
}

fun Node.sum(): Int {
    var sum = entries.sum()

    for (child in children) {
        sum += child.sum()
    }

    return sum
}

fun Node.specialSum(): Int {
    return if (children.size == 0) {
        entries.sum()
    } else {
        var sum = 0
        for (entry in entries) {
            children.getOrNull(entry - 1)?.let { sum += it.specialSum() }
        }
        sum
    }
}

fun main() {
    val numbers: Queue<Int> = ArrayDeque<Int>().apply {
        File("Input/08.txt").readText().split(" ").forEach { add(it.toInt()) }
    }

    val root = createNodes(numbers)
    val sum = root.sum()

    println("Part 1: $sum")

    val specialSum = root.specialSum()

    println("Part 2: $specialSum")
}