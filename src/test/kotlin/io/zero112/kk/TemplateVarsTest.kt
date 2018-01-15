package io.zero112.kk

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.PipedInputStream
import java.io.PipedOutputStream
import java.io.PrintWriter


fun PrintWriter.writeline(value: String = "\n") {
    write("$value\n")
    flush()
}

class TemplateVarsTest {
    val oldInputStream = System.`in`

    val consoleInput = PipedInputStream()
    val userInput = PrintWriter((PipedOutputStream(consoleInput)))

    @BeforeEach
    fun beforeTests() {
        System.setIn(consoleInput)
    }

    @AfterEach
    fun afterTests() {
        System.setIn(oldInputStream)
    }

    @Test
    fun testPromptEmpty() {
        userInput.write("testResponse\n")
        userInput.flush()
        val v = TemplateVars()

        v.prompt("promptMe")

        val varVal = v["promptMe"]
        assertTrue(varVal is String)
        assertEquals("testResponse", varVal)
    }

    @Test
    fun testVarPromptWithReplace() {
        userInput.write("testResponse\n")
        userInput.flush()
        val v = TemplateVars()

        v.prompt("promptMe")

        v["replaced"] = replace(v["promptMe"], "Response", "TheAnswer")

        val varVal = v["promptMe"]
        assertTrue(varVal is String)
        assertEquals("testResponse", varVal)

        val replaced = v["replaced"]
        assertTrue(replaced is String)
        assertEquals("testTheAnswer", replaced)
    }

    @Test
    fun testPrompt() {
        val v = TemplateVars()
        println("Hallo world")

        userInput.writeline("microsService-userInput")
        userInput.writeline("thingy-userInput")
        userInput.writeline()
        with(v) {
            prompt("microsService")
            prompt("thingy", "theDefault")
            prompt("bar", v["microsService"])
        }

        assertEquals("microsService-userInput", v["microsService"])
        assertEquals("thingy-userInput", v["thingy"])
        assertEquals("microsService-userInput", v["bar"])
    }
}