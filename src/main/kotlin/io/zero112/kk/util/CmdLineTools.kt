package io.zero112.kk.util

import com.beust.jcommander.JCommander
import com.beust.jcommander.Parameter
import com.beust.jcommander.ParameterException
import kotlin.system.exitProcess

// Using http://jcommander.org/

open class DefaultArgs {
    @Parameter(names = arrayOf("--help"), help = true)
    var help: Boolean = false
}

class ArgParser<out T: DefaultArgs> (progName: String, mainArgs: T, vararg subCommandList: Pair<String, DefaultArgs> = arrayOf()) {
    val parser: JCommander
    init {
        val builder = JCommander.newBuilder().addObject(mainArgs)
        builder.programName( progName)

        for ((cmdName, cmd) in subCommandList) {
            builder.addCommand(cmdName, cmd)
        }

        parser = builder.build()
    }

    fun parse(cmdlineArgs: Array<String>, showHelp:Boolean = false): Pair<T, DefaultArgs?> {
        parser.parse(*cmdlineArgs)

        val mainArgs = parser.objects?.get(0)!! as T

        val cmdName = parser.parsedCommand
        val subCommander = if (cmdName != null) parser.commands[cmdName] else null
        val subCmd = if (subCommander != null) subCommander.objects?.get(0) as DefaultArgs else null

        if (showHelp) {
            if (subCmd != null && subCmd.help) {
                subCommander!!.usage()
            } else if (mainArgs.help){
                parser.usage()
            }
        }

        return Pair(mainArgs, subCmd)
    }

    fun usage() {
        parser.usage()
    }

    fun run (cmdlineArgs: Array<String>, showHelp:Boolean = false, argHandler: (T, DefaultArgs?) -> Unit) {
        val (mainArgs, subCmd) = try {
            parse(cmdlineArgs, showHelp)
        } catch (e: ParameterException) {
            println(e.message)
            e.usage()
            exitProcess(1)
        }

        if (mainArgs.help) {
            return
        }

        if (subCmd != null && subCmd.help) {
            return
        }

        if (parser.commands.isNotEmpty() && subCmd == null) {
            parser.usage()
            exitProcess(1)
        }
        argHandler(mainArgs, subCmd)
    }
}
