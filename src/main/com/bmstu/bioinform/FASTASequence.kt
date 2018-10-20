package com.bmstu.bioinform

import java.lang.RuntimeException
import java.lang.StringBuilder

class FASTASequence(private val name: String, private val sequence: String): Iterable<Char> {

    val modified =  StringBuffer(sequence)

    val length: Int
        get() = sequence.length


    override fun iterator(): Iterator<Char> {
        return sequence.iterator()
    }

    operator fun get(index: Int): Char {
        return sequence[index]
    }

    fun insert(index: Int, symbol: Char) {
        modified.insert(index, symbol)
    }

    companion object {

        fun parse(text: String, disableCheck: Boolean): FASTASequence {
            if (text[0] != '>') throw RuntimeException("Not FASTA")
            var name = ""
            var index = 0
            for (i in 1 until text.length) {
                if (text[i] == '\n') {
                    name = text.substring(1, i)
                    index = i
                    break
                }
            }

            if (index == 0 )
                throw RuntimeException("Not FASTA")

            var seq = text.substring(index+1, text.length)
            seq = seq.filter { !it.isWhitespace() }

            if (!disableCheck)
                seq.forEach { if (!it.isLetter()) throw RuntimeException("Not FASTA") }

            return FASTASequence(name, seq)
        }

        fun generateOutput(s1: FASTASequence, s2:FASTASequence): String {
            if (s1.modified.length != s2.modified.length) throw RuntimeException("Sequences should be have same length")

            val res = StringBuilder()
            for (i in 0 until s1.length) {
                val s = if (s1.modified[i] == s2.modified[i]) {
                    "${s1.modified[i]} * ${s2.modified[i]}"
                } else if (s1.modified[i] == Scores.GAP_SYMBOL ||
                        s2.modified[i] == Scores.GAP_SYMBOL) {
                    "${s1.modified[i]}   ${s2.modified[i]}"
                } else {
                    "${s1.modified[i]} — ${s2.modified[i]}"
                }
                res.append("$s\n")
            }
            return res.toString()
        }
    }

    override fun toString(): String {
        return "$name: $sequence"
    }

}