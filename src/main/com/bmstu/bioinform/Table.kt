package com.bmstu.bioinform

import java.lang.RuntimeException
import java.lang.StringBuilder
import java.util.regex.Pattern

class Table(private val table: Array<Array<Int>>, private val indexes: Map<Char, Int>) {

    fun matcher(): (Char, Char) -> Int {
        if (table.isEmpty())
            return {a: Char, b: Char -> if (a == b) 1 else -1}
        return {line: Char, col: Char ->
            val l = indexes[line]
            val c = indexes[col]
            if (l == null)
                throw RuntimeException("Not found char $l in table")
            if (c == null)
                throw RuntimeException("Not found char $c in table")
            table[l][c]
        }
    }

    companion object {
        fun parse(text: List<String>): Table {
            if (text.isEmpty()) return Table(arrayOf(), mapOf())

            if (text.size < 3) {
                throw RuntimeException("Table is too small")
            }
            val chars = text[0].filter { !it.isWhitespace() }
            val indexes: Map<Char, Int> = mapOf(*(chars.mapIndexed {i, value -> Pair(value, i)}).toTypedArray())

            val data = ArrayList<Array<Int>>()

            text.subList(1, text.size).forEachIndexed { index, line ->
                val key = line[0]

                if (indexes[key] != index)
                    throw RuntimeException("Wrong table lines order." +
                            " Char at ${indexes[key]} and at $index are not coincide")

                val arr = line.split(Pattern.compile(" +")).asSequence().drop(1).map { it.toInt() }.toList()
                data.add(arr.toTypedArray())
            }

            return Table(data.toTypedArray(), indexes)
        }
    }

    override fun toString(): String {

        val res = StringBuilder()

        res.append(indexes.toList().fold("  "){acc, pair -> acc + "  ${pair.first}" } + "\n")

        table.forEachIndexed { index, ints ->
            val ch = indexes.toList().filter { it.second == index }[0]
            res.append(ints.fold("${ch.first}"){acc, int -> "$acc  $int" } + "\n")
        }

        return res.toString()
    }

}