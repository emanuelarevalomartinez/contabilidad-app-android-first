package com.emanuel_dam.contabilidad.UI.CrearNuevoInforme

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.emanuel_dam.contabilidad.Room.Entities.Informe
import com.emanuel_dam.contabilidad.Room.db.Ventas
import com.emanuel_dam.contabilidad2.R
import com.emanuel_dam.contabilidad2.databinding.ActivityCrearNuevoInformeBinding
import kotlinx.coroutines.launch

class CrearNuevoInformeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCrearNuevoInformeBinding;
    lateinit var baseDeDatos: Ventas;

    override fun onCreate(savedInstanceState: Bundle?) {

        binding = ActivityCrearNuevoInformeBinding.inflate(layoutInflater);
        baseDeDatos = Ventas.getDatabaseVentas(applicationContext)

        super.onCreate(savedInstanceState)
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
        binding.botonRegresarDesdeNuevoInforme.setOnClickListener {
            onBackPressed();
        }
        binding.botonGuardarNuevoInforme.setOnClickListener {
            validarCamposNuevoInforme();
        }
    }

    private fun guardarNuevoInforme() {
        lifecycleScope.launch {
            var datosGuardadosConExito = baseDeDatos.informeDao().insertarNuevoInforme(
                Informe(
                    nombreUsuario = binding.editTextNombreDeUsuarioCompleto.text.toString(),
                    movilUsuario = binding.editTextMovilUsuario.text.toString().toInt(),
                    montoTotal = binding.editTextMontoTotalAPagar.text.toString().toDouble(),
                    codigoSMS = binding.editTextCodigoSMSDeConformacion.text.toString(),
                    productosYSuCantidad = binding.editTextProductosYSusCantidades.text.toString(),
                    completado = false,
                    editado = false,
                )
            )

            if(datosGuardadosConExito != -1L){
                Toast.makeText(applicationContext, "Guardado", Toast.LENGTH_SHORT).show();
                onBackPressed();
            } else {
                Toast.makeText(applicationContext, "No se guardo", Toast.LENGTH_LONG).show();
            }
        }
    }

    private fun validarCamposNuevoInforme() {
        if(binding.editTextNombreDeUsuarioCompleto.text?.isEmpty() == true){
            binding.editTextNombreDeUsuarioCompleto.error = "El NOMBRE no puede estar vacío"
        } else if(binding.editTextNombreDeUsuarioCompleto.text?.length!! < 3){
            binding.editTextNombreDeUsuarioCompleto.error = "El NOMBRE debe tener 3 letras o más"
        } else if(binding.editTextMovilUsuario.text?.isEmpty() == true){
            binding.editTextMovilUsuario.error = "El MOVIL no puede estár vacío"
        } else if(binding.editTextMovilUsuario.text?.length!! < 8 || binding.editTextMovilUsuario.text?.length!! > 10){
            binding.editTextMovilUsuario.error = "El MOVIL debe tener entre 8 y 10 números"
        } else if(binding.editTextMontoTotalAPagar.text?.isEmpty() == true){
            binding.editTextMontoTotalAPagar.error = "El MONTO no puede estar vacío"
        } else if(binding.editTextMontoTotalAPagar.text.toString().toDouble() <= 0){
            binding.editTextMontoTotalAPagar.error = "El MONTO no puede ser 0"
        } else if(binding.editTextCodigoSMSDeConformacion.text?.isEmpty() == true){
            binding.editTextCodigoSMSDeConformacion.error = "El CÓDIGO SMS no puede estar vacío"
        } else if(binding.editTextCodigoSMSDeConformacion.text?.length!! < 13){
            binding.editTextCodigoSMSDeConformacion.error = "El CÓDIGO SMS debe tener 13 digitos"
        } else if(binding.editTextProductosYSusCantidades.text?.isEmpty() == true){
            binding.editTextProductosYSusCantidades.error = "El PRODUCTO no puede estar vacío"
        } else if(binding.editTextProductosYSusCantidades.text?.length!! < 5){
            binding.editTextProductosYSusCantidades.error = "El PRODUCTO debe tener 5 letras o más"
        } else {
            guardarNuevoInforme()
        }
    }
}