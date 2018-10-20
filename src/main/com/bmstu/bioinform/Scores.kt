package com.bmstu.bioinform

import java.lang.RuntimeException
import java.lang.StringBuilder

class Scores(private val height: Int, private val width: Int) {

    companion object {
        const val GAP_SYMBOL = '-'
    }

    val score: Int
        get() = data[height - 1][width - 1]

    private val data: Array<Array<Int>> = emptyTable(width, height)

    operator fun set(i: Int, j: Int, value: Int) {
        data[i][j] = value
    }

    operator fun get(i: Int, j: Int): Int {
        if (i < 0 || j < 0)
            return Int.MIN_VALUE
        return data[i][j]
    }

    private fun emptyTable(width: Int, height: Int): Array<Array<Int>> {
        val res = ArrayList<Array<Int>>()
        for (i in 0 until height) {
            res.add(Array(width){_ -> 0})
        }
        return res.toTypedArray()
    }

    fun buildFASTA(s1: FASTASequence, s2: FASTASequence) {
        var i = height - 1
        var j = width - 1
        while (i != 0 || j != 0) {
            when {
                this[i - 1, j - 1] > Math.max(this[i-1, j], this[i, j-1]) -> {
                    i--
                    j--
                }
                this[i, j - 1] > Math.max(this[i-1, j-1], this[i-1, j]) -> {
                    j--
                    s1.insert(j, GAP_SYMBOL)
                }
                this[i - 1, j] > Math.max(this[i-1, j-1], this[i, j-1]) -> {
                    i--
                    s2.insert(i, GAP_SYMBOL)
                }
                this[i-1, j-1] == this[i-1,j] -> {
                    i--
                    j--
                }
                this[i-1, j-1] == this[i,j-1] -> {
                    i--
                    j--
                }
                this[i-1,j] == this[i,j-1] -> {
                    j--
                    s1.insert(j, GAP_SYMBOL)
                }
                else -> {
                    throw RuntimeException("Something went wrong")
                }
            }
        }
    }

    override fun toString(): String {

        val res = StringBuilder()

        data.forEach {
            res.append(it.fold(""){acc, int -> "$acc  $int" } + "\n")
        }
        return res.toString()
    }
}