package com.bmstu.bioinform

import java.lang.StringBuilder

/**
 * Represents helping score tables: matching, insertion, deletion
 */
class Score(height: Int, width: Int, initializer: (Int, Int) -> Int) {

    private val data: Array<Array<Int>> = initializeTable(width, height, initializer)

    operator fun set(i: Int, j: Int, value: Int) {
        data[i][j] = value
    }

    operator fun get(i: Int, j: Int): Int {
        return data[i][j]
    }

    private fun initializeTable(width: Int, height: Int, initializer: (Int, Int) -> Int): Array<Array<Int>> {
        val res = ArrayList<Array<Int>>()
        for (i in 0 until height) {
            res.add(Array(width){0})
            for (j in 0 until width) {
                res[i][j] = initializer(i, j)
            }
        }
        return res.toTypedArray()
    }

    override fun toString(): String {
        val res = StringBuilder()
        data.forEach {
            res.append(it.fold(""){acc, int -> "$acc  $int" } + "\n")
        }
        return res.toString()
    }

}

/**
 * Contains resulting scores with address of previous cell
 */
class ResultingScore(val height: Int, val width: Int) {

    private val data = initializeTable(width, height)

    val score: Int
        get() = data[height - 1][width - 1].second

    private fun initializeTable(width: Int, height: Int): Array<Array<Pair<Pair<Int, Int>, Int>>> {
        val res = ArrayList<Array<Pair<Pair<Int, Int>, Int>>>()
        for (i in 0 until height) {
            res.add(Array(width){Pair(Pair(0, 0), 0)})
        }
        return res.toTypedArray()
    }

    operator fun set(i: Int, j: Int, value: Pair<Pair<Int, Int>, Int>) {
        data[i][j] = value
    }

    operator fun get(i: Int, j: Int): Pair<Pair<Int, Int>, Int> {
        return data[i][j]
    }

    fun set(i: Int, j: Int, match: Score, insertion: Score, deletion: Score) {
        val max = {e1: Pair<Pair<Int, Int>, Int>, e2: Pair<Pair<Int, Int>, Int> ->
            when {
                e1.second > e2.second -> e1
                e1.second == e2.second -> e1
                else -> e2
            }
        }

        data[i][j] = max(
                max(
                        Pair(Pair(i - 1, j - 1), match[i,j]),
                        Pair(Pair(i, j - 1), insertion[i, j])
                ),
                Pair(Pair(i - 1, j), deletion[i, j])
        )
    }

    override fun toString(): String {
        val res = StringBuilder()
        data.forEach {
            res.append(it.fold(""){acc, value ->
                "$acc {(${value.first.first}, ${value.first.second}) ${value.second}}"
            } + "\n")
        }
        return res.toString()
    }
}