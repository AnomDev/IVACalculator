package com.serbladev.ivacalculator

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.serbladev.ivacalculator.databinding.ActivityMainBinding
import java.text.NumberFormat
import kotlin.math.ceil

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    // Para generar un binding y evitarnos los findViewById de cada vista se pone \ntodo este bloque
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.calcularImporteSinIvaBtn.setOnClickListener { calcularImporteSinIVA() }

        binding.calcularImporteConIvaBtn.setOnClickListener { calcularImporteConIVA() }



        // Aquí llamamos al método creado al final de MainActivity para que el teclado se cierre al pulsar una keyCode
        // (en este caso la tecla ENTER)

        binding.importeConIvaTiet.setOnKeyListener { view, keyCode, _ -> handleKeyEvent(view, keyCode)
        }


    }

    private fun calcularImporteSinIVA() {
        val stringInTextField = binding.importeConIvaTiet.text.toString()

        // Para que toDouble() funcione, necesitamos convertir el texto 'editable' del EditText a String, por eso le metemos el toString()
        // ACTUALIZACIÓN:
        // Si dejabamos toDouble() la app crasheaba si el usuario no introducía ningun valor en el EditText, para evitar eso lo cambiamos por
        // toDoubleOrNull() y creamos una estructura de control mediante if...else para comprobar si el usuario ha introducido o no algun valor.
        val importeIVA = stringInTextField.toDoubleOrNull()

        if (importeIVA == null){
            binding.importeCalculadoTv.text = ""
            Toast.makeText(this,  "Por favor, introduce un importe con IVA para convertir", Toast.LENGTH_SHORT).show()
            return
        }

        //Aquí elegimos el radiobutton que esta seleccionado pero...
        val selectedId = binding.tipoGravamenRg.checkedRadioButtonId

        //...para ello debemos decirle cuál de los tres es el que ha seleccionado el usuario mediante un 'when'
        val tipoGravamen = when (selectedId) {
            R.id.general_21_rb -> 1.21
            R.id.reducido_10_rb -> 1.10
            else -> 1.04
        }
        // En este caso la variable es mutable porque, si el usuario elige la opción de redondear, nosotros deberemos cambiar el valor de tip
        var importeSinIVA = importeIVA / tipoGravamen

        var importeDelImpuesto = importeIVA - importeSinIVA

        // Comprobamos si está marcado el switch
        val roundTip = binding.redondeoAlzaSw.isChecked

        //Si el switch está marcado, se desarrolla la siguiente condición para el redondeo
        //En nuestro caso sólo vamos a redondear a la alza siempre y para ello utilizamos la función matemática de "ceil" (literalmente 'hacer techo'),
        //la cual se importa automáticamente.
        if (roundTip) {
            importeSinIVA = ceil(importeSinIVA)
        }

        //Finalmente formatearemos el resultado para que aparezca en función a la divisa predeterminada en función al lenguaje y otros ajustes del móvil
        //de nuestro usuario. Por ejemplo, en Euros hará un formato como €1.234,56 mientras que en dólares lo hará usando $1,234.56.
        //Aquí le decimos que el numero a formatear es el que se genere en tip y que lo guarde en una variable.
        val importeSinIVAFormateado = NumberFormat.getCurrencyInstance().format(importeSinIVA)
        val importeDelImpuestoFormateado = NumberFormat.getCurrencyInstance().format(importeDelImpuesto)

        binding.importeCalculadoTv.text = getString(R.string.importe_calculado, importeSinIVAFormateado)
        binding.importeDelImpuestoTv.text = getString(R.string.importe_del_impuesto, importeDelImpuestoFormateado)

    }

    private fun calcularImporteConIVA() {
        val stringInTextField = binding.importeConIvaTiet.text.toString()

        // Para que toDouble() funcione, necesitamos convertir el texto 'editable' del EditText a String, por eso le metemos el toString()
        // ACTUALIZACIÓN:
        // Si dejabamos toDouble() la app crasheaba si el usuario no introducía ningun valor en el EditText, para evitar eso lo cambiamos por
        // toDoubleOrNull() y creamos una estructura de control mediante if...else para comprobar si el usuario ha introducido o no algun valor.
        val importeSinIVA = stringInTextField.toDoubleOrNull()

        if (importeSinIVA == null){
            binding.importeCalculadoTv.text = ""
            Toast.makeText(this,  "Por favor, introduce un importe base sobre el que calcular", Toast.LENGTH_SHORT).show()
            return
        }

        //Aquí elegimos el radiobutton que esta seleccionado pero...
        val selectedId = binding.tipoGravamenRg.checkedRadioButtonId

        //...para ello debemos decirle cuál de los tres es el que ha seleccionado el usuario mediante un 'when'
        val tipoGravamen = when (selectedId) {
            R.id.general_21_rb -> 1.21
            R.id.reducido_10_rb -> 1.10
            else -> 1.04
        }
        // En este caso la variable es mutable porque, si el usuario elige la opción de redondear, nosotros deberemos cambiar el valor de importe con IVA
        var importeConIVA = importeSinIVA * tipoGravamen

        var importeDelImpuesto = importeConIVA - importeSinIVA

        // Comprobamos si está marcado el switch
        val redondearResultado = binding.redondeoAlzaSw.isChecked

        //Si el switch está marcado, se desarrolla la siguiente condición para el redondeo
        //En nuestro caso sólo vamos a redondear a la alza siempre y para ello utilizamos la función matemática de "ceil" (literalmente 'hacer techo'),
        //la cual se importa automáticamente.
        if (redondearResultado) {
            importeConIVA = ceil(importeConIVA)
        }

        //Finalmente formatearemos el resultado para que aparezca en función a la divisa predeterminada en función al lenguaje y otros ajustes del móvil
        //de nuestro usuario. Por ejemplo, en Euros hará un formato como €1.234,56 mientras que en dólares lo hará usando $1,234.56.
        //Aquí le decimos que el numero a formatear es el que se genere en tip y que lo guarde en una variable.
        val importeConIVAFormateado = NumberFormat.getCurrencyInstance().format(importeConIVA)
        val importeDelImpuestoFormateado = NumberFormat.getCurrencyInstance().format(importeDelImpuesto)

        binding.importeCalculadoTv.text = getString(R.string.importe_calculado, importeConIVAFormateado)
        binding.importeDelImpuestoTv.text = getString(R.string.importe_del_impuesto, importeDelImpuestoFormateado)

    }

    //Éste método permite que el teclado se oculte cuando se presione la tecla Enter.
    private fun handleKeyEvent(view: View, keyCode: Int): Boolean {
        if (keyCode == KeyEvent.KEYCODE_ENTER) {
            // Hide the keyboard
            val inputMethodManager =
                    getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
            return true
        }
        return false
    }

}