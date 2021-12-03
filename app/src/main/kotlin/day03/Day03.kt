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
    val total = input.size;
    val countPerPosition = HashMap<Int, Int>()
    for (bitsString in input) {
        for ((index, bit) in bitsString.withIndex()) {
            if (bit.digitToInt() == 1)
                countPerPosition.computeIfPresent(index) { _, v -> v + 1 }
            countPerPosition.putIfAbsent(index, 1);
        }
    }

    val gammaBits = arrayOfNulls<Int>(countPerPosition.size)
    val epsilonBits = arrayOfNulls<Int>(countPerPosition.size)
    for ((i, cnt) in countPerPosition) {
        if (cnt > total / 2) {
            gammaBits[i] = 1;
            epsilonBits[i] = 0;
        } else {
            gammaBits[i] = 0;
            epsilonBits[i] = 1;
        }
    }
    val gammaRate = gammaBits.joinToString("").toInt(2)
    val epsilonRate = epsilonBits.joinToString("").toInt(2)

    return gammaRate * epsilonRate
}

fun determineMajority(bitStrings: Collection<String>, index: Int, majorityOf: Int, tieBreak: Boolean): Boolean {
    var cnt = 0
    val threshold: Int = ceil(bitStrings.size / 2.0).toInt()

    for (bitString in bitStrings) {
        if (bitString[index].digitToInt() == majorityOf) {
            cnt++
        }
        if (cnt > threshold) {
            return true
        }
    }
    if (cnt == threshold) {
        return tieBreak
    }
    return false
}

fun filterForPrefix(bitStrings: Collection<String>, prefix: String): List<String> {
    return bitStrings.filter { s -> s.commonPrefixWith(prefix).length == prefix.length }.toList()
}

fun solveDay03Part2(input: List<String>): Int {
    var bitStrings = input;
    var prefix = "";
    var index = 0
    while (bitStrings.size > 1) {
        val majorityOne = determineMajority(bitStrings, index, 1, true)
        prefix += if (majorityOne) "1" else "0"

        bitStrings = filterForPrefix(bitStrings, prefix);
        index++
    }
    val o2Rating = bitStrings[0];
    println(o2Rating.toInt(2));

    index = 0
    prefix = ""
    bitStrings = input;
    while (bitStrings.size > 1) {
        val majorityOne = determineMajority(bitStrings, index, 1, true)
        prefix += if (majorityOne) "0" else "1"

        bitStrings = filterForPrefix(bitStrings, prefix);
        index++
    }
    val co2Rating = bitStrings[0];
    println(co2Rating.toInt(2));


    println(bitStrings);
    return o2Rating.toInt(2) * co2Rating.toInt(2);
}
