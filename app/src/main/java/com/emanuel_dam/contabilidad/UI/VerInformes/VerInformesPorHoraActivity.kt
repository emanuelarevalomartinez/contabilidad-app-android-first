package com.emanuel_dam.contabilidad.UI.VerInformes

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.emanuel_dam.contabilidad.Room.Entities.Informe
import com.emanuel_dam.contabilidad.Room.db.Ventas
import com.emanuel_dam.contabilidad.UI.VerInformes.adapters.informesPorHora.informesPorHoraAdapter
import com.emanuel_dam.contabilidad2.R
import com.emanuel_dam.contabilidad2.databinding.ActivityVerInformesPorHoraBinding
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class VerInformesPorHoraActivity : AppCompatActivity() {

    private lateinit var binding: ActivityVerInformesPorHoraBinding;
    private lateinit var informesPorHoraAdapter: informesPorHoraAdapter;
    lateinit var baseDeDatos: Ventas;
    private var listaDeInformesPorHora: List<Informe> = listOf();


    companion object{
        const val ID_INFORME = "id_informe";
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        binding = ActivityVerInformesPorHoraBinding.inflate(layoutInflater);
        baseDeDatos = Ventas.getDatabaseVentas(applicationContext);

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        inicializarUI();

    }

    private fun inicializarUI() {
        inicializarDatosDeLaBaseDeDatos();
        inicializarEventos()
    }

    private fun inicializarEventos() {
        binding.botonRegresarDesdeVerInformesPorHora.setOnClickListener {
            onBackPressed();
        }
    }

    private fun inicializarDatosDeLaBaseDeDatos() {


        val fechaSeleccionada = intent.getStringExtra("fecha_informe") ?: return

        binding.barraInformesPorHora.text = fechaSeleccionada

        lifecycleScope.launch {
            val resultado = baseDeDatos.informeDao().getTodosLosInformes()

            resultado.observe(this@VerInformesPorHoraActivity) { informes ->

                listaDeInformesPorHora = informes.filter { it.fechaInforme == fechaSeleccionada }.toMutableList()

                val formatoHora = SimpleDateFormat("hh:mm:ss a", Locale.getDefault())
                (listaDeInformesPorHora as MutableList<Informe>).sortBy { informe ->
                    try {
                        formatoHora.parse(informe.horaCreacionInforme)
                    } catch (e: Exception) {
                        null
                    }
                }

                if (::informesPorHoraAdapter.isInitialized) {
                    informesPorHoraAdapter.actualizarLista(listaDeInformesPorHora)
                } else {

                    informesPorHoraAdapter = informesPorHoraAdapter(
                        listaDeInformesPorHora,
                        { informe ->
                            hacerClickEnInformesPorHora(informe.id)
                        },

                        this@VerInformesPorHoraActivity,
                        baseDeDatos
                    )
                    binding.recyclerViewPorHoras.adapter = informesPorHoraAdapter
                    binding.recyclerViewPorHoras.layoutManager =
                        LinearLayoutManager(this@VerInformesPorHoraActivity)
                }
            }
        }
    }

    private fun hacerClickEnInformesPorHora(idInforme: Long) {
        val intent = Intent(this, VerInformeEspecificoActivity::class.java)
        intent.putExtra(ID_INFORME, idInforme)
        startActivityForResult(intent, 100)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {

            val idInformeEliminado = data.getLongExtra("id_informe_eliminado", -1)
            if (idInformeEliminado != -1L) {
                // Eliminar el informe de la lista y notificar al adaptador
                val index = listaDeInformesPorHora.indexOfFirst { it.id == idInformeEliminado }
                if (index != -1) {
                    listaDeInformesPorHora = listaDeInformesPorHora.filter { it.id != idInformeEliminado }
                    informesPorHoraAdapter.actualizarLista(listaDeInformesPorHora)
                }
                return
            }

            // Si no se eliminó, puede que se haya actualizado el estado de "completado"
            val idInformeActualizado = data.getLongExtra("id_informe_actualizado", -1)
            val informeCompletado = data.getBooleanExtra("informe_completado", false)

            if (idInformeActualizado != -1L) {
                val index = listaDeInformesPorHora.indexOfFirst { it.id == idInformeActualizado }
                if (index != -1) {
                    informesPorHoraAdapter.actualizarInforme(idInformeActualizado, informeCompletado)
                    listaDeInformesPorHora[index].completado = informeCompletado
                }
            }

        }
    }
}