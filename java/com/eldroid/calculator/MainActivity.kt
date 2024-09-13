package com.eldroid.calculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    private lateinit var tvResult: TextView
    private lateinit var tvPreviousOperation: TextView
    private var number1: Double = 0.0
    private var number2: Double = 0.0
    private var operator: String = ""
    private var input: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvResult = findViewById(R.id.tvResult)
        tvPreviousOperation = findViewById(R.id.tvPreviousOperation)

        val btn1: Button = findViewById(R.id.btn1)
        val btn2: Button = findViewById(R.id.btn2)
        val btn3: Button = findViewById(R.id.btn3)
        val btn4: Button = findViewById(R.id.btn4)
        val btn5: Button = findViewById(R.id.btn5)
        val btn6: Button = findViewById(R.id.btn6)
        val btn7: Button = findViewById(R.id.btn7)
        val btn8: Button = findViewById(R.id.btn8)
        val btn9: Button = findViewById(R.id.btn9)
        val btn0: Button = findViewById(R.id.btn0)
        val btnPlus: Button = findViewById(R.id.btnPlus)
        val btnMinus: Button = findViewById(R.id.btnMinus)
        val btnMultiply: Button = findViewById(R.id.btnMultiply)
        val btnDivide: Button = findViewById(R.id.btnDivide)
        val btnEquals: Button = findViewById(R.id.btnEquals)
        val btnModulo: Button = findViewById(R.id.btnModulo)
        val btnDot: Button = findViewById(R.id.btnDot)
        val btnClear: Button = findViewById(R.id.btnAllClear)
        val btnDelete: Button = findViewById(R.id.btnDelete)

        val numberButtons = listOf(btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9, btn0)
        numberButtons.forEach { button ->
            button.setOnClickListener {
                input += button.text
                tvResult.text = input
            }
        }

        val operatorButtons = mapOf(
            btnPlus to "+",
            btnMinus to "-",
            btnMultiply to "*",
            btnDivide to "/",
            btnModulo to "%"
        )

        operatorButtons.forEach { (button, op) ->
            button.setOnClickListener {

                // Ignore if input is empty (no number entered) or if an operator is already set
                if (input.isEmpty()) {
                    return@setOnClickListener // Don't apply operator if no number is input
                }

                if (input.isNotEmpty()) {
                    operator = op
                    number1 = input.toDouble()
                    input = ""
                }

            }
        }

        btnEquals.setOnClickListener {

            number2 = input.toDouble()

            // Calculate result based on the operator
            val result = when (operator) {
                "+" -> number1 + number2
                "-" -> number1 - number2
                "*" -> number1 * number2
                "/" -> number1 / number2
                "%" -> number1 % number2
                else -> 0.0
            }

            // Format number1 and number2 based on whether they are whole numbers or have decimals
            val formattedNumber1 = if (number1 % 1 == 0.0) {
                number1.toLong().toString()
            } else {
                number1.toString()
            }

            val formattedNumber2 = if (number2 % 1 == 0.0) {
                number2.toLong().toString()
            } else {
                number2.toString()
            }

            // Display the previous operation as formatted (without decimals for whole numbers)
            val equationText = "$formattedNumber1 $operator $formattedNumber2"

            // Format the result based on whether it has decimals or not
            val resultText = if (result % 1 == 0.0) {
                result.toLong().toString()
            } else {
                result.toString()
            }

            // Update the TextViews
            tvPreviousOperation.text = equationText
            tvResult.text = resultText
            input = resultText
        }


        btnDelete.setOnClickListener {
            if (input.isNotEmpty()) {
                input = input.dropLast(1)
                tvResult.text = if (input.isEmpty()) "0" else input
            }
        }

        btnDot.setOnClickListener {
            if (!input.contains(".")) {
                input += "."
                tvResult.text = input
            }
        }

        btnClear.setOnClickListener {
            input = ""
            operator = ""
            number1 = 0.0
            number2 = 0.0
            tvResult.text = "0"
            tvPreviousOperation.text = ""
        }
    }
}