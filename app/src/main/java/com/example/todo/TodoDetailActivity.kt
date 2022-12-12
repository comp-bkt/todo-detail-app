package com.example.todo

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.todo.TodoDetailActivity

class TodoDetailActivity : AppCompatActivity() {
    private var mTodoIndex = 0
    private lateinit var todoDetails: Array<String>

    /* override to write the value of mTodoIndex into the Bundle with TODO_INDEX as its key */
    public override fun onSaveInstanceState(savedInstanceState: Bundle) {
        super.onSaveInstanceState(savedInstanceState)
        savedInstanceState.putInt(TODO_INDEX, mTodoIndex)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_todo_detail)
        if (savedInstanceState != null) {
            mTodoIndex = savedInstanceState.getInt(TODO_INDEX, 0)
        }

        /* TODO: refactor to a data layer */
        val res = resources
        todoDetails = res.getStringArray(R.array.todo_detail)

        /* get the intent extra int for the todos index */
        val mTodoIndex = intent.getIntExtra(TODO_INDEX, 0)
        updateTextViewTodoDetail(mTodoIndex)
        val checkboxIsComplete = findViewById<View>(R.id.checkBoxIsComplete) as CheckBox
        /* Register the onClick listener with the generic implementation mTodoListener */
        checkboxIsComplete.setOnClickListener(mTodoListener)
    }

    private fun updateTextViewTodoDetail(todoIndex: Int) {
        val textViewTodoDetail: TextView
        textViewTodoDetail = findViewById<View>(R.id.textViewTodoDetail) as TextView

        /* display the first task from mTodo array in the TodoTextView */
        textViewTodoDetail.text = todoDetails[todoIndex]
    }

    /* Create an anonymous implementation of OnClickListener for all clickable view objects */
    private val mTodoListener = View.OnClickListener { v: View ->
        // get the clicked object and do something
        val id = v.id
        if (id == R.id.checkBoxIsComplete) {
            val checkboxIsComplete = findViewById<View>(R.id.checkBoxIsComplete) as CheckBox
            setIsComplete(checkboxIsComplete.isChecked)
            finish()
        }
    }

    private fun setIsComplete(isChecked: Boolean) {

        /* celebrate with a static Toast! */
        if (isChecked) {
            Toast.makeText(this@TodoDetailActivity,
                    "Hurray, it's done!", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(this@TodoDetailActivity,
                    "There is always tomorrow!", Toast.LENGTH_LONG).show()
        }
        val intent = Intent()
        intent.putExtra(IS_TODO_COMPLETE, isChecked)
        setResult(RESULT_OK, intent)
    }

    companion object {
        /* Any calling activity would call this static method and pass the necessary
       key, value data pair in an intent object. */
        fun newIntent(packageContext: Context?, todoIndex: Int): Intent {
            val intent = Intent(packageContext, TodoDetailActivity::class.java)
            intent.putExtra(TODO_INDEX, todoIndex)
            return intent
        }

        /* name, value pair to be returned in an intent */
        private const val IS_TODO_COMPLETE = "com.example.isTodoComplete"

        /* In case of state change, due to rotating the phone
       store the mTodoIndex to display the same mTodos element post state change

       N.B. small amounts of data, typically IDs can be stored as key, value pairs in a Bundle
       the alternative is to abstract view data to a ViewModel which can be in scope in all
       Activity states and more suitable for larger amounts of data
    */
        private const val TODO_INDEX = "com.example.todoIndex"
    }
}