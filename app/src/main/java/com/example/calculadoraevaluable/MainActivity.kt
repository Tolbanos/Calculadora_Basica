package com.example.calculadoraevaluable

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.calculadoraevaluable.databinding.ActivityMainBinding
import kotlin.math.sqrt

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var currentNumber: String = ""
    private var currentOperator: String = ""  //guarda el operador actual que se esta utilizando
    private var previousNumber: String = "" // utiliza el numero previo
    private var result: Double = 0.0 //almacena el resultado de la operacion
    private var justPressedEquals: Boolean = false //reinicia los valores

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //iniciamos LO PRIMERO el binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // botones de control
        binding.btnClear.setOnClickListener {
            currentOperator = ""
            currentNumber = ""
            previousNumber = ""
            justPressedEquals = false
            binding.tvResultado.text = "0"
        }
        //elimina el ultimo digito metido
        binding.btnBack?.setOnClickListener {
            if (currentNumber.isNotEmpty()) {
                currentNumber = currentNumber.dropLast(1)
                binding.tvResultado.text = currentNumber.ifEmpty { "0" }
            }
        }

        // operadores
        binding.btnMultiplica.setOnClickListener { handleOperatorClick("*") }
        binding.btnDivide.setOnClickListener { handleOperatorClick("/") }
        binding.btnSuma.setOnClickListener { handleOperatorClick("+") }
        binding.btnResta.setOnClickListener { handleOperatorClick("-") }

        // NUEVO botón para porcentaje %
        binding.btnPercentage?.setOnClickListener {
            if (currentNumber.isNotEmpty()) {
                val percentage = currentNumber.toDouble() / 100
                currentNumber = percentage.toString()
                binding.tvResultado.text = currentNumber
                justPressedEquals = true
            }
        }

        // NUEVO botón para raíz cuadrada
        binding.btnSquareRoot?.setOnClickListener {
            if (currentNumber.isNotEmpty()) {
                val number = currentNumber.toDouble()
                if (number >= 0) { // La raíz cuadrada solo se calcula para números no negativos
                    val squareRoot = sqrt(number)
                    currentNumber = squareRoot.toString()
                    binding.tvResultado.text = currentNumber
                    justPressedEquals = true
                } else {
                    binding.tvResultado.text = "Error"
                }
            }
        }

        // Botones numéricos
        val numericButtons = listOf(
            binding.btnOne, binding.btnTwo, binding.btnThree, binding.btnFour,
            binding.btnFive, binding.btnSix, binding.btnSeven, binding.btnEight,
            binding.btnNine, binding.btnCero
        )

        for (button in numericButtons) {
            button.setOnClickListener {
                if (justPressedEquals) {
                    previousNumber = ""
                    justPressedEquals = false
                }
                handleNumberClick(button.text.toString())
            }
        }

        // llamada botón de Equal "="
        binding.btnEquals.setOnClickListener { calculate() }
    }

    private fun handleNumberClick(number: String) {
        currentNumber += number
        binding.tvResultado.text = currentNumber
    }

    private fun handleOperatorClick(operator: String) {
        if (currentNumber.isNotEmpty()) {
            if (!justPressedEquals) {
                calculate()
            }
            currentOperator = operator
            justPressedEquals = false
            previousNumber = currentNumber
            currentNumber = ""
        }
    }

    private fun calculate() {
        if (currentNumber.isNotEmpty() && previousNumber.isNotEmpty()) {
            val previous = previousNumber.toDouble()
            val current = currentNumber.toDouble()
            result = when (currentOperator) {
                "-" -> previous - current
                "+" -> previous + current
                "*" -> previous * current
                "/" -> if (current != 0.0) previous / current else Double.NaN
                else -> 0.0
            }
            binding.tvResultado.text = result.toString()
            currentNumber = result.toString()
            justPressedEquals = true
        }
    }
}
