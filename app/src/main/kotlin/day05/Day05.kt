package day05

import common.InputRepo
import common.readSessionCookie
import common.solve
import kotlin.math.abs

fun main(args: Array<String>) {
    val day = 5
    val input = InputRepo(args.readSessionCookie()).get(day = day)

    solve(day, input, ::solveDay05Part1, ::solveDay05Part2)
}

data class Line(val beginning: Coord, val end: Coord) {
    fun isNonDiagonal(): Boolean {
        return (beginning.x == end.x) or (beginning.y == end.y)
    }

    fun getAffectedCoords(): Collection<Coord> {
        val affectedCoords = ArrayList<Coord>()

        val step_x = if (beginning.x < end.x) 1 else if (beginning.x == end.x) 0 else -1
        val step_y = if (beginning.y < end.y) 1 else if (beginning.y == end.y) 0 else -1

        val x_diff = beginning.x - end.x
        val y_diff = beginning.y - end.y
        // lmao 45 degree hax below
        val distance: Int = (abs(x_diff) + abs(y_diff)) / if (isNonDiagonal()) 1 else 2
        for (i in 0..distance) {
            affectedCoords.add(Coord(beginning.x + step_x * i, beginning.y + step_y * i))
        }

        return affectedCoords
    }
}

data class Coord(val x: Int, val y: Int)

fun parseLine(line: String): Line {
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
                        .forEach { coord -> coverMap.compute(coord) { _, v -> if (v == null) 1 else v + 1 } }
            }

    return coverMap.count { entry -> entry.value >= 2 }
}

fun solveDay05Part2(input: List<String>): Int {
    val coverMap = HashMap<Coord, Int>()

    input.map { line -> parseLine(line) }
            .forEach { line ->
                line.getAffectedCoords()
                        .forEach { coord -> coverMap.compute(coord) { _, v -> if (v == null) 1 else v + 1 } }
            }

    return coverMap.count { entry -> entry.value >= 2 }
}