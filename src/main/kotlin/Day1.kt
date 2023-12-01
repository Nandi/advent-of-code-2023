import util.loadResource


object Day1 {
    @JvmStatic
    fun main(args: Array<String>) {
        val lines = loadResource("day1")
        println("Part 1: ${calibration(lines, false)}")
        println("Part 1: ${calibration(lines, true)}")
    }

    private fun calibration(lines: List<String>, wordToDigit: Boolean): Int {
        return lines
            .mapIf(wordToDigit) { replaceDigitWords(it) }
            .sumOf { calibrationLine ->
                val first = calibrationLine.first { it.isDigit() }.digitToInt()
                val second = calibrationLine.reversed().first { it.isDigit() }.digitToInt()
                "$first$second".toInt()
            }
    }

    private fun replaceDigitWords(line: String): String {
        return (digitWords.keys + digitWords.values)
            .flatMap { word -> word.toRegex().findAll(line).map { it.range.first to word } }
            .sortedBy { it.first }
            .joinToString("") { digitWords[it.second] ?: it.second }
    }

    private fun <E> Collection<E>.mapIf(condition: Boolean, mapper: (E) -> E): Collection<E> {
        return if (condition) map(mapper)
        else this
    }

    private val digitWords = mapOf(
        "one" to "1",
        "two" to "2",
        "three" to "3",
        "four" to "4",
        "five" to "5",
        "six" to "6",
        "seven" to "7",
        "eight" to "8",
        "nine" to "9",
    )
}