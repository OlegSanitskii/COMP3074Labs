package ca.gbc.comp3074.lab3

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ca.gbc.comp3074.lab3.ui.theme.Lab3Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Lab3Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MyDeck(messages = SampleData.items,
                        modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}



data class Message(val author:String, val body:String)
@Composable
fun MyCard(msg: Message, modifier: Modifier) {
    Row (modifier = modifier){
        Image(
            painter= painterResource(R.drawable.ic_launcher_foreground),
            contentDescription = "Logo",
            modifier = Modifier
                .size(50.dp)
                .clip(CircleShape)
                .border(2.dp, MaterialTheme.colorScheme.primary,
                    CircleShape)
        )
        Spacer(modifier = Modifier.width(8.dp))

        var isSelected by remember { mutableStateOf(false) }
        val surfaceBk by animateColorAsState(
            if (isSelected) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.surface
            }
        )

        Surface(modifier = Modifier.fillMaxSize()){
            Column(modifier = Modifier.clickable{ isSelected = !isSelected}) {
                Text(
                    msg.author,
                    color = MaterialTheme.colorScheme.secondary,
                    style = MaterialTheme.typography.titleSmall
                )
                Spacer(modifier = Modifier.height(4.dp))
                Surface(
                    shape = MaterialTheme.shapes.medium,
                    shadowElevation = 2.dp,
                    color = surfaceBk,
                    modifier = Modifier.animateContentSize().padding(2.dp)
                ) {
                    Text(
                        msg.body,
                        modifier = Modifier.padding(6.dp),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}


@Preview
@Composable
fun PreviewMyCard() {
    Lab3Theme(dynamicColor =  false) {
        MyCard(msg = Message("Joe", "Hello, Android"),
            modifier = Modifier.padding(10.dp)
        )
    }
}




@Composable
fun MyDeck(messages:List<Message>, modifier: Modifier){
    LazyColumn(modifier = modifier) {
        items(messages){ message ->
            MyCard(
                msg = message,
                modifier = Modifier.padding(10.dp)
            )
        }
    }
}

@Preview
@Composable
fun PreviewMyDeck(){
    Lab3Theme(dynamicColor = true){
        MyDeck(messages = SampleData.items,
            modifier = Modifier.padding(10.dp))
    }
}