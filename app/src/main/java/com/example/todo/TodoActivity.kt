package com.example.todo

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class TodoActivity : AppCompatActivity() {
    private lateinit var mTodos: Array<String>
    private var mTodoIndex = 0

    /* override to write the value of mTodoIndex into the Bundle with TODO_INDEX as its key */
    public override fun onSaveInstanceState(savedInstanceState: Bundle) {
        super.onSaveInstanceState(savedInstanceState)
        savedInstanceState.putInt(TODO_INDEX, mTodoIndex)
        savedInstanceState.putString(TODO_COMPLETE,
            (findViewById<View>(R.id.textViewComplete) as TextView).text as String?
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        /* call the super class onCreate to complete the creation of Activity
           like the view hierarchy */
        super.onCreate(savedInstanceState)
        Log.d(TAG, " **** Just to say the PC is in onCreate!")

        /* set the user interface layout for this Activity
         the layout file is defined in the project res/layout/activity_todo.xml file */
        setContentView(R.layout.activity_todo)

        /* check for saved state due to changes such as rotation or back button
           and restore any saved state such as the todo_index */
        var message:String = ""
        if (savedInstanceState != null) {
            mTodoIndex = savedInstanceState.getInt(TODO_INDEX, 0)
            message = savedInstanceState.getString(TODO_COMPLETE, "")
        }

        /* TODO: Refactor to data layer */
        val res = resources
        mTodos = res.getStringArray(R.array.todo)

        /* initialize member TextView so we can manipulate it later */
        val textViewTodo: TextView
        textViewTodo = findViewById<View>(R.id.textViewTodo) as TextView
        setTextViewComplete(message)

        /* display the first task from mTodo array in the textViewTodo */
        textViewTodo.text = mTodos[mTodoIndex]
        val buttonNext = findViewById<View>(R.id.buttonNext) as Button
        buttonNext.setOnClickListener {
            mTodoIndex = (mTodoIndex + 1) % mTodos.size
            textViewTodo.text = mTodos[mTodoIndex]
            setTextViewComplete("")
        }
        val todoDetailActivityResultLauncher = registerForActivityResult<Intent, ActivityResult>(
                ActivityResultContracts.StartActivityForResult()
        ) { result: ActivityResult ->
            if (result.resultCode == RESULT_OK) {
                val intent = result.data
                val isTodoComplete = intent!!.getBooleanExtra(IS_TODO_COMPLETE, false)
                updateTodoComplete(isTodoComplete)
            } else {
                Toast.makeText(this, R.string.back_button_pressed, Toast.LENGTH_SHORT).show()
            }
        }
        val buttonTodoDetail = findViewById<View>(R.id.buttonTodoDetail) as Button
        buttonTodoDetail.setOnClickListener {
            val intent: Intent = TodoDetailActivity.Companion.newIntent(this@TodoActivity, mTodoIndex)
            todoDetailActivityResultLauncher.launch(intent)
        }
    }

    private fun updateTodoComplete(is_todo_complete: Boolean) {
        val textViewTodo: TextView
        textViewTodo = findViewById<View>(R.id.textViewTodo) as TextView
        if (is_todo_complete) {
            textViewTodo.setBackgroundColor(
                    ContextCompat.getColor(this, R.color.backgroundSuccess))
            textViewTodo.setTextColor(
                    ContextCompat.getColor(this, R.color.colorSuccess))
            setTextViewComplete("\u2713")
        }
    }

    private fun setTextViewComplete(message: String) {
        val textViewComplete: TextView
        textViewComplete = findViewById<View>(R.id.textViewComplete) as TextView
        textViewComplete.text = message
    }

    companion object {
        private const val IS_SUCCESS = 0
        const val TAG = "TodoActivity"

        /* map or name, value pair to be returned in an intent */
        private const val IS_TODO_COMPLETE = "com.example.isTodoComplete"

        /** In case of state change, due to rotating the phone
         * store the mTodoIndex to display the same mTodos element post state change
         * N.B. small amounts of data, typically IDs can be stored as key, value pairs in a Bundle
         * the alternative is to abstract view data to a ViewModel which can be in scope in all
         * Activity states and more suitable for larger amounts of data  */
        private const val TODO_INDEX = "com.example.todoIndex"
        private const val TODO_COMPLETE = "is_complete_message"
    }
}