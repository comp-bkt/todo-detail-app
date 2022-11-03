package com.example.todo;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class TodoActivity extends AppCompatActivity {


    private static final int IS_SUCCESS = 0;
    private String[] mTodos;
    private int mTodoIndex = 0;

    public static final String TAG = "TodoActivity";

    /* map or name, value pair to be returned in an intent */
    private static final String IS_TODO_COMPLETE = "com.example.isTodoComplete";

    /** In case of state change, due to rotating the phone
     * store the mTodoIndex to display the same mTodos element post state change
     * N.B. small amounts of data, typically IDs can be stored as key, value pairs in a Bundle
     * the alternative is to abstract view data to a ViewModel which can be in scope in all
     * Activity states and more suitable for larger amounts of data */

    private static final String TODO_INDEX = "com.example.todoIndex";

    /* override to write the value of mTodoIndex into the Bundle with TODO_INDEX as its key */
    @Override
    public void onSaveInstanceState(@NonNull Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putInt(TODO_INDEX, mTodoIndex);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        /* call the super class onCreate to complete the creation of Activity
           like the view hierarchy */
        super.onCreate(savedInstanceState);

        Log.d(TAG, " **** Just to say the PC is in onCreate!");

        /* set the user interface layout for this Activity
         the layout file is defined in the project res/layout/activity_todo.xml file */
        setContentView(R.layout.activity_todo);

        /* check for saved state due to changes such as rotation or back button
           and restore any saved state such as the todo_index */
        if (savedInstanceState != null){
            mTodoIndex = savedInstanceState.getInt(TODO_INDEX, 0);
        }

        /* TODO: Refactor to data layer */
        Resources res = getResources();
        mTodos = res.getStringArray(R.array.todo);

        /* initialize member TextView so we can manipulate it later */
        final TextView textViewTodo;
        textViewTodo = (TextView) findViewById(R.id.textViewTodo);

        setTextViewComplete("");

        /* display the first task from mTodo array in the textViewTodo */
        textViewTodo.setText(mTodos[mTodoIndex]);

        Button buttonNext = (Button) findViewById(R.id.buttonNext);
        buttonNext.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                mTodoIndex = (mTodoIndex + 1) % mTodos.length;
                textViewTodo.setText(mTodos[mTodoIndex]);
                setTextViewComplete("");
            }
        });

        ActivityResultLauncher<Intent> todoDetailActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent intent = result.getData();
                        boolean isTodoComplete = intent.getBooleanExtra(IS_TODO_COMPLETE, false);
                        updateTodoComplete(isTodoComplete);
                    } else {
                        Toast.makeText(this, R.string.back_button_pressed, Toast.LENGTH_SHORT).show();
                    }

                });

        Button buttonTodoDetail = (Button) findViewById(R.id.buttonTodoDetail);
        buttonTodoDetail.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = TodoDetailActivity.newIntent(TodoActivity.this, mTodoIndex);
                todoDetailActivityResultLauncher.launch(intent);

            }
        });

    }

    private void updateTodoComplete(boolean is_todo_complete) {

        final TextView textViewTodo;
        textViewTodo = (TextView) findViewById(R.id.textViewTodo);

        if (is_todo_complete) {
            textViewTodo.setBackgroundColor(
                    ContextCompat.getColor(this, R.color.backgroundSuccess));
            textViewTodo.setTextColor(
                    ContextCompat.getColor(this, R.color.colorSuccess));

            setTextViewComplete("\u2713");
        }

    }

    private void setTextViewComplete( String message ){
        final TextView textViewComplete;
        textViewComplete = (TextView) findViewById(R.id.textViewComplete);

        textViewComplete.setText(message);
    }

}