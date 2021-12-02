package day02

import common.InputRepo
import common.readSessionCookie
import common.solve

fun main(args: Array<String>) {
    val day = 2
    val input = InputRepo(args.readSessionCookie()).get(day = day)

    solve(day, input, ::solveDay02Part1, ::solveDay02Part2)
}

enum class Command {
    FORWARD,
    DOWN,
    UP
}

fun parseCommand(cmd: String): Pair<Command, Int> {
    val split = cmd.split(" ")
    return Pair(Command.valueOf(split[0].uppercase()), split[1].toInt())
}

fun solveDay02Part1(input: List<String>): Int {
    var pos = 0
    var depth = 0

    for (commandLine in input) {
        val cmd = parseCommand(commandLine)
        when (cmd.first) {
            Command.FORWARD -> pos += cmd.second
            Command.DOWN -> depth += cmd.second
            Command.UP -> depth -= cmd.second
        }
    }

    return pos * depth
}

fun solveDay02Part2(input: List<String>): Int {
    var aim = 0
    var pos = 0
    var depth = 0

    for (commandLine in input) {
        val cmd = parseCommand(commandLine)
        when (cmd.first) {
            Command.FORWARD -> {
                pos += cmd.second
                depth += cmd.second * aim
            }
            Command.DOWN -> aim += cmd.second
            Command.UP -> aim -= cmd.second
        }
    }

    return pos * depth
}
