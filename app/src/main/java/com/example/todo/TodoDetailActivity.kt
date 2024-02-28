package com.example.todo


import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.todo.ui.theme.TodoTheme

class TodoDetailActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val mTodoIndex = intent.getIntExtra(TODO_INDEX, 0)
        setContent {
            TodoTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    TodoDetail(LocalContext.current.resources.getStringArray(R.array.todo)[mTodoIndex])
                }
            }
        }
    }

    companion object {

        fun newIntent(packageContext: Context?, todoIndex: Int): Intent {
            val intent = Intent(packageContext, TodoDetailActivity::class.java)
            intent.putExtra(TODO_INDEX, todoIndex)
            return intent
        }
        const val IS_TODO_COMPLETE = "com.example.isTodoComplete"

        private const val TODO_INDEX = "com.example.todoIndex"
    }
}
@Composable
fun TodoDetail(todo:String , modifier: Modifier = Modifier) {
    val isChecked = remember { mutableStateOf(false) }
    val activity = (LocalContext.current as? Activity)

    Column(modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally)
    {
        Text(
            text = todo,
            fontSize = 24.sp
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {

            Text(
                text = stringResource(id = R.string.todo_complete),
                fontSize = 12.sp
            )
            Spacer(Modifier.size(6.dp))
            Checkbox(
                checked = isChecked.value,
                onCheckedChange = {
                    isChecked.value = it
                    val intent = Intent()
                    intent.putExtra(TodoDetailActivity.IS_TODO_COMPLETE, isChecked.value)
                    activity?.setResult(RESULT_OK, intent)
                    activity?.finish()

                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TodoDetailPreview() {
    TodoTheme {
        TodoDetail(LocalContext.current.resources.getStringArray(R.array.todo)[0])
    }
}