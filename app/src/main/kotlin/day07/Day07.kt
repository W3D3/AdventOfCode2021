package day07

import common.InputRepo
import common.readSessionCookie
import common.solve
import java.util.Collections.max
import java.util.Collections.min
import kotlin.math.abs

fun main(args: Array<String>) {
    val day = 7
    val input = InputRepo(args.readSessionCookie()).get(day = day)

    solve(day, input, ::solveDay07Part1, ::solveDay07Part2)
}


fun solveDay07Part1(input: List<String>): Int {
    return median(input)
//    return bruteforce(input)
}

private fun bruteforce(input: List<String>): Int {
    val positions = parsePositions(input)

    var min: Int = Int.MAX_VALUE

    for (position in positions) {
        val sumOf = positions.sumOf { abs(it - position) }
        if (sumOf < min) {
            min = sumOf;
        }
    }

    return min
}

fun median(input: List<String>): Int {
    val positions = parsePositions(input)

    var median = positions.sorted()[positions.size / 2]

    return positions.sumOf { abs(it - median) }
}

fun solveDay07Part2(input: List<String>): Int {
    return bruteforce2(input);
}

private fun bruteforce2(input: List<String>): Int {
    val positions = parsePositions(input).sorted()

    var min: Int = Int.MAX_VALUE

    print(positions)
    for (goal in min(positions)..max(positions)) {
        val sumOf = positions.sumOf { pos ->
            val sum = generateSequence(0) { if (it < abs(pos - goal)) it + 1 else null }.sum()
            println("$sum fuel for goal $goal from $pos")
            sum
        }
        println("===============================")
        println("Total: $sumOf")
        if (sumOf < min) {
            min = sumOf;
            println("NEW MIN: $sumOf")
        } else {
            // no new min in the sorted list means, we're done
            break
        }
    }

    return min
}

private fun parsePositions(input: List<String>) =
    input.map { line -> line.split(",").map(String::toInt) }.flatten()
