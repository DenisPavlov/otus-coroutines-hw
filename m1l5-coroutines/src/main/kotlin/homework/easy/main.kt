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
        listOf(
            async { findNumberInList(toFind, numbers) },
            async { findNumberInList(toFindOther, numbers) },
        ).map {
            it.await()
        }.forEach {
            if (it != -1) {
                println("Your number $it found!")
            } else {
                println("Not found number $toFind || $toFindOther")
            }
        }
    }

    println("Result time is: $timeMillis ms")
}
