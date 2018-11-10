package com.bmstu.bioinform

import java.lang.RuntimeException


class Algorithm(
        private val s1: FASTASequence,
        private val s2: FASTASequence,
        private val gapOpenFine: Int,
        private val gapExtendFine: Int,
        matchingReward: (Char, Char) -> Int
) {

    private val matchingReward = { i: Int, j: Int -> matchingReward(s1[i], s2[j]) }

    private val matchScore: Score
    private val insertionScore: Score
    private val deletionScore: Score
    private val resultingScore: ResultingScore

    init {
        //Initialize score tables
        val infinityScore = 2 * gapOpenFine + (s1.length + s2.length + 2) * gapExtendFine + 1
        matchScore = Score(s1.length + 1, s2.length + 1) { i: Int, j: Int ->
            when {
                i == 0 && j == 0 -> 0
                j == 0 -> infinityScore
                i == 0 -> infinityScore
                else -> 0
            }
        }
        insertionScore = Score(s1.length + 1, s2.length + 1) { i: Int, j: Int ->
            when {
                i == 0 && j == 0 -> infinityScore
                j == 0 -> gapOpenFine + (i - 1) * gapExtendFine
                i == 0 -> infinityScore
                else -> 0
            }
        }
        deletionScore = Score(s1.length + 1, s2.length + 1) { i: Int, j: Int ->
            when {
                i == 0 && j == 0 -> infinityScore
                j == 0 -> infinityScore
                i == 0 -> gapOpenFine + (j - 1) * gapExtendFine
                else -> 0
            }
        }
        //init resulting table's first row and first column
        resultingScore = ResultingScore(s1.length + 1, s2.length + 1)
        for (j in 1 until resultingScore.width) {
            resultingScore[0, j] = max(
                    Pair(Pair(0, j - 1), matchScore[0, j]),
                    Pair(Pair(0, j - 1), deletionScore[0, j])
            )
        }
        for (i in 1 until resultingScore.height) {
            resultingScore[i, 0] = max(
                    Pair(Pair(i - 1, 0), matchScore[i, 0]),
                    Pair(Pair(i - 1, 0), insertionScore[i, 0])
            )
        }
    }

    fun perform(): ResultingScore {
        algorithm()
        buildStrings()
        return resultingScore
    }

    /*
        fill table from left to right from top to bottom
        + + + +
        + + + +
        + + - -
        - - - -
        Right and bottom cell will contain resulting score
     */
    private fun algorithm() {

        //for every line except first(filled at initialization)
        for (i in 1..s1.length) {
            //for every column except first(filled at initialization)
            for (j in 1..s2.length) {
                setMatchScore(i, j)
                setInsertionScore(i, j)
                setDeletionScore(i, j)
                setResultingScore(i, j)
            }
        }
    }

    private fun setMatchScore(i: Int, j: Int) {
        matchScore[i, j] = max(
                matchScore    [i - 1, j - 1] + matchingReward(i - 1, j - 1),
                insertionScore[i - 1, j - 1] + matchingReward(i - 1, j - 1),
                deletionScore [i - 1, j - 1] + matchingReward(i - 1, j - 1)
        )
    }

    private fun setInsertionScore(i: Int, j: Int) {
        insertionScore[i, j] = max(
                matchScore    [i, j - 1] + gapOpenFine,
                insertionScore[i, j - 1] + gapExtendFine,
                deletionScore [i, j - 1] + gapOpenFine
        )
    }

    private fun setDeletionScore(i: Int, j: Int) {
        deletionScore[i, j] = max(
                matchScore    [i - 1, j] + gapOpenFine,
                insertionScore[i - 1, j] + gapOpenFine,
                deletionScore [i - 1, j] + gapExtendFine
        )
    }

    private fun setResultingScore(i: Int, j: Int) {
        //save score and path for convenient rows building
        resultingScore[i, j] = max(
                max(
                        Pair(Pair(i - 1, j - 1), matchScore[i, j]),
                        Pair(Pair(i, j - 1), insertionScore[i, j])
                ),
                Pair(Pair(i - 1, j), deletionScore[i, j])
        )
    }

    private fun buildStrings() {
        var i = resultingScore.height - 1
        var j = resultingScore.width - 1

        while(i != 0 || j != 0) {
            val value = resultingScore[i, j]
            when {
                i - 1 == value.first.first && j - 1 == value.first.second -> {}
                i     == value.first.first && j - 1 == value.first.second -> s1.insertGap(i)
                i - 1 == value.first.first && j     == value.first.second -> s2.insertGap(j)
                else -> throw RuntimeException("Unexpected previous cell")
            }
            i = value.first.first
            j = value.first.second
        }
    }

private fun max(a: Int, b: Int, c: Int) = Math.max(Math.max(a, b), c)

private fun max(e1: Pair<Pair<Int, Int>, Int>, e2: Pair<Pair<Int, Int>, Int>) = when {
    e1.second >  e2.second -> e1
    e1.second == e2.second -> e2
    else                   -> e2
}

}