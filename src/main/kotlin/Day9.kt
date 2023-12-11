import util.loadResource

object Day9 {
    @JvmStatic
    fun main(args: Array<String>) {
        val readings = loadResource("day9").map { input -> input.split(" ").map { it.toInt() } }

        val predictions = readings.map { reading -> predict(reading) { diff, next -> diff + (next.lastOrNull() ?: 0) } }
        println("Part 1: ${predictions.sum()}")

        val histories = readings.map { reading -> predict(reading) { diff, next -> (next.firstOrNull() ?: 0) - diff } }
        println("Part 2: ${histories.sum()}")
    }

    private fun predict(readings: List<Int>, predictor: (Int, List<Int>) -> Int): Int {
        val diffs = mutableListOf(readings)

        while (diffs.last().any { it != 0 }) {
            val current = diffs.last()
            val next = mutableListOf<Int>()

            for (i in 0..<current.lastIndex) {
                val left = current[i]
                val right = current[i + 1]
                next.add(right - left)
            }

            diffs.add(next)
        }

        return diffs.reversed().fold(0, predictor)
    }
}