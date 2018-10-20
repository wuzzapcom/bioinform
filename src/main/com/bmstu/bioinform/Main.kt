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
    Algorithm(
            FASTASequence.parse(config.firstFile, config.disableCheck),
            FASTASequence.parse(config.secondFile, config.disableCheck),
            config.gap,
            Table.parse(config.table).matcher()
            ).perform()
}