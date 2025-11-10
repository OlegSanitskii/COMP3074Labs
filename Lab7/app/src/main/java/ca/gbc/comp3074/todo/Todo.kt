package ca.gbc.comp3074.todo

data class Todo(
    val userId: Int,
    val id: Int,
    val title: String,
    val completed: Boolean
)

