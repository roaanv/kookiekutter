@file:JvmName("Main")
import io.zero112.kk.CommandGenerate
import io.zero112.kk.Generator
import io.zero112.kk.MainArgs
import io.zero112.kk.util.ArgParser
import mu.KotlinLogging

val LOG = KotlinLogging.logger {}

fun runGenerate(appArgs: MainArgs, cmdArgs: CommandGenerate) {
    val generator = Generator()
    generator.generate(appArgs, cmdArgs)
}

fun main(args: Array<String>) {
    val parser = ArgParser("kookiekutter", MainArgs(), "generate" to CommandGenerate())
    parser.run(args, showHelp = true) {appArgs, cmdArgs ->
        when(cmdArgs) {
            is CommandGenerate -> {
                cmdArgs.varsOverride.forEach{
                    cmdArgs.vars[it.key] = it.value
                }
                runGenerate(appArgs, cmdArgs)
            }
        }
    }
}
