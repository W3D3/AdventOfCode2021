package day11

import common.InputRepo
import common.readSessionCookie
import common.solve

data class Coord(val x: Int, val y: Int)

class Octopus(val pos: Coord, var energy: Int) {
    private val flashedAtTime: MutableSet<Int> = mutableSetOf()

    fun increase(time: Int): Boolean {
        energy++
        if ((energy > 9) and !flashedAtTime.contains(time)) {
            return true
        }
        return false
    }

    fun flash(time: Int): Set<Coord> {
        if ((energy > 9) and !flashedAtTime.contains(time)) {
            flashedAtTime.add(time)
            return neighbors
        }
        return emptySet()
    }

    fun resetIfFlashed(time: Int) {
        if (flashedAtTime.contains(time)) {
            energy = 0
        }
    }

    val neighbors: Set<Coord> by lazy {
        mutableSetOf(
            Coord(pos.x + 1, pos.y),
            Coord(pos.x, pos.y + 1),
            Coord(pos.x - 1, pos.y),
            Coord(pos.x, pos.y - 1),
            Coord(pos.x + 1, pos.y + 1),
            Coord(pos.x - 1, pos.y - 1),
            Coord(pos.x - 1, pos.y + 1),
            Coord(pos.x + 1, pos.y - 1),
        )
            .filter { coord -> (coord.x >= 0) and (coord.x < 10) }
            .filter { coord -> (coord.y >= 0) and (coord.y < 10) }
            .toSet()
    }

    override fun toString(): String {
        return energy.toString()
    }
}

fun main(args: Array<String>) {
    val day = 11
    val input = InputRepo(args.readSessionCookie()).get(day = day)

    solve(day, input, ::solveDay11Part1, ::solveDay11Part2)
}

private fun parseInput(input: List<String>): List<List<Octopus>> {
    return input.mapIndexed { x, line -> line.mapIndexed { y, c -> Octopus(Coord(x, y), c.digitToInt()) } }
}

private fun printMap(map: List<List<Octopus>>) {
    map.forEach { line -> line.forEach { print(it) }; println() }
    println()
}

fun solveDay11Part1(input: List<String>): Int {
    val map = parseInput(input)
    printMap(map)

    val flashCount = IntArray(100)

    for (t in 0 until 100) {
        var toFlash = map.flatten()
            .map { octopus -> if (octopus.increase(t)) octopus else null }
            .filterNotNull()
            .toMutableSet()

        do {
            flashCount[t] += toFlash.size
            println("Flashing ${flashCount[t]} at time $t")
            val toIncrease = toFlash.flatMap { octopus -> octopus.flash(t) }
            val toFlashNextIteration = mutableSetOf<Octopus>()
            for ((x, y) in toIncrease) {
                val octopus = map[x][y]
                if (octopus.increase(t)) {
                    toFlashNextIteration.add(octopus)
                }
            }
            toFlash = toFlashNextIteration
        } while (toFlash.isNotEmpty())
        map.flatten()
            .forEach { octopus -> octopus.resetIfFlashed(t) }
        printMap(map)
    }

    return flashCount.sum()
}

fun solveDay11Part2(input: List<String>): Int {
    val map = parseInput(input)
    printMap(map)

    val flashCount = IntArray(10000)

    // TODO fix & refactor
    for (t in 0 until 10000) {
        var toFlash = map.flatten()
            .map { octopus -> if (octopus.increase(t)) octopus else null }
            .filterNotNull()
            .toMutableSet()

        do {
            flashCount[t] += toFlash.size
            println("Flashing ${flashCount[t]} at time $t")
            val toIncrease = toFlash.flatMap { octopus -> octopus.flash(t) }
            val toFlashNextIteration = mutableSetOf<Octopus>()
            for ((x, y) in toIncrease) {
                val octopus = map[x][y]
                if (octopus.increase(t)) {
                    toFlashNextIteration.add(octopus)
                }
            }
            toFlash = toFlashNextIteration
        } while (toFlash.isNotEmpty())
        map.flatten()
            .forEach { octopus -> octopus.resetIfFlashed(t) }
        printMap(map)

        if (flashCount[t] == 100) {
            return t + 1 // adjust that our days start with 0
        }
    }

    return flashCount.indexOfFirst { it == 10 * 10 }
}