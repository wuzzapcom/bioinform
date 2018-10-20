package com.bmstu.bioinform


class Algorithm(
        private val s1: FASTASequence,
        private val s2: FASTASequence,
        private val gapFine: Int,
        private val matchingReward: (Char, Char) -> Int
) {

    fun perform(): Score {
        val score = algorithm()
//        score.buildFASTA(s1, s2)
        buildStrings(score)
        return score
    }

    /*
        fill table from left to right from top to bottom
        + + + +
        + + + +
        + + - -
        - - - -
        Right and bottom cell will contain resulting score
     */
    private fun algorithm(): Score {

        val scores = Score(s1.length + 1, s2.length + 1)

        //for every line
        for (i in 0..s1.length) {
            //for every column
            for (j in 0..s2.length) {
                //left and top cell is initial score, equal 0
                if (i == 0) {
                    if (j == 0) continue
                    //else first line and only gaps available
                    scores[i, j] = scores[i, j - 1] + gapFine
                } else if (j == 0) {
                    //first column and only gaps available
                    scores[i, j] = scores[i - 1, j] + gapFine
                } else {
                    //pick one of two types of gap and equality
                    scores[i, j] = max(
                            scores[i - 1, j - 1] + matchingReward(s1[i - 1], s2[j - 1]),
                            scores[i - 1, j] + gapFine,
                            scores[i, j - 1] + gapFine
                    )
                }
            }
        }
        return scores
    }

    private fun buildStrings(score: Score) {
        var i = score.height - 1
        var j = score.width - 1
        while (i != 0 || j != 0) {
            when {
                score[i, j] == score[i - 1, j - 1] + matchingReward(s1[i - 1], s2[j - 1]) -> {
                    i--
                    j--
                }
                score[i, j] == score[i - 1, j] + gapFine -> {
                    i--
                    s2.insert(i, Score.GAP_SYMBOL)
                }
                score[i, j] == score[i, j - 1] + gapFine -> {
                    j--
                    s1.insert(j, Score.GAP_SYMBOL)
                }
                else -> throw RuntimeException("Error in score table: no way to previous cell")
            }
        }
    }

//    fun buildFASTA(s1: FASTASequence, s2: FASTASequence) {
//        var i = height - 1
//        var j = width - 1
//        while (i != 0 || j != 0) {
//            when {
//                this[i - 1, j - 1] > Math.max(this[i-1, j], this[i, j-1]) -> {
//                    i--
//                    j--
//                }
//                this[i, j - 1] > Math.max(this[i-1, j-1], this[i-1, j]) -> {
//                    j--
//                    s1.insert(j, Score.GAP_SYMBOL)
//                }
//                this[i - 1, j] > Math.max(this[i-1, j-1], this[i, j-1]) -> {
//                    i--
//                    s2.insert(i, Score.GAP_SYMBOL)
//                }
//                this[i-1, j-1] == this[i-1,j] -> {
//                    i--
//                    j--
//                }
//                this[i-1, j-1] == this[i,j-1] -> {
//                    i--
//                    j--
//                }
//                this[i-1,j] == this[i,j-1] -> {
//                    j--
//                    s1.insert(j, Score.GAP_SYMBOL)
//                }
//                else -> {
//                    throw RuntimeException("Something went wrong")
//                }
//            }
//        }
//    }

    private fun max(a: Int, b: Int, c: Int): Int {
        return Math.max(Math.max(a, b), c)
    }

}