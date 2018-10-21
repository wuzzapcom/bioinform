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

        const val OUTPUT_STRING_LENGTH = 80

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

            val r1 = StringBuilder()
            for (ch in str1) {
                r1.append("$ch ")
            }

            val r2 = StringBuilder()
            for (i in 0 until str1.length) {
                if (str1[i] == str2[i]){
                    r2.append("* ")
                } else if (str1[i] == Score.GAP_SYMBOL || str2[i] == Score.GAP_SYMBOL) {
                    r2.append("  ")
                } else {
                    r2.append("| ")
                }
            }

            val r3 = StringBuilder()
            for (ch in str2) {
                r3.append("$ch ")
            }

            var i = 0
            var j = 0

            val ar1 = arrayListOf<String>()
            while (i < r1.length) {
                if (i != 0 && i % OUTPUT_STRING_LENGTH == 0) {
                    ar1.add(r1.substring(j * OUTPUT_STRING_LENGTH,
                            if ((j+1) * OUTPUT_STRING_LENGTH < r1.length) ++j* OUTPUT_STRING_LENGTH else r1.length))
                }
                i++
            }
            i = 0
            j = 0
            val ar2 = arrayListOf<String>()
            while (i < r2.length) {
                if (i != 0 && i % OUTPUT_STRING_LENGTH == 0) {
                    ar2.add(r2.substring(j * OUTPUT_STRING_LENGTH,
                            if ((j+1) * OUTPUT_STRING_LENGTH < r2.length) ++j*OUTPUT_STRING_LENGTH else r2.length))
                }
                i++
            }
            i = 0
            j = 0
            val ar3 = arrayListOf<String>()
            while (i < r3.length) {
                if (i != 0 && i % OUTPUT_STRING_LENGTH == 0) {
                    ar3.add(r3.substring(j * OUTPUT_STRING_LENGTH,
                            if ((j+1) * OUTPUT_STRING_LENGTH < r3.length) ++j*OUTPUT_STRING_LENGTH else r3.length))
                }
                i++
            }

            val res = StringBuilder()
            for (i in 0 until ar1.size) {
                res.append("${ar1[i]}\n")
                res.append("${ar2[i]}\n")
                res.append("${ar3[i]}\n")
                res.append("\n")
            }

            return res.toString()
        }
    }

    override fun toString(): String {
        return "$name: $sequence"
    }

}