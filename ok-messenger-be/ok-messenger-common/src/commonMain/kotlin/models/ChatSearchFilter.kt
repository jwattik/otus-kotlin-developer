package ru.otus.messenger.common.models

data class ChatSearchFilter(
    var searchFields: List<SearchField> = emptyList(),
    var ownerId: ChatOwnerId = ChatOwnerId.NONE,
    var type: ChatType = ChatType.NONE,
    var mode: ChatMode = ChatMode.NONE,
) {
    interface SearchField {
        val fieldName: String
        val action: SearchAction
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
        val stringValue: String,
    ) : SearchField

    companion object {
        val NONE = ChatSearchFilter()
    }
}