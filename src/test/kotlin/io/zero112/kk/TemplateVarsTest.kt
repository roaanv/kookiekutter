package io.zero112.kk

import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertTrue
import org.junit.*
import java.io.*

class TemplateVarsTest {
    val oldInputStream = System.`in`

    val consoleInput = PipedInputStream()
    val userInput = PrintWriter((PipedOutputStream(consoleInput)))

    @Before
    fun beforeTests() {
        System.setIn(consoleInput)
    }

    @After
    fun afterTests() {
        System.setIn(oldInputStream)
    }

    @Test
    fun testPromptEmpty() {
        userInput.write("testResponse\n")
        userInput.flush()
        val v = TemplateVars()

        v["promptMe"] = Prompt()

        val varVal = v["promptMe"]
        assertTrue(varVal is String)
        assertEquals("testResponse", varVal)
    }

    @Test
    fun testVarPromptWithReplace() {
        userInput.write("testResponse\n")
        userInput.flush()
        val v = TemplateVars()

        v["promptMe"] = Prompt()

        v["replaced"] = strReplace(v["promptMe"], "Response", "TheAnswer")

        val varVal = v["promptMe"]
        assertTrue(varVal is String)
        assertEquals("testResponse", varVal)

        val replaced = v["replaced"]
        assertTrue(replaced is String)
        assertEquals("testTheAnswer", replaced)
    }
}