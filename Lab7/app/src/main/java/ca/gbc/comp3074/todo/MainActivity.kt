package ca.gbc.comp3074.todo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ca.gbc.comp3074.todo.ui.theme.ToDoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ToDoTheme {
                    TodoApp()
                }
            }
        }
    }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoApp(vm: TodoViewModel = TodoViewModel()){
    val state = vm.state
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "TODO App") },
                actions = {
                    TextButton(onClick = { vm.refresh() }) { Text(text = "Refresh") }
                }
            )
        }
    ){ padding ->
        when{
            state.loading -> {
                Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center){
                    CircularProgressIndicator()
                }
            }

            state.error != null -> {
                Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "Failed to load: ${state.error}")
                        Spacer(Modifier.height(10.dp))
                        Button(onClick = { vm.refresh() }) { Text(text = "Retry") }
                    }
                }
            }

            else -> TodoList(state.items, Modifier.padding(padding))
        }
    }
}





@Composable
fun TodoList(items: List<Todo>, modifier: Modifier = Modifier) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(all = 10.dp),
        verticalArrangement = Arrangement.spacedBy(space = 5.dp)
    ) {
        itemsIndexed(items, key = { _, t -> t.id }) { index, todo ->
            ElevatedCard(Modifier.fillMaxWidth()) {
                Row(
                    Modifier.padding(all = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AssistChip(
                        onClick = {},
                        label = { Text(text = "#${todo.id}") }
                    )
                    Spacer(Modifier.width(10.dp))
                    Column {
                        Text(
                            text = todo.title,
                            style = MaterialTheme.typography.titleMedium,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = "User: ${todo.userId} - ${if (todo.completed) "Completed" else "Pending"}",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        }
    }
}
