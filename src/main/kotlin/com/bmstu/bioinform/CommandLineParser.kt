package com.bmstu.bioinform

import com.sun.org.apache.xpath.internal.operations.Bool
import com.xenomachina.argparser.ArgParser
import com.xenomachina.argparser.default
import java.io.File

class CommandLineParser(parser: ArgParser) {
    private val firstConfig by parser.storing("--first", help="first config file")
    private val secondConfig by parser.storing("--second", help="second config file")
    private val tableConfig by parser.storing(
            "--table",
            help="path to table(BLOSUM64 or DNAfull). Used default table if empty")
            .default { "" }
    private val gap by parser.storing("-g", "--gap", help="fine for gap(default: -2)") {toInt()}.default { -2 }
    private val disableCheck by parser.flagging("--disable-check", help="Disable check for FASTA standard")
    private val debug by parser.flagging("--debug", help="Enable debug output")


    val config: Config
        get() = Config(debug, gap, disableCheck, firstConfig, secondConfig, tableConfig)

    inner class Config(val debug: Boolean,
                       val gap: Int,
                       val disableCheck: Boolean,
                       firstConfig: String,
                       secondConfig: String,
                       tableConfig: String) {
        val firstFile: String = readFileAll(firstConfig)
        val secondFile: String = readFileAll(secondConfig)
        val table: List<String> = readFileLines(tableConfig)

        private fun readFileAll(path: String): String {
            val file = File(path)
            return file.readText(Charsets.UTF_8)
        }

        private fun readFileLines(path: String): List<String> {
            if (path == "") return listOf()
            val file = File(path)
            return file.readLines(Charsets.UTF_8)
        }
    }
}