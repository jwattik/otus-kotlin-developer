package ru.otus

class SqlSelectBuilder {
    private var table: String? = null
    private val columns = mutableListOf<String>()
    private var whereClause: String? = null

    fun select(vararg columns: String) = apply {
        this.columns.addAll(columns)
    }

    fun from(table: String) = apply {
        this.table = table
    }

    fun where(conditionBuilder: ConditionBuilder.() -> Unit) = apply {
        val condition = ConditionBuilder().apply(conditionBuilder).build()
        whereClause = condition
    }

    fun build(): String {
        requireNotNull(table) { "Table name must be specified." }
        val columnsPart = if (columns.isEmpty()) "*" else columns.joinToString(", ")
        val wherePart = whereClause?.let { " where $it" } ?: ""
        return "select $columnsPart from $table$wherePart"
    }

    class ConditionBuilder {
        private val conditions = mutableListOf<String>()

        infix fun String.eq(value: Any?): String {
            val formattedValue = formatValue(value)
            conditions.add("$this = $formattedValue")
            return this
        }

        infix fun String.nonEq(value: Any?): String {
            val formattedValue = formatValue(value)
            conditions.add(if (value == null) "$this !is null" else "$this != $formattedValue")
            return this
        }

        fun or(block: ConditionBuilder.() -> Unit): ConditionBuilder {
            val nestedConditions = ConditionBuilder().apply(block).build()
            conditions.add("($nestedConditions)")
            return this
        }

        fun build(): String {
            return conditions.joinToString(" or ")
        }

        private fun formatValue(value: Any?): String = when (value) {
            is String -> "'$value'"
            null -> "null"
            else -> value.toString()
        }
    }
}
