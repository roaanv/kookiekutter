@file:JvmName("Main")
import io.zero112.kk.CommandGenerate
import io.zero112.kk.Generator
import io.zero112.kk.GeneratorArgs
import io.zero112.kk.MainArgs
import io.zero112.kk.util.ArgParser
import mu.KotlinLogging

val logger = KotlinLogging.logger {}

fun runGenerate(cmdArgs: CommandGenerate) {
    val generator = Generator()
    generator.generate(GeneratorArgs(cmdArgs))
}

fun main(args: Array<String>) {
    val parser = ArgParser("kookiekutter", MainArgs(), "generate" to CommandGenerate())
    parser.run(args, showHelp = true) { _, cmdArgs ->
        when(cmdArgs) {
            is CommandGenerate -> {
                runGenerate(cmdArgs)
            }
        }
    }
}
