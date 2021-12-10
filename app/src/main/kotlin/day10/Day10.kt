package day10

import common.InputRepo
import common.readSessionCookie
import common.solve
import java.util.*

fun main(args: Array<String>) {
    val day = 10
    val input = InputRepo(args.readSessionCookie()).get(day = day)

    solve(day, input, ::solveDay10Part1, ::solveDay10Part2)
}

fun solveDay10Part1(input: List<String>): Int {
    val openToClosed = mapOf(
        '(' to ')',
        '[' to ']',
        '{' to '}',
        '<' to '>',
    )

    val pointsLookup = mapOf(
        ')' to 3,
        ']' to 57,
        '}' to 1197,
        '>' to 25137,
    )

    return input.map { line ->
        val expect = Stack<Char>()
        var points = 0
        for (c in line.toCharArray()) {
            if (openToClosed[c] != null) {
                expect.push(openToClosed[c])
            } else if (expect.peek() == c) {
                expect.pop()
            } else {
                points = pointsLookup[c]!!
                break
            }
        }
        points
    }.sum()
}

fun solveDay10Part2(input: List<String>): Long {
    val openToClosed = mapOf(
        '(' to ')',
        '[' to ']',
        '{' to '}',
        '<' to '>',
    )

    val scoreLookup = mapOf(
        ')' to 1,
        ']' to 2,
        '}' to 3,
        '>' to 4,
    )

    val sortedScores = input.map { line ->
        val expect = Stack<Char>()
        var score: Long = 0
        var valid = true
        for (c in line.toCharArray()) {
            if (openToClosed[c] != null) {
                expect.push(openToClosed[c])
                print(c)
            } else if (expect.peek() == c) {
                expect.pop()
            } else {
                valid = false
                break
            }
        }
        if (expect.isNotEmpty() and valid) {
            repeat(expect.size) { score = score * 5 + scoreLookup[expect.pop()]!! }
        }
        score
    }.filter { score -> score > 0 }.sorted()

    return sortedScores[(sortedScores.size - 1) / 2]
}