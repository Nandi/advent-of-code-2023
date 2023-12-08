import util.loadResource

object Day6 {
    @JvmStatic
    fun main(args: Array<String>) {
        val lines = loadResource("day6")
        val (times, distances) = parseInput(lines)

        println("Part 1: ${winningOptions(times, distances)}")
        println("Part 2: ${winningOptions(listOf(times.joinToString("").toLong()), listOf(distances.joinToString("").toLong()))}")
    }

    private fun parseInput(lines: List<String>): Pair<List<Long>, List<Long>> {
        val times = lines[0].drop(5).trim().split(" {2,}".toRegex()).map { it.trim().toLong() }
        val distances = lines[1].drop(9).trim().split(" {2,}".toRegex()).map { it.trim().toLong() }
        return times to distances
    }

    private fun winningOptions(times: List<Long>, distances: List<Long>): Long {
        return times.mapIndexed { i, time -> winnerDistance(time, distances[i]) }.map { it.count() }.fold(1L) { sum, count -> sum * count }
    }

    private fun winnerDistance(time: Long, distance: Long): List<Pair<Long, Long>> {
        val half = time / 2L
        return (half downTo 0L).calculateDistance(time, distance) +
                ((half + 1)..time).calculateDistance(time, distance)
    }

    private fun Iterable<Long>.calculateDistance(time: Long, cutoff: Long): List<Pair<Long, Long>> {
        return asSequence()
            .map { it to it * (time - it) }
            .takeWhile { it.second > cutoff }
            .toList()
    }
}