package com.example.basic_todoapp

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.basic_todoapp.ui.theme.BasicTodoAppTheme
import androidx.lifecycle.viewmodel.compose.viewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BasicTodoAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    TopLevel()
                }
            }
        }
    }
}
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun TopLevel(viewModel: ToDoViewModel = viewModel()) {
    val (text, setText) = remember { mutableStateOf("") }
    // ToDoData 의 속성은 immutable 로 선언하고 TodoData 는 mutable 로 선언. 리스트 아이템을 변경하여 ui 업데이트
    val todoList = remember { mutableStateListOf<ToDoData>() }
    val onSubmit: (String) -> Unit = { text ->
        val key = (todoList.lastOrNull()?.key ?: 0) + 1
        todoList.add(ToDoData(key, text))
        setText("")
    }
    val onToggle: (Int, Boolean) -> Unit = { key, checked ->
        val index = todoList.indexOfFirst { it.key == key }
        todoList[index] = todoList[index].copy(done = checked)
    }
    val onDelete: (Int) -> Unit = { key ->
        val index = todoList.indexOfFirst { it.key == key }
        todoList.removeAt(index)
    }
    val onEdit: (Int, String) -> Unit = { key, text ->
        val index = todoList.indexOfFirst { it.key == key }
        todoList[index] = todoList[index].copy(text = text)
    }


    Scaffold {
        Column {
            ToDoInput(text = text, onTextChange = setText, onSubmit = onSubmit)

            LazyColumn {
                // key 를 설정하는 것은 성능을 위함
                items(todoList, key = {it.key}) { toDoData ->
                    TodoItem(toDoData = toDoData, onToggle = onToggle, onDelete = onDelete, onEdit = onEdit)
                }
            }
        }
    }
}
@Composable
fun ToDoInput(
    text: String,
    onTextChange: (String) -> Unit,
    onSubmit: (String) -> Unit
) {
    Row(modifier = Modifier.padding(8.dp)) {
        OutlinedTextField(value = text, onValueChange = onTextChange, modifier = Modifier.weight(1.0f))
        Spacer(modifier = Modifier.size(8.dp))
        Button(onClick = { onSubmit(text) }) {
            Text(text = "입력")
        }
    }
}
@Composable
fun TodoItem(
    toDoData: ToDoData,
    onEdit: (key: Int, text: String) -> Unit = { _, _ -> },
    onToggle: (key: Int, checked: Boolean) -> Unit = { _, _ -> },
    onDelete: (key: Int) -> Unit = {}
) {
    var isEditing by remember { mutableStateOf(false) }
    // 수정 시 가시성 변경을 위해 Crossfade 사용
    Card(
        modifier = Modifier.padding(4.dp),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Crossfade(targetState = isEditing, label = "") { state ->
            when (state) {
                true -> {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(4.dp)
                    ) {
                        val (newText, setNewText) = remember { mutableStateOf(toDoData.text) }
                        OutlinedTextField(
                            value = newText,
                            onValueChange = setNewText,
                            modifier = Modifier.weight(1.0f)
                        )
                        Spacer(modifier = Modifier.size(4.dp))
                        Button(onClick = {
                            onEdit(toDoData.key, newText)
                            isEditing = false
                        }) {
                            Text(text = "완료")
                        }
                    }
                }
                false -> {
                    Row(
                        verticalAlignment = Alignment.CenterVertically, // text, checkbox 위치 맞춤
                        modifier = Modifier.padding(4.dp)
                    ) {
                        Text(text = toDoData.text, modifier = Modifier.weight(1.0f))
                        Checkbox(
                            checked = toDoData.done,
                            onCheckedChange = { checked -> onToggle(toDoData.key, checked) }
                        )
                        Button(onClick = { isEditing = true }) {
                            Text(text = "수정")
                        }
                        Spacer(modifier = Modifier.size(4.dp))
                        Button(onClick = { onDelete(toDoData.key) }) {
                            Text(text = "삭제")
                        }
                    }
                }
            }
        }
    }
}

data class ToDoData(
    val key: Int,
    val text: String,
    val done: Boolean = false
)

@Preview(showBackground = true)
@Composable
fun TodoInputTruePreview() {
    BasicTodoAppTheme {
        TodoItem(ToDoData(1, "nice", true))
    }
}

