package com.emanuel_dam.contabilidad.UI.CalcularMasas

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.emanuel_dam.contabilidad2.R
import com.emanuel_dam.contabilidad2.databinding.ActivityCalcularMasasBinding

class CalcularMasasActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCalcularMasasBinding;
    private var cantidadActual:String = "";
    private var unidadDeMedidaSeleccionada = 0;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCalcularMasasBinding.inflate(layoutInflater);

        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        inicializarUI()

    }

    private fun inicializarUI() {
        inicializarEventos()
    }

    private fun inicializarEventos() {
        binding.botonRegresarDesdeCalcularMasas.setOnClickListener {
            onBackPressed();
        }

        binding.disminuirCantidadDeProductos.setOnClickListener {
            binding.resultadoCalculo.text = "________"
            disminuirNumeroDeProductos()
        }

        binding.aumentarCantidadDeProductos.setOnClickListener {
            binding.resultadoCalculo.text = "________"
            aumentarNumeroDeProductos();
        }
        binding.grupoDeBotones.setOnCheckedChangeListener { _, radioBotonChequeado ->
            unidadDeMedidaSeleccionada = determinarUnidadDeMedidaSelecionada(radioBotonChequeado)
        }

        binding.botonDarResultado.setOnClickListener {
            calcularResultado()
        }

        binding.editTextCostoUnitario.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                binding.resultadoCalculo.text = "________"
            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {}
        })

        binding.editTextCantidadDeProductos.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                binding.resultadoCalculo.text = "________"
            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {}
        })
    }


    private fun calcularResultado() {

        if(binding.editTextCostoUnitario.text?.isEmpty() == true){
            binding.editTextCostoUnitario.error = "El campo COSTO no puede estar vacío"
        } else if(binding.editTextCantidadDeProductos.text?.isEmpty() == true){
            binding.editTextCantidadDeProductos.error = "El campo CANTIDAD no puede estar vacío"
        } else {

            val librasAKilogramos = 2.20462;
            var precioTotal = 0.0;
            var costoUnitario =  binding.editTextCostoUnitario.text.toString().toDouble();
            var cantidadDeProductos = binding.editTextCantidadDeProductos.text.toString().toDouble();

            formatearValor(precioTotal)

            when(unidadDeMedidaSeleccionada){
                0-> {
                    precioTotal = costoUnitario * cantidadDeProductos;
                    if(precioTotal > 2100000000){
                        binding.resultadoCalculo.text = "Imposible"
                    } else {
                        binding.resultadoCalculo.text = formatearValor(precioTotal);
                    }
                }
                1-> {
                    precioTotal = costoUnitario * ( cantidadDeProductos / librasAKilogramos )
                    if(precioTotal > 2100000000){
                        binding.resultadoCalculo.text = "Imposible"
                    } else {
                        binding.resultadoCalculo.text = formatearValor(precioTotal);
                    }
                }
                2-> {
                    precioTotal = costoUnitario * cantidadDeProductos;
                    if(precioTotal > 2100000000){
                        binding.resultadoCalculo.text = "Imposible"
                    } else {
                        binding.resultadoCalculo.text = formatearValor(precioTotal);
                    }
                }
            }
        }

    }

    private fun formatearValor(precioTotal: Double): String {
        val valorFormateado = if (precioTotal % 1 == 0.0) {
            precioTotal.toInt().toString()
        } else {
            String.format("%.2f", precioTotal)
        }
        return valorFormateado;
    }


    private fun determinarUnidadDeMedidaSelecionada(radioBotonChequeado: Int): Int {

        binding.radioBotonKilogramos.isChecked = false;
        var elementoChequeado = 0;


        binding.textViewKilogramos.setBackgroundColor(0)
        binding.textViewLibras.setBackgroundColor(0)
        binding.textViewUnidadesDeMedida.setBackgroundColor(0)

        when(radioBotonChequeado){
            R.id.radioBotonKilogramos -> {
                binding.textViewKilogramos.setBackgroundResource(R.drawable.personalizacion_text_view_bordes)
                binding.radioBotonKilogramos.isChecked = true;
                binding.resultadoCalculo.text = "________"
                elementoChequeado = 0;
            }
            R.id.radioBotonLibras -> {
                binding.textViewLibras.setBackgroundResource(R.drawable.personalizacion_text_view_bordes)
                binding.radioBotonLibras.isChecked = true;
                binding.resultadoCalculo.text = "________"
                elementoChequeado = 1;
            }
            R.id.radioBotonUnidadesDeMedida -> {
                binding.textViewUnidadesDeMedida.setBackgroundResource(R.drawable.personalizacion_text_view_bordes)
                binding.radioBotonUnidadesDeMedida.isChecked = true;
                binding.resultadoCalculo.text = "________"
                elementoChequeado = 2;
            }

        }
        return elementoChequeado;
    }

    private fun disminuirNumeroDeProductos() {
        cantidadActual = binding.editTextCantidadDeProductos.text.toString()

        if(cantidadActual.isEmpty()){
            binding.editTextCantidadDeProductos.text = Editable.Factory.getInstance().newEditable("")
        } else {
            try {
                var valorActual = cantidadActual.toDouble()

                if(valorActual -1 < 0){
                    binding.editTextCantidadDeProductos.text = Editable.Factory.getInstance().newEditable("0")
                } else {
                    valorActual--;

                    val valorFormateado = if (valorActual % 1 == 0.0) {
                        valorActual.toInt().toString()
                    } else {
                        String.format("%.2f", valorActual)
                    }
                    binding.editTextCantidadDeProductos.text = Editable.Factory.getInstance().newEditable(valorFormateado)
                }


            }  catch (e: NumberFormatException) {
                binding.editTextCantidadDeProductos.text = Editable.Factory.getInstance().newEditable("1")
            }
        }
    }


    private fun aumentarNumeroDeProductos() {

        cantidadActual = binding.editTextCantidadDeProductos.text.toString()

        if (cantidadActual.isEmpty()) {
            binding.editTextCantidadDeProductos.text = Editable.Factory.getInstance().newEditable("1")
        } else {
            try {
                var valorActual = cantidadActual.toDouble()
                valorActual++

                val valorFormateado = if (valorActual % 1 == 0.0) {
                    valorActual.toInt().toString()
                } else {
                    String.format("%.2f", valorActual)
                }
                binding.editTextCantidadDeProductos.text = Editable.Factory.getInstance().newEditable(valorFormateado)
            } catch (e: NumberFormatException) {
                binding.editTextCantidadDeProductos.text = Editable.Factory.getInstance().newEditable("1")
            }
        }
    }


}