package com.bmstu.bioinform


import org.junit.jupiter.api.Assertions.assertEquals

import org.junit.jupiter.api.Test

class AlgorithmTests {

    @Test
    fun `Simple`() {
        val s1 = FASTASequence("1", "AATCG")
        val s2 = FASTASequence("2", "AACG")
        val score = Algorithm(
                s1,
                s2,
                -2,
                Table.parse(listOf()).matcher()
        ).perform()
        assertEquals(score.score, 2)
        assertEquals(s1.modified.toString(), "AATCG")
        assertEquals(s2.modified.toString(), "AA-CG")
    }

}