package day14

import common.InputRepo
import common.readSessionCookie
import common.solve


data class Input(val template: String, val rules: Map<String, String>)

private fun parseInput(input: List<String>): Input {
    val rulesRegex = """(\w+) -> (\w+)""".toRegex()

    val (templateRaw, foldsRaw) = input.partition { (it != "") and !it.contains("->") }
    val template = templateRaw.first()
    val rules = foldsRaw.associate {
        println(it)
        val (from, to) = rulesRegex.find(it)!!.destructured
        from to to
    }
    return Input(template, rules)
}

fun main(args: Array<String>) {
    val day = 14
    val input = InputRepo(args.readSessionCookie()).get(day = day)

    solve(day, input, ::solveDay14Part1, ::solveDay14Part2)
}


fun solveDay14Part1(input: List<String>): Long {
    val (template, rules) = parseInput(input)

    var countPairs: HashMap<String, Long>
    repeat(10) {

    }
}

fun solveDay14Part2(input: List<String>): Int {
    TODO()
}