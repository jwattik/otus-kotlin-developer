package ru.otus

import java.io.File
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import ru.otus.easy.findNumberInList
import ru.otus.easy.generateNumbers
import ru.otus.hard.DictionaryApi
import ru.otus.hard.Locale
import ru.otus.hard.dto.Dictionary

class HomeWorkCoroutines {

    @Test
    fun easyHw() = runBlocking {
        val numbers = generateNumbers()
        val toFind = 10
        val toFindOther = 1000

        val foundNumbers = mutableListOf<Deferred<Int>>()

        foundNumbers.add(async(Dispatchers.Default) { findNumberInList(toFind, numbers) })
        foundNumbers.add(async(Dispatchers.Default) { findNumberInList(toFindOther, numbers) })

        foundNumbers.awaitAll().forEach {
            if (it != -1) {
                println("Your number $it found!")
            } else {
                println("Not found number $toFind || $toFindOther")
            }
        }
    }

    @Test
    fun hardHw(): Unit = runBlocking {
        val dictionaryApi = DictionaryApi()
        val words = FileReader.readFile().split(" ", "\n").toSet()

        // Асинхронный поиск значений
        val deferredDictionaries = findWordsAsync(dictionaryApi, words, Locale.EN)
        val dictionaries = deferredDictionaries.awaitAll()

        dictionaries.filterNotNull().map { dictionary ->
            print("For word ${dictionary.word} i found examples: ")
            println(
                dictionary.meanings
                    .mapNotNull { definition ->
                        val r = definition.definitions
                            .mapNotNull { it.example.takeIf { it?.isNotBlank() == true } }
                            .takeIf { it.isNotEmpty() }
                        r
                    }
                    .takeIf { it.isNotEmpty() }
            )
        }
    }

    private fun findWords(
        dictionaryApi: DictionaryApi,
        words: Set<String>,
        @Suppress("SameParameterValue") locale: Locale
    ) =
        // make some suspensions and async
        words.map {
            dictionaryApi.findWord(locale, it)
        }

    @OptIn(DelicateCoroutinesApi::class)
    private fun findWordsAsync(
        dictionaryApi: DictionaryApi,
        words: Set<String>,
        @Suppress("SameParameterValue") locale: Locale
    ): List<Deferred<Dictionary?>> {
        return words.map { word ->
            GlobalScope.async(Dispatchers.IO) {
                try {
                    dictionaryApi.findWord(locale, word)
                } catch (e: Exception) {
                    println("Error searching word $word: ${e.message}")
                    null // Игнорируем ошибку
                }
            }
        }
    }

    object FileReader {
        fun readFile(): String =
            File(
                this::class.java.classLoader.getResource("words.txt")?.toURI()
                    ?: throw RuntimeException("Can't read file")
            ).readText()
    }
}