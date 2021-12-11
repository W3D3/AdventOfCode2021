package day10

import common.InputRepo
import common.readSessionCookie
import common.solve
import java.util.*

val openToClosed = mapOf(
    '(' to ')',
    '[' to ']',
    '{' to '}',
    '<' to '>',
)

fun main(args: Array<String>) {
    val day = 10
    val input = InputRepo(args.readSessionCookie()).get(day = day)

    solve(day, input, ::solveDay10Part1, ::solveDay10Part2)
}

fun solveDay10Part1(input: List<String>): Int {
    val pointsLookup = mapOf(
        ')' to 3,
        ']' to 57,
        '}' to 1197,
        '>' to 25137,
    )

    fun lineToScore(line: String): Int {
        val expect = Stack<Char>()
        for (c in line.toCharArray()) {
            if (openToClosed[c] != null) {
                expect.push(openToClosed[c])
            } else if (expect.peek() == c) {
                expect.pop()
            } else {
                return pointsLookup[c]!!
            }
        }
        return 0
    }

    return input.map(::lineToScore).sum()
}

fun solveDay10Part2(input: List<String>): Long {
    val scoreLookup = mapOf(
        ')' to 1,
        ']' to 2,
        '}' to 3,
        '>' to 4,
    )

    fun lineToScore(line: String): Long {
        val expect = Stack<Char>()
        var score: Long = 0
        var valid = true
        for (char in line) {
            if (openToClosed[char] != null) {
                expect.push(openToClosed[char])
            } else {
                valid = expect.pop() == char
                if (!valid) break
            }
        }
        if (expect.isNotEmpty() and valid) {
            repeat(expect.size) { score = score * 5 + scoreLookup[expect.pop()]!! }
        }
        return score
    }

    val sortedScores = input
        .map(::lineToScore)
        .filter { score -> score > 0 }
        .sorted()

    return sortedScores[(sortedScores.size - 1) / 2]
}