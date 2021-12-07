package day06

import common.InputRepo
import common.readSessionCookie
import common.solve

fun main(args: Array<String>) {
    val day = 6
    val input = InputRepo(args.readSessionCookie()).get(day = day)

    solve(day, input, ::solveDay06Part1, ::solveDay06Part2)
}

fun calculateFish(startingFish: List<Int>, days: Int): Long {
    val lanternFishes = LongArray(days + 10)

    for (startFish in startingFish) {
        lanternFishes[startFish + 1] += 1L
    }

    for (i in 1 until days) {
        lanternFishes[i + 7] += lanternFishes[i]
        lanternFishes[i + 9] += lanternFishes[i]
    }

    val relevantFish = lanternFishes.copyOfRange(0, days + 1)
    return relevantFish.sum() + startingFish.count()
}

fun solveDay06Part1(input: List<String>): Long {
    val startingFish = parseStartingFish(input)

    return calculateFish(startingFish, 80)
}

fun solveDay06Part2(input: List<String>): Long {
    val startingFish = parseStartingFish(input)

    return calculateFish(startingFish, 256)
}

private fun parseStartingFish(input: List<String>) =
    input.map { line -> line.split(",").map(String::toInt) }.flatten()
