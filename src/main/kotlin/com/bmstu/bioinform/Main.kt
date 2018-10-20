package com.bmstu.bioinform

import com.xenomachina.argparser.ArgParser
import com.xenomachina.argparser.mainBody

/*
    TODO
    1. Algorithm
    2. Tests
    3. check fasta for correctness(only chars from alphabet)
    4. print output to file optionally
 */

fun main(args: Array<String>) = mainBody {
    val config = ArgParser(args).parseInto { parser: ArgParser -> CommandLineParser(parser) }.config

    val s1 = FASTASequence.parse(config.firstFile, config.disableCheck)
    val s2 = FASTASequence.parse(config.secondFile, config.disableCheck)
    val score = Algorithm(
            s1,
            s2,
            config.gap,
            Table.parse(config.table).matcher()
            ).perform()
    println("Score: ${score.score}")
    if (config.debug){
        println(score)
    }
    println(FASTASequence.generateOutput(s1, s2))
}