import util.loadResource
import java.util.*

object Day4 {
    private val digits = Regex("\\d+")

    @JvmStatic
    fun main(args: Array<String>) {
        val lines = loadResource("day4")
        val scratchCards = lines.associate { parseInput(it) }
        println("Part 1: ${scratchCards.values.sumOf { it.winner }}")
        println("Part 2: ${properRuleScoring(scratchCards)}")
    }

    private fun parseInput(input: String): Pair<Int, ScratchCard> {
        val (game, card) = input.split(":")
        val id = game.drop(4).trim().toInt()
        val (numbers, checks) = card.split('|')
            .map {
                digits.findAll(it).map { number -> number.value.toLong() }.toList()
            }

        val winners = numbers.filter { checks.contains(it) }

        return id to ScratchCard(id, winners)
    }

    private fun properRuleScoring(cards: Map<Int, ScratchCard>): Int {

        val queue: Queue<ScratchCard> = LinkedList()
        queue.addAll(cards.values)

        var cardCount = 0

        while (queue.peek() != null) {
            val card: ScratchCard = queue.poll()
            cardCount++
            if (card.wins == 0) continue

            for (i in card.id + 1..(card.id + card.wins)) {
                cards[i]?.let { queue.add(cards[i]) }
            }
        }

        return cardCount
    }

    private data class ScratchCard(val id: Int, val winners: List<Long>) {
        val winner = winners.fold(0L) { score, _ -> if (score == 0L) 1L else score * 2L }
        val wins = winners.size
    }
}