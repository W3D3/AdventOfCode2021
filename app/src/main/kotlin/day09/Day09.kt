package day09

import common.InputRepo
import common.colored
import common.readSessionCookie
import common.solve

fun main(args: Array<String>) {
    val day = 9
    val input = InputRepo(args.readSessionCookie()).get(day = day)

    solve(day, input, ::solveDay09Part1, ::solveDay09Part2)
}

private fun parseInput(input: List<String>): List<List<Int>> {
    return input.map { line -> line.map { c -> c.digitToInt() } }
}

data class Point(val x: Int, val y: Int, val height: Int)

private fun getLowPoints(map: List<List<Int>>, print: Boolean = false): ArrayList<Point> {
    val lowPoints = ArrayList<Point>()
    for (i in map.indices) {
        for (j in map[i].indices) {
            val currentPoint = map[i][j]
            val neighbors = setOf(
                pointAt(map, i + 1, j),
                pointAt(map, i, j + 1),
                pointAt(map, i - 1, j),
                pointAt(map, i, j - 1),
            ).filterNotNull()

            if (neighbors.all { it.height > currentPoint }) {
                lowPoints += Point(i, j, currentPoint)
                if (print) colored { print(currentPoint.toString().red) }
            } else {
                if (print) print(currentPoint)
            }
        }
        if (print) println()
    }
    return lowPoints
}

fun solveDay09Part1(input: List<String>): Int {
    val heightMap = parseInput(input)

    return getLowPoints(heightMap).sumOf { p -> p.height + 1 }
}

private fun findBasin(map: List<List<Int>>, point: Point, visited: Set<Point> = mutableSetOf(point)): Set<Point> {
    val i = point.x
    val j = point.y

    var newVisited: Set<Point>
    val unvisitedNeighbors = setOf(
        pointAt(map, i + 1, j),
        pointAt(map, i, j + 1),
        pointAt(map, i - 1, j),
        pointAt(map, i, j - 1),
    )
        .filterNotNull()
        .filter { value -> value.height < 9 }
        .filter { value -> !visited.contains(value) }

    if (unvisitedNeighbors.isEmpty())
        return emptySet()

    newVisited = visited union unvisitedNeighbors union setOf(point)
    for (neighbor in unvisitedNeighbors) {
        newVisited = newVisited + findBasin(map, neighbor, newVisited)
    }
    return newVisited
}

private fun pointAt(map: List<List<Int>>, x: Int, y: Int): Point? {
    val value = map.getOrNull(x)?.getOrNull(y) ?: return null
    return Point(x, y, value)
}

fun solveDay09Part2(input: List<String>): Int {
    val heightMap = parseInput(input)

    val lowPoints = getLowPoints(heightMap)

    return lowPoints.map { findBasin(heightMap, it).size }
        .sortedDescending()
        .take(3)
        .reduce { x, y -> x * y }
}
