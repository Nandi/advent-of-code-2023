import util.loadResource
import kotlin.math.max
import kotlin.math.min

object Day5 {
    @JvmStatic
    fun main(args: Array<String>) {
        val data = loadResource("day5")
        val seeds = seed(data.first())

        println("Part 1: ${seedLocation(seeds, data).min()}")
        println("Part 2: ${seedRangeLocation(seeds, data).minBy { it.first }.first}")
    }

    private fun seedLocation(seeds: List<Long>, data: List<String>): List<Long> {
        val seedToSoil = data.parseTransitions("seed-to-soil")
        val soilToFertilizer = data.parseTransitions("soil-to-fertilizer")
        val fertilizerToWater = data.parseTransitions("fertilizer-to-water")
        val waterToLight = data.parseTransitions("water-to-light")
        val lightToTemperature = data.parseTransitions("light-to-temperature")
        val temperatureToHumidity = data.parseTransitions("temperature-to-humidity")
        val humidityToLocation = data.parseTransitions("humidity-to-location")

        return seeds.asSequence()
            .map { it.transitionTo(seedToSoil) }
            .map { it.transitionTo(soilToFertilizer) }
            .map { it.transitionTo(fertilizerToWater) }
            .map { it.transitionTo(waterToLight) }
            .map { it.transitionTo(lightToTemperature) }
            .map { it.transitionTo(temperatureToHumidity) }
            .map { it.transitionTo(humidityToLocation) }
            .toList()
    }

    private fun seedRangeLocation(seeds: List<Long>, data: List<String>): List<LongRange> {
        val seedRanges = seeds.chunked(2).map { (start, size) -> start..<start + size }

        val seedToSoil = data.parseTransitions("seed-to-soil")
        val soilToFertilizer = data.parseTransitions("soil-to-fertilizer")
        val fertilizerToWater = data.parseTransitions("fertilizer-to-water")
        val waterToLight = data.parseTransitions("water-to-light")
        val lightToTemperature = data.parseTransitions("light-to-temperature")
        val temperatureToHumidity = data.parseTransitions("temperature-to-humidity")
        val humidityToLocation = data.parseTransitions("humidity-to-location")

        return seedRanges
            .asSequence()
            .flatMap { it.transitionTo(seedToSoil) }
            .flatMap { it.transitionTo(soilToFertilizer) }
            .flatMap { it.transitionTo(fertilizerToWater) }
            .flatMap { it.transitionTo(waterToLight) }
            .flatMap { it.transitionTo(lightToTemperature) }
            .flatMap { it.transitionTo(temperatureToHumidity) }
            .flatMap { it.transitionTo(humidityToLocation) }
            .toList()
    }

    private fun Long.transitionTo(transition: List<Transition>): Long {
        val destination = transition.firstOrNull { it.source.contains(this) }
        return destination?.toDestination(this) ?: this
    }

    private fun LongRange.transitionTo(transitions: List<Transition>): List<LongRange> {
        val sorted = transitions.sortedBy { it.sourceStart }
        val result = mutableListOf<LongRange>()

        if (sorted.first().sourceStart > this.first) result.add(this.first..<sorted.first().source.first)

        for (transition in sorted) {
            if (!transition.source.overlap(this)) continue
            val start = max(this.first, transition.source.first)
            val end = min(this.last, transition.source.last)
            result.add(transition.toDestination(start)..transition.toDestination(end))
        }

        if (sorted.last().source.last < this.last) result.add((sorted.last().source.last + 1)..this.last)

        return result
    }

    private fun seed(seedList: String): List<Long> = seedList.drop(7).split(" ").map { it.toLong() }

    private fun List<String>.parseTransitions(mapName: String): List<Transition> {
        return dropWhile { !it.startsWith(mapName) }
            .drop(1)
            .takeWhile { it.trim().isNotBlank() }
            .map { transition ->
                val (destinationStart, sourceStart, size) = transition.split(" ").map { it.toLong() }
                Transition(sourceStart, destinationStart, size)
            }
    }

    private fun LongRange.overlap(range: LongRange): Boolean {
        return this.first <= range.last && this.last >= range.first
    }

    private data class Transition(val sourceStart: Long, val destinationStart: Long, val size: Long) {
        val source = sourceStart..<sourceStart + size
        val destination = destinationStart..<destinationStart + size

        fun toDestination(input: Long): Long {
            return input - source.first + destination.first
        }
    }
}