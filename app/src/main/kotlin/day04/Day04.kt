package day04

import common.InputRepo
import common.readSessionCookie
import common.solve

fun main(args: Array<String>) {
    val day = 4
    val input = InputRepo(args.readSessionCookie()).get(day = day)

    solve(day, input, ::solveDay04Part1, ::solveDay04Part2)
}

data class BingoTile(val value: Int, val marked: Boolean) {
    override fun toString(): String {
        return value.toString()
    }
}

fun solveDay04Part1(input: List<String>): Int {
    var drawn = input.first().split(",").map(String::toInt)

    var bingoBoards = input.drop(1).map { s ->
        if (s != "") {
            s
        } else {
            null
        }
    }.filterNotNull()
        .map { s -> s.split("\\s+".toRegex()).map { numStr -> BingoTile(numStr.toInt(), false) } }
        .chunked(5);

    println(bingoBoards)



    return 0
}

fun solveDay04Part2(input: List<String>): Int {
    TODO()
}