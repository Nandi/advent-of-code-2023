import util.loadResource

object Day7 {
    private var cardValues = arrayOf('A', 'K', 'Q', 'J', 'T', '9', '8', '7', '6', '5', '4', '3', '2').reversed()

    @JvmStatic
    fun main(args: Array<String>) {
        val lines = loadResource("day7")
        println("Part 1: ${camelCards(catalog(lines))}")

        cardValues = arrayOf('A', 'K', 'Q', 'T', '9', '8', '7', '6', '5', '4', '3', '2', 'J').reversed()
        println("Part 2: ${camelCards(catalog(lines, true))}")
    }

    private fun catalog(lines: List<String>, jokers: Boolean = false): List<Hand> {
        return lines.map {
            val (hand, bid) = it.split(" ")
            Hand(hand, bid.toLong(), jokers)
        }
    }

    private fun camelCards(hands: List<Hand>): Long {
        val ranking = hands.sorted()
        ranking.forEach(::println)
        return ranking.mapIndexed { i, hand -> (i + 1) to hand.bid }.sumOf { it.first * it.second }
    }

    private class Hand(val hand: String, val bid: Long, jokers: Boolean) : Comparable<Hand> {
        private val cards = hand.groupingBy { it }.eachCount().toMutableMap()

        init {
            if (jokers) {
                // Don't look at my shame!
                val max = cards.filter { it.key != 'J' }.map { it }.sortedWith(compareBy({ it.value }, { cardValues.indexOf(it.key) })).lastOrNull()
                // Special case where hand is only jokers
                if (max == null) cards['A'] = 5
                else {
                    cards[max.key] = cards[max.key]!! + hand.count { it == 'J' }
                    cards.remove('J')
                }
            }
        }

        val strength by lazy { strength() }

        fun strength(): Int {
            // Five of a kind
            return if (cards.any { it.value == 5 }) {
                7
                // Four of a kind
            } else if (cards.any { it.value == 4 }) {
                6
                // Full house
            } else if (cards.any { it.value == 3 } && cards.any { it.value == 2 }) {
                5
                // Three of a kind
            } else if (cards.any { it.value == 3 }) {
                4
                // Two pairs
            } else if (cards.filter { it.value == 2 }.size == 2) {
                3
                // A pair
            } else if (cards.any { it.value == 2 }) {
                2
                // High card
            } else {
                1
            }
        }

        override fun compareTo(other: Hand): Int {
            var test = strength - other.strength
            if (test != 0) return test

            for ((i, c) in hand.withIndex()) {
                test = cardValues.indexOf(c).compareTo(cardValues.indexOf(other.hand[i]))
                if (test != 0) return test
            }

            return 0
        }

        override fun toString(): String {
            return "$hand: ${strength()}"
        }
    }
}