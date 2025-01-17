package ru.otus

fun mapToFML(fml: Map<String, String>): String = "${fml["last"]} ${fml["first"]} ${fml["middle"]}"

fun mapToFL(fl: Map<String, String>): String = "${fl["last"]} ${fl["first"]}"

fun mapToF(f: Map<String, String>): String = "${f["first"]}"

fun mapListToNames(mapList: List<Map<String, String>>): List<String> {
    val result = mutableListOf<String>()
    mapList.forEach {
        when(it.keys) {
            setOf("first") -> result.add(mapToF(it))
            setOf("first", "last") -> result.add(mapToFL(it))
            setOf("first", "middle", "last") -> result.add(mapToFML(it))
        }
    }

    return result.toList()
}