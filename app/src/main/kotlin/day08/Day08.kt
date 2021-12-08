package day08

import common.InputRepo
import common.readSessionCookie
import common.solve
import kotlin.math.pow

fun main(args: Array<String>) {
    val day = 8
    val input = InputRepo(args.readSessionCookie()).get(day = day)

    solve(day, input, ::solveDay08Part1, ::solveDay08Part2)
}

data class InputLine(val patterns: Collection<Set<Char>>, val outputValues: Collection<Set<Char>>)

private fun parseSignals(line: String): InputLine {
    val uniquePatters = line.split("|")[0].trim().split(" ").map { s -> s.toCharArray().toSet() }
    val outputValue = line.split("|")[1].trim().split(" ").map { s -> s.toCharArray().toSet() }
    return InputLine(uniquePatters, outputValue)
}

fun solveDay08Part1(input: List<String>): Int {
    return input.sumOf { line ->
        parseSignals(line).outputValues.count { chars -> chars.size in setOf(2, 4, 3, 7) }
    }
}

fun solveDay08Part2(input: List<String>): Int {
    var sum = 0
    for (line in input) {
        val (patterns, outputValues) = parseSignals(line)
        val sizeToPattern = patterns.groupBy { p -> p.size }

        print(sizeToPattern)
        var numToPattern = HashMap<Int, Set<Char>>()
        // 1, 4, 7, 8 are unique
        // Find top row (
        val pattern1 = sizeToPattern[2]!![0]
        val pattern7 = sizeToPattern[3]!![0]
        val pattern4 = sizeToPattern[4]!![0]
        val pattern8 = sizeToPattern[7]!![0]
        val rightLine = pattern1 intersect pattern7
        // 3 is the only 5 line digit with the right line turned on
        val pattern3 = sizeToPattern[5]?.filter { chars -> chars.containsAll(rightLine) }?.first()
        // 5 is the only 5 line digit with a top L shape
        val topL = pattern4 subtract rightLine
        val pattern5 = sizeToPattern[5]?.filter { chars -> chars.containsAll(topL) }?.first()
        // 2 is the only 5 pattern left
        val pattern2 = sizeToPattern[5]?.subtract(listOf(pattern5, pattern3).toSet())?.first()
//        val pattern2 = sizeToPattern[5]?.filter { chars -> chars != pattern5 && chars != pattern3}

        // 9 is the only 6 line digit with a top L shape and the right line
        val pattern9 = sizeToPattern[6]?.filter { chars -> chars.containsAll(topL union rightLine) }?.first()
        // 0 is the only other 6 line digit with a right line (that is not 9)
        val pattern0 = sizeToPattern[6]?.filter { chars -> (chars != pattern9) and chars.containsAll(rightLine) }?.first()
        // 6 is the only 6 line digit left
        val pattern6 = sizeToPattern[6]?.subtract(listOf(pattern9, pattern0).toSet())?.first()

        var value = 0
        val ten = 10.0
        outputValues.withIndex().forEach { (index, outputValue) ->
            when (outputValue) {
                // for the love of god, refactor this.
                pattern1 -> { value += 1 * ten.pow(outputValues.size - (index + 1)).toInt()}
                pattern2 -> { value += 2 * ten.pow(outputValues.size - (index + 1)).toInt()}
                pattern3 -> { value += 3 * ten.pow(outputValues.size - (index + 1)).toInt()}
                pattern4 -> { value += 4 * ten.pow(outputValues.size - (index + 1)).toInt()}
                pattern5 -> { value += 5 * ten.pow(outputValues.size - (index + 1)).toInt()}
                pattern6 -> { value += 6 * ten.pow(outputValues.size - (index + 1)).toInt()}
                pattern7 -> { value += 7 * ten.pow(outputValues.size - (index + 1)).toInt()}
                pattern8 -> { value += 8 * ten.pow(outputValues.size - (index + 1)).toInt()}
                pattern9 -> { value += 9 * ten.pow(outputValues.size - (index + 1)).toInt()}
                else -> {}
            }
        }
        sum += value
        println(value)
    }

    return sum
}