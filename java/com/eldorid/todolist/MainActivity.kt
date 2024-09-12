package com.eldorid.todolist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import com.eldorid.todolist.Adapter.TaskAdapter
import com.eldorid.todolist.data.Task

class MainActivity : AppCompatActivity() {

    private lateinit var taskAdapter: TaskAdapter
    private lateinit var taskList: ArrayList<Task>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize the task list
        taskList = ArrayList()

        // Set up the ListView and the adapter
        val listViewTasks = findViewById<ListView>(R.id.listViewTasks)
        taskAdapter = TaskAdapter(this, taskList)
        listViewTasks.adapter = taskAdapter

        // Handle adding tasks
        val editTextTask = findViewById<EditText>(R.id.editTextTask)
        val buttonAddTask = findViewById<Button>(R.id.buttonAddTask)
        buttonAddTask.setOnClickListener {
            val taskDescription = editTextTask.text.toString()
            if (taskDescription.isNotEmpty()) {
                val newTask = Task(taskDescription)
                taskList.add(newTask)
                taskAdapter.notifyDataSetChanged() // Refresh the list
                editTextTask.text.clear() // Clear the input field
            }
        }
    }
}