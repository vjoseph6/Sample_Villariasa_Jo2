package com.eldorid.todolist.Adapter

import android.app.AlertDialog
import android.content.Context
import android.view.GestureDetector
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.eldorid.todolist.data.Task
import com.eldroid.todolist.R


class TaskAdapter(private val context: Context, private val taskList: ArrayList<Task>) : BaseAdapter() {

    private val selectedTasks = ArrayList<Task>()

    override fun getCount(): Int {
        return taskList.size
    }

    override fun getItem(position: Int): Any {
        return taskList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val viewHolder: ViewHolder
        val rowView: View

        if (convertView == null) {
            val inflater = LayoutInflater.from(context)
            rowView = inflater.inflate(R.layout.list_item, parent, false)
            viewHolder = ViewHolder(
                rowView.findViewById(R.id.checkBox),
                rowView.findViewById(R.id.textViewTask),
                rowView.findViewById(R.id.imageView)
            )
            rowView.tag = viewHolder
        } else {
            rowView = convertView
            viewHolder = rowView.tag as ViewHolder
        }

        val task = getItem(position) as Task
        viewHolder.textViewTask.text = task.description
        viewHolder.checkBox.isChecked = task.isChecked

        viewHolder.checkBox.setOnCheckedChangeListener { _, isChecked ->
            task.isChecked = isChecked
            if (isChecked) {
                selectedTasks.add(task)
            } else {
                selectedTasks.remove(task)
            }
        }

        val gestureDetector = GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {
            override fun onDoubleTap(e: MotionEvent): Boolean {
                showEditDeleteDialog()
                return true
            }
        })

        rowView.setOnTouchListener { _, event ->
            gestureDetector.onTouchEvent(event)
            true
        }

        return rowView
    }

    private fun showEditDeleteDialog() {
        val builder = AlertDialog.Builder(context)
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_choose_option, null)
        builder.setView(dialogView)

        val dialog = builder.create()

        val editTextView = dialogView.findViewById<TextView>(R.id.textViewEdit)
        val deleteTextView = dialogView.findViewById<TextView>(R.id.textViewDelete)

        editTextView.setOnClickListener {
            if (selectedTasks.isNotEmpty()) {
                editSelectedTasks()
            } else {
                Toast.makeText(context, "No tasks selected to edit", Toast.LENGTH_SHORT).show()
            }
            dialog.dismiss()
        }

        deleteTextView.setOnClickListener {
            if (selectedTasks.isNotEmpty()) {
                showDeleteConfirmationDialog()
            } else {
                Toast.makeText(context, "No tasks selected to delete", Toast.LENGTH_SHORT).show()
            }
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun editSelectedTasks() {
        val builder = AlertDialog.Builder(context)

        // Inflating the custom dialog layout
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_edit_multiple, null)
        builder.setView(dialogView)

        // Create the dialog from builder
        val dialog = builder.create()

        // Set the dialog title (optional)
        val titleTextView = dialogView.findViewById<TextView>(R.id.editDialogTitle)
        titleTextView.text = "Edit Tasks"

        // LinearLayout that will hold the EditTexts for each task
        val linearLayout = dialogView.findViewById<LinearLayout>(R.id.linearLayoutMultipleEdit)
        val editTextMap = HashMap<Task, EditText>()

        // Dynamically add EditText for each selected task
        selectedTasks.forEach { task ->
            val editText = EditText(context).apply {
                setText(task.description)
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    setMargins(0, 8, 0, 8)  // Add margins for better spacing
                }
            }
            linearLayout.addView(editText)
            editTextMap[task] = editText
        }

        // Save button: Save the task descriptions, uncheck tasks, and dismiss the dialog
        val saveButton = dialogView.findViewById<Button>(R.id.btnSave)
        saveButton.setOnClickListener {
            editTextMap.forEach { (task, editText) ->
                if (editText.text.toString().isNotBlank()) {
                    task.description = editText.text.toString()
                } else {
                    Toast.makeText(context, "Task description cannot be empty", Toast.LENGTH_SHORT).show()
                }
            }
            selectedTasks.forEach { task ->
                task.isChecked = false
            }
            notifyDataSetChanged()
            dialog.dismiss()  // Dismiss the dialog after saving
        }

        // Cancel button: Uncheck tasks and dismiss the dialog
        val cancelButton = dialogView.findViewById<Button>(R.id.btnCancel)
        cancelButton.setOnClickListener {
            selectedTasks.forEach { task ->
                task.isChecked = false
            }
            notifyDataSetChanged()
            dialog.dismiss()  // Dismiss the dialog when cancel is clicked
        }

        // Show the dialog
        dialog.show()
    }


    private fun showDeleteConfirmationDialog() {
        val builder = AlertDialog.Builder(context)

        // Inflate the custom delete confirmation dialog layout
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_delete_confirmation, null)
        builder.setView(dialogView)

        // Create the dialog from builder
        val dialog = builder.create()

        // Set up the "Yes" button
        val yesButton = dialogView.findViewById<Button>(R.id.btnDeleteYes)
        yesButton.setOnClickListener {
            deleteSelectedTasks()
            dialog.dismiss()  // Dismiss the dialog after confirming deletion
        }

        // Set up the "No" button
        val noButton = dialogView.findViewById<Button>(R.id.btnDeleteNo)
        noButton.setOnClickListener {
            dialog.dismiss()  // Just dismiss the dialog without deleting
        }

        // Show the dialog
        dialog.show()
    }


    private fun deleteSelectedTasks() {
        taskList.removeAll(selectedTasks)
        selectedTasks.clear()
        notifyDataSetChanged()
        Toast.makeText(context, "Selected tasks deleted", Toast.LENGTH_SHORT).show()
    }

    private data class ViewHolder(
        val checkBox: CheckBox,
        val textViewTask: TextView,
        val imageView: ImageView
    )
}