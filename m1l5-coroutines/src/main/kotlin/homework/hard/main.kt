@file:OptIn(ExperimentalCoroutinesApi::class)

package homework.hard

import kotlinx.coroutines.*
import java.io.File

fun main() = runBlocking<Unit> {
    val dictionaryApi = DictionaryApi()
    val words = FileReader.readFile().split(" ", "\n").toSet()

    val dictionaries = async(Dispatchers.IO.limitedParallelism(200)) {
        findWords(dictionaryApi, words, Locale.EN)
    }

    dictionaries.await().map { dictionary ->
        print("For word ${dictionary.word} i found examples: ")
        println(dictionary.meanings.map { definition -> definition.definitions.map { it.example } })
    }
}

private suspend fun findWords(dictionaryApi: DictionaryApi, words: Set<String>, locale: Locale) = coroutineScope {
        words.map {
            async {
                try {
                    dictionaryApi.findWord(locale, it)
                } catch (ex: RuntimeException) {
                    if (ex is CancellationException) throw ex
                    null
                }
            }
        }.toList()
            .awaitAll()
            .mapNotNull { it }
    }

object FileReader {
    fun readFile(): String = File(
        this::class.java.classLoader.getResource("words.txt")?.toURI() ?: throw RuntimeException("Can't read file")
    ).readText()
}
