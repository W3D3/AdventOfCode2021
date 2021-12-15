package day13

import common.InputRepo
import common.readSessionCookie
import common.solve

fun main(args: Array<String>) {
    val day = 13
    val input = InputRepo(args.readSessionCookie()).get(day = day)

    solve(day, input, ::solveDay13Part1, ::solveDay13Part2)
}

data class Coord(val x: Int, val y: Int)
data class Fold(val axis: Char, val value: Int)


data class Input(val dots: Set<Coord>, val folds: List<Fold>)

private fun parseInput(input: List<String>): Input {
    val coordRegex = """(\d+),(\d+)""".toRegex()
    val foldRegex = """fold along (\w)=(\d+)""".toRegex()

    val (coordinatesRaw, foldsRaw) = input.partition { (it != "\n") and it.contains(",") }
    val dots = coordinatesRaw.filter { it.isNotEmpty() }.map {
        val (x, y) = coordRegex.find(it)!!.destructured
        Coord(x.toInt(), y.toInt())
    }.toSet()
    val folds = foldsRaw.filter { it.isNotEmpty() }.map {
        println(it)
        val (axis, value) = foldRegex.find(it)!!.destructured
        Fold(axis.toCharArray().first(), value.toInt())
    }
    return Input(dots, folds)
}

private fun foldDot(location: Coord, fold: Fold): Coord {
    when (fold.axis) {
        'x' -> {
            if (location.x < fold.value) return location
            return Coord(fold.value - (location.x - fold.value), location.y)
        }
        'y' -> {
            if (location.y < fold.value) return location
            return Coord(location.x, fold.value - (location.y - fold.value))
        }
        else -> throw UnsupportedOperationException("Does not support folding on axis ${fold.axis}")
    }
}

private fun printDots(dots: Set<Coord>) {
    val maxX = dots.maxOf { it.x }
    val maxY = dots.maxOf { it.y }

    for (y in 0..maxY) {
        for (x in 0..maxX) {
            if (dots.contains(Coord(x, y))) print("#") else print(" ")
        }
        println()
    }

}

fun solveDay13Part1(input: List<String>): Int {
    val puzzle = parseInput(input)

    var dots = puzzle.dots
    for (fold in puzzle.folds) {
        dots = dots.map { foldDot(it, fold) }.toSet()
    }

    printDots(dots)
    return dots.size
}

fun solveDay13Part2(input: List<String>): Int {
    val puzzle = parseInput(input)

    val firstFold = puzzle.folds.first()
    val dots = puzzle.dots.map { foldDot(it, firstFold) }.toSet()

    return dots.size
}