package com.bmstu.bioinform

import com.xenomachina.argparser.ArgParser
import com.xenomachina.argparser.mainBody

fun main(args: Array<String>) = mainBody {
    val config = ArgParser(args).parseInto { parser: ArgParser -> CommandLineParser(parser) }.config

    val s1 = FASTASequence.parse(config.firstFile, config.disableCheck)
    val s2 = FASTASequence.parse(config.secondFile, config.disableCheck)
    val score = Algorithm(
            s1,
            s2,
            config.gapOpen,
            config.gapExtend,
            Table.parse(config.table).matcher()
            ).perform()
    println("Score: ${score.score}")
    if (config.debug){
        println(score)
    }
    println(FASTASequence.generateOutput(s1, s2))
}