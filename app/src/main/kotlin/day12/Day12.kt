package day12

import common.InputRepo
import common.readSessionCookie
import common.solve

var foundPaths = ArrayList<List<Edge>>()

data class Edge(val source: String, val target: String) {
    override fun toString(): String {
        return "$source -> $target"
    }

    fun invert(): Edge {
        return Edge(target, source)
    }
}

private fun parseLine(line: String): Edge {
    val split = line.split("-")
    return Edge(split[0], split[1])
}

fun main(args: Array<String>) {
    val day = 12
    val input = InputRepo(args.readSessionCookie()).get(day = day)

    solve(day, input, ::solveDay12Part1, ::solveDay12Part2)
}


fun solveDay12Part1(input: List<String>): Int {
    foundPaths = ArrayList()
    val normalEdges = input.map(::parseLine)
    val inverted = normalEdges.map { it.invert() }
    val edges = normalEdges + inverted

    findPaths(emptyList(), edges)
    println("start, $foundPaths")

    return foundPaths.size

}

fun findPaths(previousPath: List<Edge>, edges: Collection<Edge>): List<Edge> {
    val currentNode: String
    if (previousPath.isEmpty()) {
        currentNode = "start"
    } else {
        currentNode = previousPath.last().target
        if (currentNode == "end") {
            foundPaths.add(previousPath)
            return previousPath
        }
    }

    val notAllowed = previousPath.map { it.source }.filter { it == it.lowercase() }.toSet()
    val reachable = edges
        .filter { it.source == currentNode }
        .filter { !notAllowed.contains(it.target) }
    for (next in reachable) {
        println(previousPath + next)
        findPaths(previousPath + next, edges)
    }
    return emptyList()
}

fun solveDay12Part2(input: List<String>): Int {
    foundPaths = ArrayList()
    val normalEdges = input.map(::parseLine)
    val inverted = normalEdges.map { it.invert() }
    val edges = normalEdges + inverted

    findPathsPart2(emptyList(), edges)

    return foundPaths.size
}

fun findPathsPart2(previousPath: List<Edge>, edges: Collection<Edge>): List<Edge> {
    val currentNode: String
    if (previousPath.isEmpty()) {
        currentNode = "start"
    } else {
        currentNode = previousPath.last().target
        if (currentNode == "end") {
            foundPaths.add(previousPath)
            return previousPath
        }
    }

    val notAllowed: Set<String>
    val previousSmallCaves = previousPath.map { it.source }.plus(currentNode).filter { it == it.lowercase() }
    if (previousSmallCaves.size == previousSmallCaves.distinct().size) {
        // no duplicates, allow all small caves
        notAllowed = setOf("start")
    } else {
        notAllowed = previousPath.map { it.source }.filter { it == it.lowercase() }.toSet()
    }
    val reachable = edges
        .filter { it.source == currentNode }
        .filter { !notAllowed.contains(it.target) }
    for (next in reachable) {
        println(previousPath + next)
        findPathsPart2(previousPath + next, edges)
    }
    return emptyList()
}