package com.bmstu.bioinform

import java.lang.RuntimeException
import java.lang.StringBuilder

class FASTASequence(private val name: String, private val sequence: String): Iterable<Char> {

    val modified = StringBuffer(sequence)

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

            val str1 = s1.modified.toString()
            val str2 = s2.modified.toString()
            val res = StringBuilder()

            for (ch in str1) {
                res.append("$ch ")
            }
            res.append("\n")
            for (i in 0 until str1.length) {
                if (str1[i] == str2[i]){
                    res.append("* ")
                } else if (str1[i] == Score.GAP_SYMBOL || str2[i] == Score.GAP_SYMBOL) {
                    res.append("  ")
                } else {
                    res.append("| ")
                }
            }
            res.append("\n")
            for (ch in str2) {
                res.append("$ch ")
            }
            return res.toString()
        }
    }

    override fun toString(): String {
        return "$name: $sequence"
    }

}