import util.loadResource
import java.util.*
import kotlin.math.pow
import kotlin.math.sqrt

object Day8 {
    @JvmStatic
    fun main(args: Array<String>) {
        val lines = loadResource("day8")
        val instructions = lines.first()
        val directions = readMap(lines.drop(2))

        println("Part 1: ${navigate(instructions, directions)}")

        val starts = directions.keys.filter { it.endsWith("A") }
        println("Part 2: ${ghostNavigation(starts, instructions, directions)}")
    }

    private fun readMap(lines: List<String>): Map<String, Step> {
        return lines.associate { line ->
            val (pos, dest) = line.split(" = ")
            val (left, right) = dest.drop(1).dropLast(1).split(", ")
            pos to (Step(left, right))
        }

    }

    private fun ghostNavigation(starts: List<String>, instructions: String, directions: Map<String, Step>): Long {
        val lowestCompleted = starts.map { navigate(instructions, directions, it, "Z") }.sorted()
        return lcm(lowestCompleted)
    }

    private fun navigate(instructions: String, directions: Map<String, Step>, start: String = "AAA", end: String = "ZZZ"): Long {
        var steps = 0L
        var next = start
        var ins = LinkedList(instructions.toMutableList())

        while (!next.endsWith(end)) {
            if (ins.peek() == null) ins = LinkedList(instructions.toMutableList())
            val dir = ins.poll()

            next = directions[next]!!.next(dir)
            steps++
        }

        return steps
    }

    private fun lcm(numbers: List<Long>): Long {
        return numbers
            .asSequence()
            .flatMap { number -> number.primeFactors().groupingBy { it }.eachCount().toList() }
            .sortedBy { it.second }
            .toSet()
            .associate { it }
            .map { it.key.toDouble().pow(it.value.toDouble()) }
            .fold(1.0) { sum: Double, next: Double -> sum * next }.toLong()
    }

    private fun Long.primeFactors(): List<Long> {
        val factors = mutableListOf<Long>()
        var current = this

        while (current % 2 == 0L) {
            current /= 2
            factors.add(2)
        }

        for (i in 3..sqrt(current.toDouble()).toLong() step 2) {
            while (current % i == 0L) {
                current /= i
                factors.add(i)
            }
        }

        if (current > 2) factors.add(current)

        return factors
    }

    private data class Step(val left: String, val right: String) {
        fun next(direction: Char): String {
            return if (direction == 'R') right else left
        }
    }
}