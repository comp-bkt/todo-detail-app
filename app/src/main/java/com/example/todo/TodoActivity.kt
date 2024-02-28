package com.example.todo


import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.todo.ui.theme.TodoTheme

class TodoActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContent {
            TodoTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    TodoWithButtons(LocalContext.current.resources.getStringArray(R.array.todo))
                }
            }
        }
    }
}

@Composable
fun TodoWithButtons(todos:Array<String> , modifier: Modifier = Modifier) {
    val context = LocalContext.current
    var stringIndex by rememberSaveable { mutableStateOf(0) }
    var checked by rememberSaveable {
        mutableStateOf(0)
    }
    val startForResult =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                val intent = result.data
                if (intent?.getBooleanExtra(TodoDetailActivity.IS_TODO_COMPLETE, false) == true) {
                    checked = 1
                }
            }
            else {
                createToast(context = context)
            }
        }
    Column(modifier = Modifier
        .fillMaxSize()
        .background(color = Color.Gray),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally)
    {
        val textColor = if (checked == 0) Color.White else Color.Red
        Box(modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .background(color = Color.Green),
            contentAlignment = Alignment.Center,
            ) {
            Text(
                text = todos[stringIndex],
                fontSize = 32.sp,
                color = textColor
            )
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Button(onClick = {
                checked = 0
                stringIndex = onPrevButton(stringIndex, todos.size)
            }) {
                Text(stringResource(R.string.prev))
            }
            Button(onClick = { startForResult.launch(TodoDetailActivity.Companion.newIntent(context, stringIndex)) }) {
                Text(stringResource(R.string.detail))
            }
            Button(onClick = {
                checked = 0
                stringIndex = onNextButton(stringIndex, todos.size)
            }) {
                Text(stringResource(R.string.next))
            }
        }
    }
}

fun onPrevButton(index:Int, length:Int): Int {
    return if (index > 0) {
        index - 1
    } else {
        length - 1
    }
}

fun onNextButton(index:Int, length:Int): Int {
    return if (index < length - 1) {
        index + 1
    } else {
        0
    }
}

fun createToast(context: Context) {
    Toast.makeText(context, R.string.back_button_pressed, Toast.LENGTH_SHORT).show()
}

@Preview(showBackground = true)
@Composable
fun TodosPreview() {
    TodoTheme {
        TodoWithButtons(LocalContext.current.resources.getStringArray(R.array.todo))
    }
}