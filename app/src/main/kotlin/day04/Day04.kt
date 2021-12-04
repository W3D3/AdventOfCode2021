package day04

import common.InputRepo
import common.readSessionCookie
import common.solve
import java.util.function.Consumer

fun main(args: Array<String>) {
    val day = 4
    val input = InputRepo(args.readSessionCookie()).get(day = day)

    solve(day, input, ::solveDay04Part1, ::solveDay04Part2)
}

data class BingoTile(val value: Int, var marked: Boolean) {
    override fun toString(): String {
        return value.toString()
    }
}

fun markTile(board: List<List<BingoTile>>, drawn: Int) {
    board.forEach(Consumer { row -> row.forEach(Consumer { tile -> tile.marked = (tile.value == drawn) or tile.marked }) })
}

fun checkWinner(board: List<List<BingoTile>>): Boolean {
    if (board.any { row -> row.all { tile -> tile.marked } }) {
        return true
    }
    for (i in 0..4) {
        if (board.all { row -> row[i].marked }) {
            return true
        }
    }
    return false
}

fun sumNonMarked(board: List<List<BingoTile>>): Int {
    return board.sumOf { row -> row.sumOf { tile -> if (tile.marked) 0 else tile.value } }
}

private fun parseInput(input: List<String>): Pair<List<Int>, List<List<List<BingoTile>>>> {
    val drawn = input.first().split(",").map(String::toInt)

    val bingoBoards = input.drop(1)
            .filterNot { s -> s.isBlank() }
            .map { s ->
                s.trim().split("\\s+".toRegex())
                        .map { numStr -> BingoTile(numStr.toInt(), false) }
            }
            .chunked(5)
    return Pair(drawn, bingoBoards)
}

fun solveDay04Part1(input: List<String>): Int {
    val (drawn, bingoBoards) = parseInput(input)

    for (drawnBall in drawn) {
        for (bingoBoard in bingoBoards) {
            markTile(bingoBoard, drawnBall)
        }

        for (bingoBoard in bingoBoards) {
            if (checkWinner(bingoBoard)) {
                return sumNonMarked(bingoBoard) * drawnBall
            }
        }
    }

    return -1
}

fun solveDay04Part2(input: List<String>): Int {
    val (drawn, bingoBoards) = parseInput(input)

    val winningBoardsIndices = HashSet<Int>()
    for (drawnBall in drawn) {
        for (bingoBoard in bingoBoards) {
            markTile(bingoBoard, drawnBall)
        }

        for (bingoBoard in bingoBoards) {
            if (checkWinner(bingoBoard)) {
                winningBoardsIndices.add(bingoBoards.indexOf(bingoBoard))
                if (winningBoardsIndices.size == bingoBoards.size) {
                    return sumNonMarked(bingoBoard) * drawnBall
                }
            }
        }
    }
    return -1
}