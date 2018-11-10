package com.bmstu.bioinform


import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.fail
import java.lang.RuntimeException

class AlgorithmTests {

    @Test
    fun `ACGGCTT -- ACGT`() {
        val s1 = FASTASequence("1", "ACGGCTT")
        val s2 = FASTASequence("2", "ACGT")
        val score = Algorithm(
                s1,
                s2,
                -10,
                -1,
                Table.parse(listOf()).matcher()
        ).perform()
        assertEquals(8, score.score)
        assertEquals( "ACGGCTT", s1.modified.toString())
        assertEquals( "ACG---T", s2.modified.toString())
    }

    @Test
    fun `A -- B`() {
        val s1 = FASTASequence("1", "A")
        val s2 = FASTASequence("2", "B")
        val score = Algorithm(
                s1,
                s2,
                -10,
                -1,
                Table.parse(listOf()).matcher()
        ).perform()
        assertEquals(-5, score.score)
        assertEquals( "A", s1.modified.toString())
        assertEquals( "B", s2.modified.toString())
    }

    @Test
    fun `AAAAB -- B`() {
        //Appending dashes to start
        val s1 = FASTASequence("1", "AAAAB")
        val s2 = FASTASequence("2", "B")
        val score = Algorithm(
                s1,
                s2,
                -2,
                -1,
                Table.parse(listOf()).matcher()
        ).perform()
        assertEquals(0, score.score)
        assertEquals( "AAAAB", s1.modified.toString())
        assertEquals( "----B", s2.modified.toString())
    }

    @Test
    fun `BAAAA -- B`() {
        val s1 = FASTASequence("1", "BAAAA")
        val s2 = FASTASequence("2", "B")
        val score = Algorithm(
                s1,
                s2,
                -2,
                -1,
                Table.parse(listOf()).matcher()
        ).perform()
        assertEquals(0, score.score)
        assertEquals( "BAAAA", s1.modified.toString())
        assertEquals( "B----", s2.modified.toString())
    }

    @Test
    fun `test blosum62 table`() {
        val s1 = FASTASequence("1", "BAAAA")
        val s2 = FASTASequence("2", "B")
        val score = Algorithm(
                s1,
                s2,
                -2,
                -1,
                Table.parse(blosumTable.split("\n")).matcher()
        ).perform()
        assertEquals(-1, score.score)
        assertEquals( "BAAAA", s1.modified.toString())
        assertEquals( "B----", s2.modified.toString())
    }

    @Test
    fun `test wrong sequence`() {
        try {
            FASTASequence.parse(">AAAA\n;4Av", disableCheck = false)
        } catch (e: RuntimeException){
            assertEquals("Not FASTA", e.message)
            return
        }
        fail("expected exception")
    }

    @Test
    fun `test symbol not in table`() {
        val s1 = FASTASequence("1", "AU")
        val s2 = FASTASequence("2", "B")
        try {
            val score = Algorithm(
                    s1,
                    s2,
                    -2,
                    -1,
                    Table.parse(blosumTable.split("\n")).matcher()
            ).perform()
        } catch (e: RuntimeException){
            assertEquals("Not found char null in table", e.message)
        }
    }

}

const val blosumTable = "   A  R  N  D  C  Q  E  G  H  I  L  K  M  F  P  S  T  W  Y  V  B  Z  X  *\n" +
        "A  4 -1 -2 -2  0 -1 -1  0 -2 -1 -1 -1 -1 -2 -1  1  0 -3 -2  0 -2 -1  0 -4\n" +
        "R -1  5  0 -2 -3  1  0 -2  0 -3 -2  2 -1 -3 -2 -1 -1 -3 -2 -3 -1  0 -1 -4\n" +
        "N -2  0  6  1 -3  0  0  0  1 -3 -3  0 -2 -3 -2  1  0 -4 -2 -3  3  0 -1 -4\n" +
        "D -2 -2  1  6 -3  0  2 -1 -1 -3 -4 -1 -3 -3 -1  0 -1 -4 -3 -3  4  1 -1 -4\n" +
        "C  0 -3 -3 -3  9 -3 -4 -3 -3 -1 -1 -3 -1 -2 -3 -1 -1 -2 -2 -1 -3 -3 -2 -4\n" +
        "Q -1  1  0  0 -3  5  2 -2  0 -3 -2  1  0 -3 -1  0 -1 -2 -1 -2  0  3 -1 -4\n" +
        "E -1  0  0  2 -4  2  5 -2  0 -3 -3  1 -2 -3 -1  0 -1 -3 -2 -2  1  4 -1 -4\n" +
        "G  0 -2  0 -1 -3 -2 -2  6 -2 -4 -4 -2 -3 -3 -2  0 -2 -2 -3 -3 -1 -2 -1 -4\n" +
        "H -2  0  1 -1 -3  0  0 -2  8 -3 -3 -1 -2 -1 -2 -1 -2 -2  2 -3  0  0 -1 -4\n" +
        "I -1 -3 -3 -3 -1 -3 -3 -4 -3  4  2 -3  1  0 -3 -2 -1 -3 -1  3 -3 -3 -1 -4\n" +
        "L -1 -2 -3 -4 -1 -2 -3 -4 -3  2  4 -2  2  0 -3 -2 -1 -2 -1  1 -4 -3 -1 -4\n" +
        "K -1  2  0 -1 -3  1  1 -2 -1 -3 -2  5 -1 -3 -1  0 -1 -3 -2 -2  0  1 -1 -4\n" +
        "M -1 -1 -2 -3 -1  0 -2 -3 -2  1  2 -1  5  0 -2 -1 -1 -1 -1  1 -3 -1 -1 -4\n" +
        "F -2 -3 -3 -3 -2 -3 -3 -3 -1  0  0 -3  0  6 -4 -2 -2  1  3 -1 -3 -3 -1 -4\n" +
        "P -1 -2 -2 -1 -3 -1 -1 -2 -2 -3 -3 -1 -2 -4  7 -1 -1 -4 -3 -2 -2 -1 -2 -4\n" +
        "S  1 -1  1  0 -1  0  0  0 -1 -2 -2  0 -1 -2 -1  4  1 -3 -2 -2  0  0  0 -4\n" +
        "T  0 -1  0 -1 -1 -1 -1 -2 -2 -1 -1 -1 -1 -2 -1  1  5 -2 -2  0 -1 -1  0 -4\n" +
        "W -3 -3 -4 -4 -2 -2 -3 -2 -2 -3 -2 -3 -1  1 -4 -3 -2 11  2 -3 -4 -3 -2 -4\n" +
        "Y -2 -2 -2 -3 -2 -1 -2 -3  2 -1 -1 -2 -1  3 -3 -2 -2  2  7 -1 -3 -2 -1 -4\n" +
        "V  0 -3 -3 -3 -1 -2 -2 -3 -3  3  1 -2  1 -1 -2 -2  0 -3 -1  4 -3 -2 -1 -4\n" +
        "B -2 -1  3  4 -3  0  1 -1  0 -3 -4  0 -3 -3 -2  0 -1 -4 -3 -3  4  1 -1 -4\n" +
        "Z -1  0  0  1 -3  3  4 -2  0 -3 -3  1 -1 -3 -1  0 -1 -3 -2 -2  1  4 -1 -4\n" +
        "X  0 -1 -1 -1 -2 -1 -1 -1 -1 -1 -1 -1 -1 -1 -2  0  0 -2 -1 -1 -1 -1 -1 -4\n" +
        "* -4 -4 -4 -4 -4 -4 -4 -4 -4 -4 -4 -4 -4 -4 -4 -4 -4 -4 -4 -4 -4 -4 -4  1"