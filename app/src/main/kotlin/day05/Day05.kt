package day05

import common.InputRepo
import common.readSessionCookie
import common.solve
import kotlin.math.abs
import kotlin.math.max

fun main(args: Array<String>) {
    val day = 5
    val input = InputRepo(args.readSessionCookie()).get(day = day)

    solve(day, input, ::solveDay05Part1, ::solveDay05Part2)
}

data class Line(val start: Coord, val end: Coord) {
    fun isNonDiagonal(): Boolean {
        return (start.x == end.x) or (start.y == end.y)
    }

    fun getAffectedCoords(): Collection<Coord> {
        val affectedCoords = ArrayList<Coord>()

        fun calcStepSize(from: Int, to: Int): Int = when {
            from < to -> 1
            from == to -> 0
            else -> -1
        }
        val stepX = calcStepSize(start.x, end.x)
        val stepY = calcStepSize(start.y, end.y)

        val xDiff = abs(start.x - end.x)
        val yDiff = abs(start.y - end.y)

        // Note, this assumes that all diagonal lines are 45°.
        val numSteps: Int = max(xDiff, yDiff)
        for (i in 0..numSteps) {
            affectedCoords.add(Coord(start.x + stepX * i, start.y + stepY * i))
        }

        return affectedCoords
    }
}

data class Coord(val x: Int, val y: Int)

private fun parseLine(line: String): Line {
    val regex = """(\d+),(\d+) -> (\d+),(\d+)""".toRegex()
    val matchResult = regex.find(line)
    val (x1, y1, x2, y2) = matchResult!!.destructured

    return Line(Coord(x1.toInt(), y1.toInt()), Coord(x2.toInt(), y2.toInt()))
}

fun solveDay05Part1(input: List<String>): Int {
    val coverMap = HashMap<Coord, Int>()

    input.map { line -> parseLine(line) }
            .filter(Line::isNonDiagonal)
            .forEach { line ->
                line.getAffectedCoords()
                        .forEach { coord -> coverMap.compute(coord) { _, cnt -> if (cnt == null) 1 else cnt + 1 } }
            }

    return coverMap.count { entry -> entry.value >= 2 }
}

fun solveDay05Part2(input: List<String>): Int {
    val coverMap = HashMap<Coord, Int>()

    input.map { line -> parseLine(line) }
            .forEach { line ->
                line.getAffectedCoords()
                        .forEach { coord -> coverMap.compute(coord) { _, cnt -> if (cnt == null) 1 else cnt + 1 } }
            }

    return coverMap.count { entry -> entry.value >= 2 }
}