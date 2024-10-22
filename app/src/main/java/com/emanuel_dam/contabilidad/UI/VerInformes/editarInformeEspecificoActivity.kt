package com.emanuel_dam.contabilidad.UI.VerInformes

import android.content.Intent
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
import com.emanuel_dam.contabilidad2.databinding.ActivityEditarInformeEspecificoBinding
import kotlinx.coroutines.launch

class editarInformeEspecificoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditarInformeEspecificoBinding;
    lateinit var baseDeDatos: Ventas;
    private var idInforme: Long = -1

    override fun onCreate(savedInstanceState: Bundle?) {

        binding = ActivityEditarInformeEspecificoBinding.inflate(layoutInflater);
        baseDeDatos = Ventas.getDatabaseVentas(applicationContext);

        idInforme = intent.getLongExtra("id_informe",-1)

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        inicializarUI(idInforme)

    }

    private fun inicializarUI(idInforme: Long) {
        inicializarDatosDeLaBaseDeDatos(idInforme)
        inicializarEventos()
    }

    private fun inicializarEventos() {
        binding.botonRegresarDesdeEditarInforme.setOnClickListener {
            onBackPressed();
        }
        binding.botonGuardarInformeEditado.setOnClickListener {
            validarCamposActualizarInforme();
        }
    }

    private fun inicializarDatosDeLaBaseDeDatos(idInforme: Long) {

        if (idInforme != 0L) {
            lifecycleScope.launch {
                baseDeDatos.informeDao().getInformePorId(this@editarInformeEspecificoActivity.idInforme)
                    .observe(this@editarInformeEspecificoActivity) { informe ->
                        if (informe != null) {
                            binding.textViewFechaInformeEspecificoEdiccion.text = informe.fechaInforme;
                            binding.editTextNombreDeUsuarioCompleto.setText(informe.nombreUsuario);
                            binding.editTextMovilUsuario.setText(informe.movilUsuario.toString());
                            binding.editTextMontoTotalAPagar.setText(informe.montoTotal.toString());
                            binding.editTextCodigoSMSDeConformacion.setText(informe.codigoSMS);
                            binding.textViewHoraInformeEspecificoEdicion.text = informe.horaCreacionInforme;
                            binding.editTextProductosYSusCantidades.setText(informe.productosYSuCantidad);

                        }
                    }
            }
        }

    }

    private fun validarCamposActualizarInforme() {
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
            actualizarInforme()
        }
    }

    private fun actualizarInforme() {

        var estadoActualInformeCompletado = intent.getBooleanExtra("estado_informe_completado",false)

        lifecycleScope.launch {
            var filasDelInformeModificadas = baseDeDatos.informeDao().actualizarInforme(
                Informe(
                    id = idInforme,
                    fechaInforme = binding.textViewFechaInformeEspecificoEdiccion.text.toString(),
                    nombreUsuario = binding.editTextNombreDeUsuarioCompleto.text.toString(),
                    movilUsuario = binding.editTextMovilUsuario.text.toString().toInt(),
                    montoTotal = binding.editTextMontoTotalAPagar.text.toString().toDouble(),
                    codigoSMS = binding.editTextCodigoSMSDeConformacion.text.toString(),
                    horaCreacionInforme = binding.textViewHoraInformeEspecificoEdicion.text.toString(),
                    productosYSuCantidad = binding.editTextProductosYSusCantidades.text.toString(),
                    editado = true,
                    completado = estadoActualInformeCompletado,
                )
            )

            if(filasDelInformeModificadas > 0){
                val intent = Intent()
                intent.putExtra("id_informe_actualizado", idInforme)
                setResult(RESULT_OK, intent)
                Toast.makeText(applicationContext, "Actualizado", Toast.LENGTH_SHORT).show();
                onBackPressed();
            } else {
                Toast.makeText(applicationContext, "No se actualizo", Toast.LENGTH_LONG).show();
            }

        }

    }


}