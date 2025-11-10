package ca.gbc.comp3074.todo

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class TodoViewModel(private val repo: TodoRepository = TodoRepository()) : ViewModel() {

    data class UiState(
        val loading: Boolean = false,
        val items: List<Todo> = emptyList(),
        val error: String? = null
    )

    var state by mutableStateOf(UiState(loading = true))
        private set

    init {
        refresh()
    }


    fun refresh() {
        repo.fetchTodos { results ->
            state = results.fold(
                onFailure = { e ->
                    UiState(
                        loading = false,
                        items = emptyList(),
                        error = e.message ?: e.toString()
                    )
                },
                onSuccess = { list ->
                    UiState(
                        loading = false,
                        items = list,
                        error = null
                    )
                }
            )
        }
    }
}

