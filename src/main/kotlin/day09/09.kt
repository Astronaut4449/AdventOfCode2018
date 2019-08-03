package day09

import java.io.File
import java.math.BigInteger

fun main() {
    val (playerCount, marbleCount) = File("Input/09.txt").readText().let { line ->
        val regex = """(\d+).*?(\d+).*""".toRegex()
        regex.matchEntire(line)!!.destructured.toList().map { it.toInt() }
    }

    println("Part 1: ${highestScoreOfMarbleGame(playerCount, marbleCount)}")
    println("Part 2: ${highestScoreOfMarbleGame(playerCount, marbleCount*100)}")
}

class Element(val value: Int) {
    var previous = this
    var next = this
}

class Circle {
    private var head = Element(0)

    fun moveHead(steps: Int) = if (steps > 0) {
        repeat(steps) { head = head.next }
    } else {
        repeat(-steps) { head = head.previous }
    }

    fun add(number: Int) {
        moveHead(2)

        val element = Element(number)
        element.previous = head.previous
        element.previous.next = element
        head.previous = element
        element.next = head
        head = element
    }

    fun remove(): Int {
        val removed = head.value
        head.previous.next = head.next
        head.next.previous = head.previous
        head = head.next
        return removed
    }
}

fun highestScoreOfMarbleGame(playerCount: Int,  marbleCount: Int): BigInteger {
    val scoreOfPlayer = hashMapOf<Int, BigInteger>()

    val circle = Circle()
    var currPlayer = 1
    for (marble in 1..marbleCount) {
        if (marble % 23 != 0) {
            circle.add(marble)
        } else {
            scoreOfPlayer[currPlayer] = scoreOfPlayer.getOrPut(currPlayer, { BigInteger.ZERO }) + marble.toBigInteger()
            circle.moveHead(-7)
            scoreOfPlayer[currPlayer] = scoreOfPlayer[currPlayer]!! + circle.remove().toBigInteger()
        }
        currPlayer = (currPlayer + 1) % playerCount
    }

    return scoreOfPlayer.values.max()!!
}