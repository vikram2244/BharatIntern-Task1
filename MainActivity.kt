package com.example.calculator

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.calculator.R
import com.google.android.material.button.MaterialButton

class MainActivity : AppCompatActivity() {
    private lateinit var resultTextView: TextView
    private val currentNumber = StringBuilder("0")
    private var currentTotal = 0.0
    private var currentOperator = ' '
    private var isOperatorClicked = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        resultTextView = findViewById(R.id.resultTextView)
        setDigitClickListeners()
        setOperatorClickListeners()

        findViewById<MaterialButton>(R.id.clearbutton).setOnClickListener {
            clearAll()
        }

        findViewById<MaterialButton>(R.id.Deletebutton).setOnClickListener {
            deleteLastCharacter()
        }

        findViewById<MaterialButton>(R.id.equalsbutton).setOnClickListener {
            if (currentOperator != ' ' && !isOperatorClicked) {
                val number = currentNumber.toString().toDouble()
                performPendingOperation(number)
                currentNumber.clear()
                currentNumber.append(currentTotal.toString())
                resultTextView.text = currentNumber.toString()
                currentOperator = ' '
            }
        }
    }

    private fun setDigitClickListeners() {
        val digitButtons = listOf(
            R.id.button0, R.id.button1, R.id.button2, R.id.button3, R.id.button4,
            R.id.button5, R.id.button6, R.id.button7, R.id.button8, R.id.button9,
            R.id.button00
        )
        digitButtons.forEach { buttonId ->
            val digit = findViewById<MaterialButton>(buttonId).text.toString()
            findViewById<MaterialButton>(buttonId).setOnClickListener {
                handleDigitClick(digit)
            }
        }
    }

    private fun setOperatorClickListeners() {
        findViewById<MaterialButton>(R.id.additionbutton).setOnClickListener { handleOperatorClick('+') }
        findViewById<MaterialButton>(R.id.subtractbutton).setOnClickListener { handleOperatorClick('-') }
        findViewById<MaterialButton>(R.id.multiplybutton).setOnClickListener { handleOperatorClick('*') }
        findViewById<MaterialButton>(R.id.Dividerbutton).setOnClickListener { handleOperatorClick('/') }
        findViewById<MaterialButton>(R.id.percentagebutton).setOnClickListener { handleOperatorClick('%') }
        findViewById<MaterialButton>(R.id.dotbutton).setOnClickListener { handleOperatorClick('.') }
    }

    private fun handleDigitClick(digit: String) {
        if (digit == "." && currentNumber.contains(".")) {
            // Avoid adding more than one decimal point
            return
        }
        if (isOperatorClicked) {
            currentNumber.clear()
            isOperatorClicked = false
        }
        if (currentNumber.toString() == "0") {
            currentNumber.clear()
        }
        currentNumber.append(digit)
        resultTextView.text = currentNumber.toString()
    }
    private fun handleOperatorClick(operator: Char) {
        if (operator == '%') {
            val number = currentNumber.toString().toDouble() / 100
            currentNumber.clear()
            currentNumber.append(number)
            resultTextView.text = currentNumber.toString()
        } else if (operator == '.') {
            if (!currentNumber.contains(".")) {
                currentNumber.append(".")
                resultTextView.text = currentNumber.toString()
            }
        } else {
            if (!isOperatorClicked) {
                val number = currentNumber.toString().toDouble()
                performPendingOperation(number)
                currentNumber.clear()
                currentTotal = number
            }
            currentOperator = operator
            isOperatorClicked = true
            when (operator) {
                '+' -> resultTextView.append(" + ")
                '-' -> resultTextView.append(" - ")
                '*' -> resultTextView.append(" * ")
                '/' -> resultTextView.append(" / ")
            }
        }
    }

    private fun performPendingOperation(number: Double) {
        when (currentOperator) {
            '+' -> currentTotal += number
            '-' -> currentTotal -= number
            '*' -> currentTotal *= number
            '/' -> {
                if (number != 0.0) {
                    currentTotal /= number
                } else {
                    // Handle division by zero
                }
            }
        }
    }

    private fun clearAll() {
        currentNumber.clear()
        currentNumber.append("0")
        currentTotal = 0.0
        currentOperator = ' '
        isOperatorClicked = false
        resultTextView.text = currentNumber.toString()
    }

    private fun deleteLastCharacter() {
        if (currentNumber.isNotEmpty()) {
            currentNumber.deleteCharAt(currentNumber.length - 1)
            if (currentNumber.isEmpty()) {
                currentNumber.append("0")
            }
            resultTextView.text = currentNumber.toString()
        }
    }
}
