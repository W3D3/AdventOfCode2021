package day03

import common.InputRepo
import common.readSessionCookie
import common.solve
import kotlin.math.ceil

fun main(args: Array<String>) {
    val day = 3
    val input = InputRepo(args.readSessionCookie()).get(day = day)

    solve(day, input, ::solveDay03Part1, ::solveDay03Part2)
}

fun solveDay03Part1(input: List<String>): Int {
    val total = input.size
    val countPerPosition = HashMap<Int, Int>()
    for (bitsString in input) {
        for ((index, bit) in bitsString.withIndex()) {
            if (bit.digitToInt() == 1) {
                countPerPosition.computeIfPresent(index) { _, v -> v + 1 }
                countPerPosition.putIfAbsent(index, 1)
            }
        }
    }

    val gammaBits = countPerPosition
        .map { entry -> entry.value > total / 2 }
        .map { b -> if (b) 1 else 0 }
        .toList()
    val epsilonBits = gammaBits
        .map { bit -> if (bit == 1) 0 else 1 }

    val gammaRate = gammaBits.joinToString("").toInt(2)
    val epsilonRate = epsilonBits.joinToString("").toInt(2)

    return gammaRate * epsilonRate
}

/**
 * Calculates the rating
 *
 * @param input the list of strings containing the binary numbers of the same length
 * @param bitToAppend bit to append if the majority of an index contains ones (true = 1, false = 0)
 */
fun calculateRating(input: List<String>, bitToAppend: Boolean): Int {
    var bitStrings = input
    var prefix = ""
    var index = 0

    val bitCharToAppendOnMajority = if (bitToAppend) '1' else '0'
    val bitCharToAppendOnMinority = if (bitToAppend) '0' else '1'

    while (bitStrings.size > 1) {
        val majorityOne = majorityOneOnIndex(bitStrings, index)
        prefix += if (majorityOne) bitCharToAppendOnMajority else bitCharToAppendOnMinority

        bitStrings = filterForPrefix(bitStrings, prefix)
        index++
    }
    assert(bitStrings.size == 1)

    return bitStrings[0].toInt(2)
}

fun majorityOneOnIndex(bitStrings: Collection<String>, index: Int): Boolean {
    var cnt = 0
    val threshold: Int = ceil(bitStrings.size / 2.0).toInt()

    for (bitString in bitStrings) {
        if (bitString[index].digitToInt() == 1) {
            cnt++
        }
        if (cnt > threshold) {
            return true
        }
    }
    if (cnt == threshold) {
        return true
    }
    return false
}

fun filterForPrefix(bitStrings: Collection<String>, prefix: String): List<String> {
    return bitStrings.filter { s -> s.commonPrefixWith(prefix).length == prefix.length }.toList()
}

fun solveDay03Part2(input: List<String>): Int {
    val o2Rating = calculateRating(input, true)
    val co2Rating = calculateRating(input, false)

    return o2Rating * co2Rating
}
