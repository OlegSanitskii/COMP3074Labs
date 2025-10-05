package ca.gbc.comp3074.lab4

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import ca.gbc.comp3074.lab4.ui.theme.Lab4Theme
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Lab4Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(modifier = Modifier.padding(innerPadding)) {
                        ItemForm()
                        ItemList()
                    }
                }
            }
        }
    }
}

@Composable
fun ItemForm(){
    var name by remember { mutableStateOf("") }
    var quantity by remember {mutableStateOf(0)}
    var db = ItemDatabase.getDatabase(LocalContext.current)

    Column {
        TextField(
            value = name,
            label = { Text(text = "Item name") },
            onValueChange = {
                name = it
            },
            modifier = Modifier.fillMaxWidth()
        )

        TextField(
            value = quantity.toString(),
            label = { Text(text = "Quantity") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth(),
            onValueChange = { newValue ->
                val v = newValue.filter { it.isDigit() }
                if (v.isNotBlank()) {
                    quantity = v.toInt()
                } else {
                    quantity = 0
                }
            }
        )

        var scope = rememberCoroutineScope()
        Button(
            onClick = {
                scope.launch {
                    db.itemDao().insert(Item(name = name, quantity = quantity))
                    name = ""
                    quantity = 0
                }
            },
            modifier = Modifier
        ) {
            Text(text = "Add Item")
        }
    }
}

@Composable
fun MyListItem(item: Item) {
    Card(modifier = Modifier.padding(8.dp).fillMaxWidth()) {
        Row {
            Text(item.name)
            Spacer(modifier = Modifier.width(10.dp))
            Text(item.quantity.toString())
        }
    }
}

class ItemViewModel(private val db: ItemDatabase) : ViewModel() {
    val allItems: StateFlow<List<Item>> = db.itemDao().getAllItems()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
            initialValue = emptyList()
        )
}

@Composable
fun ItemList(viewModel: ItemViewModel =
                 ItemViewModel(ItemDatabase.getDatabase(LocalContext.current))) {

    val items by viewModel.allItems.collectAsStateWithLifecycle()

    LazyColumn {
        items(items) { item ->
            MyListItem(item)
        }
    }
}