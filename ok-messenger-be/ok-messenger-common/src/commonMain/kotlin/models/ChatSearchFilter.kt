package ru.otus.messenger.common.models

data class ChatSearchFilter(
    var searchFields: List<SearchField> = emptyList(),
    var ownerId: ChatOwnerId = ChatOwnerId.NONE,
    var type: ChatType = ChatType.NONE,
    var mode: ChatMode = ChatMode.NONE,
) {
    fun deepCopy(): ChatSearchFilter = copy(
        searchFields = searchFields.toMutableList().toList()
    )

    interface SearchField {
        val fieldName: String
        val action: SearchAction
        fun deepCopy(fieldName: String? = null): SearchField
    }

    enum class SearchAction {
        CONTAINS,
        EQUALS,
        MORE,
        LESS
    }

    data class StringSearchField(
        override val fieldName: String,
        override val action: SearchAction = SearchAction.CONTAINS,
        var stringValue: String,
    ) : SearchField {
        override fun deepCopy(fieldName: String?): SearchField {
            fieldName?.let {
                return this.copy(fieldName = it)
            }
            return this.copy()
        }
    }

    companion object {
        val NONE = ChatSearchFilter(
            searchFields = listOf(
                StringSearchField(
                    fieldName = "",
                    stringValue = ""
                )
            )
        )
    }
}