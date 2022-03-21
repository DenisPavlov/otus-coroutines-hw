@file:OptIn(ExperimentalCoroutinesApi::class)

package homework.hard

import kotlinx.coroutines.*
import java.io.File

fun main() {
    val dictionaryApi = DictionaryApi()
    val words = FileReader.readFile().split(" ", "\n").toSet()

    val dictionaries = findWords(dictionaryApi, words, Locale.EN)

    dictionaries.map { dictionary ->
        print("For word ${dictionary.word} i found examples: ")
        println(dictionary.meanings.map { definition -> definition.definitions.map { it.example } })
    }
}

private fun findWords(dictionaryApi: DictionaryApi, words: Set<String>, locale: Locale) =
    runBlocking(Dispatchers.IO.limitedParallelism(200)) {
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
