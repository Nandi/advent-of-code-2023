import Day2.KubeColor.*
import util.loadResource

object Day2 {
    @JvmStatic
    fun main(args: Array<String>) {
        val games = loadResource("day2")
        println("Part 1: ${possibleMatchGames(games, KubeConfig(12, 13, 14))}")
        println("Part 2: ${minimumPossibleGames(games)}")

    }

    private fun possibleMatchGames(games: List<String>, kubeConfig: KubeConfig): Int {
        return games.mapIndexed { id, game ->
            val pulls = game.drop(game.indexOf(':') + 1).trim()
            val result = pulls(pulls)
            KubeGame(id + 1, result[RED] ?: 0, result[GREEN] ?: 0, result[BLUE] ?: 0)
        }.filter { validGame(it, kubeConfig) }
            .sumOf { it.id }
    }

    private fun validGame(game: KubeGame, kubeConfig: KubeConfig): Boolean {
        return game.maxRed <= kubeConfig.red
                && game.maxBlue <= kubeConfig.blue
                && game.maxGreen <= kubeConfig.green
    }

    private fun pulls(game: String): Map<KubeColor, Int> {
        return game
            .split(';')
            .flatMap { pulls -> pulls.split(',') }
            .map { pull ->
                val (count, colorName) = pull.trim().split(' ')
                val color = when (colorName) {
                    "red" -> RED
                    "green" -> GREEN
                    "blue" -> BLUE
                    else -> throw Exception()
                }

                color to count.trim().toInt()
            }

            .groupingBy { it.first }
            .reduce { _, max, next -> if (next.second > max.second) next else max }
            .values
            .toMap()
    }

    private fun minimumPossibleGames(games: List<String>): Long {
        return games.sumOf { game ->
            val pulls = game.drop(game.indexOf(':') + 1).trim()
            val result = pulls(pulls)
            KubeGame(1, result[RED] ?: 0, result[GREEN] ?: 0, result[BLUE] ?: 0).power
        }
    }

    private enum class KubeColor {
        RED, GREEN, BLUE
    }

    private data class KubePull(val count: Int, val color: KubeColor)

    private data class KubeGame(val id: Int, val maxRed: Int, val maxGreen: Int, val maxBlue: Int) {
        val power: Long = maxRed.toLong() * maxGreen.toLong() * maxBlue.toLong()
    }

    private data class KubeConfig(val red: Int, val green: Int, val blue: Int)
}