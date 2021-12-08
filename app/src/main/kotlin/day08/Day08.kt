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
    val outputPattern = line.split("|")[1].trim().split(" ").map { s -> s.toCharArray().toSet() }
    return InputLine(uniquePatters, outputPattern)
}

fun solveDay08Part1(input: List<String>): Int {
    return input.sumOf { line ->
        parseSignals(line).outputValues.count { chars -> chars.size in setOf(2, 4, 3, 7) }
    }
}

fun solveDay08Part2(input: List<String>): Int {
    return input.sumOf { line ->
        val (patterns, outputPatterns) = parseSignals(line)
        val sizeToPattern = patterns.groupBy { p -> p.size }

        val numToPattern = generateNumToPattern(sizeToPattern)

        val patternToNum = numToPattern.entries.associate { (k, v) -> v to k }

        outputPatterns.withIndex().sumOf { (index, outputPattern) ->
            patternToNum[outputPattern]!! * 10.0.pow(outputPatterns.size - (index + 1)).toInt()
        }
    }
}

private fun generateNumToPattern(sizeToPattern: Map<Int, List<Set<Char>>>): HashMap<Int, Set<Char>> {
    val numToPattern = HashMap<Int, Set<Char>>()

    // 1, 4, 7, 8 are unique considering size of lines.
    numToPattern[1] = sizeToPattern[2]!!.first()
    numToPattern[4] = sizeToPattern[4]!!.first()
    numToPattern[7] = sizeToPattern[3]!!.first()
    numToPattern[8] = sizeToPattern[7]!!.first()

    val rightLine = numToPattern[1]!!   // Alias 1 to rightLine, to make things more readable

    numToPattern[3] = sizeToPattern[5]!!.first { chars -> chars.containsAll(rightLine) }    // 3 is the only 5 line digit with the right line turned on

    val topL = numToPattern[4]!! subtract rightLine
    numToPattern[5] = sizeToPattern[5]!!.first { chars -> chars.containsAll(topL) }                                         // 5 is the only 5 line digit with a top L shape
    numToPattern[2] = (sizeToPattern[5]!! subtract setOf(numToPattern[5]!!, numToPattern[3]!!)).first()                     // 2 is the only 5 line digit left
    numToPattern[9] = sizeToPattern[6]!!.first { chars -> chars.containsAll(numToPattern[4]!!) }                            // 9 is the only 6 line digit with a 4 shape in it
    numToPattern[0] = sizeToPattern[6]!!.first { chars -> (chars != numToPattern[9]) and chars.containsAll(rightLine) }     // 0 is the only other 6 line digit with a right line (that is not 9)
    numToPattern[6] = sizeToPattern[6]!!.subtract(setOf(numToPattern[9]!!, numToPattern[0]!!)).first()                      // 6 is the only 6 line digit left
    return numToPattern
}