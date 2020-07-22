package com.github.fernthedev.fernutils

import com.github.fernthedev.fernutils.console.ArgumentArrayUtils.parseArguments
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.util.*
import java.util.concurrent.atomic.AtomicBoolean

class ArgumentTestKotlin {

    @DisplayName("Kotlin Argument test 1")
    @Test
    fun testArgumentTest() {
        val wasFound = AtomicBoolean(false)
        parseArguments(
            args,
            "w" to
                    { _: Queue<String> ->
                        println("The argument w was found")
                    },
            "-test" to
                    { _: Queue<String> ->
                        println("The argument -test was found")
                        wasFound.set(true)
                    }
        )
        Assertions.assertTrue(wasFound.get(), "Did not find argument -test in parse")
    }



    @DisplayName("Kotlin Argument test 2 arg extras check")
    @Test
    fun testArgumentExtrasTest() {
        val wasFound = AtomicBoolean(false)
        val argQueue = LinkedList(listOf(*args))

        argQueue.remove("-test")
        parseArguments(
            args,
            "w" to
                    { _: Queue<String> ->
                        println("The argument w was found")
                    },
            "-test" to
                    { strings: Queue<String> ->
                        val expectedSize = argQueue.size
                        val resultSize = strings.size
                        Assertions.assertEquals(expectedSize, resultSize)
                        val expected = argQueue.toString()
                        val result = strings.toString()
                        Assertions.assertEquals(expected, result)
                        println("The argument -test was found")
                        wasFound.set(true)
                    }
        )
        Assertions.assertTrue(wasFound.get(), "Did not find -test arguments in parse")
    }

    companion object {
        private val args =
            arrayOf("-test", "arg", "-something", "thisisvalue")
    }
}