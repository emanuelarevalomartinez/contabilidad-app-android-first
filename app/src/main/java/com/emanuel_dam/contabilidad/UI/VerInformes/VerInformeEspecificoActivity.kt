package com.emanuel_dam.contabilidad.UI.VerInformes

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.emanuel_dam.contabilidad.Room.db.Ventas
import com.emanuel_dam.contabilidad2.R
import com.emanuel_dam.contabilidad2.databinding.ActivityVerInformeEspecificoBinding
import kotlinx.coroutines.launch

class VerInformeEspecificoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityVerInformeEspecificoBinding;
    lateinit var baseDeDatos: Ventas;
    private var informeCompletado: Boolean = false;
    private var idInforme: Long = -1
    private val RESPUESTA_CORRECTA_SE_ACTUALIZO_EL_INFORME_ESPECIFICO = 1

    override fun onCreate(savedInstanceState: Bundle?) {

        binding = ActivityVerInformeEspecificoBinding.inflate(layoutInflater);
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
        inicializarDatosDeLaBaseDeDatos(idInforme);
        inicializarEventos(idInforme);
    }

    private fun inicializarEventos(idInforme: Long) {
        binding.botonRegresarDesdeVerInformeEspecifico.setOnClickListener {
            onBackPressed();
        }
        binding.checkBoxInformeCompetado.setOnCheckedChangeListener { _, esChequeado ->
            marcarDesmarcarInformeCompletado(idInforme,esChequeado);
        }
        binding.botonEditarInformeEspecifico.setOnClickListener {
            irAEditarInformeEspecifico()
        }
        binding.botonEliminarInformeEspecifico.setOnClickListener {
            mostrarCuadroDeDialogoEliminar()
        }
    }

    private fun irAEditarInformeEspecifico() {
        val intent = Intent(this, editarInformeEspecificoActivity::class.java)
        intent.putExtra("id_informe", idInforme)
        intent.putExtra("estado_informe_completado", informeCompletado)
        startActivityForResult(intent, RESPUESTA_CORRECTA_SE_ACTUALIZO_EL_INFORME_ESPECIFICO)
    }

    private fun inicializarDatosDeLaBaseDeDatos(idInforme: Long) {

        if (idInforme != 0L) {
            lifecycleScope.launch {
                baseDeDatos.informeDao().getInformePorId(idInforme)
                    .observe(this@VerInformeEspecificoActivity) { informe ->
                        if (informe != null) {
                            binding.textViewFechaInformeEspecifico.text = informe.fechaInforme;
                            binding.textViewNombreCompletoInformeEspecifico.text = informe.nombreUsuario;
                            binding.textViewMovilUsuarioInformeEspecifico.text = informe.movilUsuario.toString();
                            binding.textViewMontoTotalAPagarInformeEspecifico.text = informe.montoTotal.toString();
                            binding.textViewCodigoSMSInformeEspecifico.text = informe.codigoSMS;
                            binding.textViewHoraInformeEspecifico.text = informe.horaCreacionInforme;
                            binding.textViewProductosYCantidadesInformeEspecifico.text = informe.productosYSuCantidad;
                            binding.checkBoxInformeCompetado.isChecked = informe.completado;
                            informeCompletado = informe.completado;
                            binding.textViewEstadoEditadoInformeEspecifico.text = if (informe.editado) "EDITADO" else "No-Editado"
                        }
                    }
            }
        }
    }



    private fun marcarDesmarcarInformeCompletado(idInforme: Long, esChequeado: Boolean) {

        binding.root.setBackgroundResource(R.color.primario)

        if(esChequeado){
            binding.root.setBackgroundResource(R.color.completado)
            lifecycleScope.launch {
                baseDeDatos.informeDao().actualizarInformeCompletado(idInforme,true);
            }
        } else {
            binding.root.setBackgroundResource(R.color.primario)
            lifecycleScope.launch {
                baseDeDatos.informeDao().actualizarInformeCompletado(idInforme,false);
            }
        }
    }

    private fun mostrarCuadroDeDialogoEliminar() {
        val dialogo = Dialog(this);
        dialogo.setContentView(R.layout.dialog_eliminar);

        val cancelarEliminarInforme: Button = dialogo.findViewById(R.id.cancelarEliminar);
        val aceptarEliminarInforme: Button = dialogo.findViewById(R.id.aceptarEliminar);

        cancelarEliminarInforme.setOnClickListener {
            dialogo.hide();
        }

        aceptarEliminarInforme.setOnClickListener {
            eliminarInforme(idInforme)
            dialogo.hide();
            Toast.makeText(applicationContext, "Eliminado", Toast.LENGTH_SHORT).show();
        }

        dialogo.show();
    }

    private fun eliminarInforme(idInforme: Long) {
        if (idInforme != 0L) {
            lifecycleScope.launch {
                baseDeDatos.informeDao().eliminarInformePorId(idInforme);
                val data = Intent()
                data.putExtra("id_informe_eliminado", idInforme)
                setResult(RESULT_OK, data)
                finish()
            }
        }
    }

    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {
        val data = Intent()
        data.putExtra("id_informe_actualizado", idInforme)
        data.putExtra("informe_completado", informeCompletado)
        setResult(RESULT_OK, data)
        finish()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RESPUESTA_CORRECTA_SE_ACTUALIZO_EL_INFORME_ESPECIFICO && resultCode == RESULT_OK) {
            val idInformeActualizado = data?.getLongExtra("id_informe_actualizado", -1)
            if (idInformeActualizado != null && idInformeActualizado != -1L) {

                inicializarDatosDeLaBaseDeDatos(idInformeActualizado)
            }
        }
    }

}