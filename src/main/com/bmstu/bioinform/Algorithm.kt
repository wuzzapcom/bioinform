package com.bmstu.bioinform

class Algorithm(
        private val s1: FASTASequence,
        private val s2: FASTASequence,
        private val gapFine: Int,
        private val matchingReward: (Char, Char) -> Int
) {

    fun perform() {
        val score = algorithm()
        println("Score: ${score.score}")
        score.buildFASTA(s1, s2)
        println(FASTASequence.generateOutput(s1, s2))
    }

    /*
        fill table from left to right from top to bottom
        + + + +
        + + + +
        + + - -
        - - - -
        Right and bottom cell will contain resulting score
     */
    private fun algorithm(): Scores {

        val scores = Scores(s1.length + 1, s2.length + 1)

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

    private fun max(a: Int, b: Int, c: Int): Int {
        return Math.max(Math.max(a, b), c)
    }

}