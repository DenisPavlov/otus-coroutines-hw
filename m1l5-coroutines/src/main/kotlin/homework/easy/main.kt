package homework.easy

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import kotlin.system.measureTimeMillis

fun main() = runBlocking(Dispatchers.IO) {
    val numbers = generateNumbers()
    val toFind = 10
    val toFindOther = 1000

    val timeMillis = measureTimeMillis {
        val firstResult = async { findNumberInList(toFind, numbers) }
        val secondResult = async { findNumberInList(toFindOther, numbers) }

        val foundNumbers = listOf(
            firstResult.await(),
            secondResult.await(),
        )

        foundNumbers.forEach {
            if (it != -1) {
                println("Your number $it found!")
            } else {
                println("Not found number $toFind || $toFindOther")
            }
        }
    }

    println("Result time is: $timeMillis ms")
}
