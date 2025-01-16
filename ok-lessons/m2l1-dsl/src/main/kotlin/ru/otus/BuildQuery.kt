package ru.otus

fun query(block: SqlSelectBuilder.() -> Unit) = SqlSelectBuilder().apply(block)