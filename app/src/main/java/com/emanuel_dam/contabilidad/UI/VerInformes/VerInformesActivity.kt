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
import com.emanuel_dam.contabilidad.UI.VerInformes.adapters.informesPorFecha.informesPorFechaAdapter
import com.emanuel_dam.contabilidad2.R
import com.emanuel_dam.contabilidad2.databinding.ActivityVerInformesBinding
import kotlinx.coroutines.launch

class VerInformesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityVerInformesBinding;
    lateinit var baseDeDatos:Ventas;
    private var listaDeInformesPorFecha: MutableList<Informe> = mutableListOf()
    private lateinit var informesPorFechaAdapter: informesPorFechaAdapter;

    companion object{
        const val FECHA_INFORME = "fecha_informe";
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        binding = ActivityVerInformesBinding.inflate(layoutInflater);
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
        inicializarDatosDeLaBaseDeDatos()
        inicializarEventos();
    }

    private fun inicializarEventos() {
        binding.botonRegresarDesdeVerInformes.setOnClickListener {
            onBackPressed();
        }
    }

    private fun inicializarDatosDeLaBaseDeDatos() {

        lifecycleScope.launch {
            val resultado = baseDeDatos.informeDao().getTodosLosInformes();

            resultado.observe(this@VerInformesActivity) { informes ->
                listaDeInformesPorFecha = informes

                val informesAgrupadosPorFecha = listaDeInformesPorFecha.groupBy { it.fechaInforme }

                informesPorFechaAdapter = informesPorFechaAdapter(informesAgrupadosPorFecha as Map<String, MutableList<Informe>>) { fecha ->
                    abrirVistaInformesPorFecha(fecha)
                }
                binding.recyclerViewPorFecha.adapter = informesPorFechaAdapter
                binding.recyclerViewPorFecha.layoutManager = LinearLayoutManager(this@VerInformesActivity)
            }
        }
    }

    private fun abrirVistaInformesPorFecha(fecha: String) {
        val intent = Intent(this, VerInformesPorHoraActivity::class.java)
        intent.putExtra(FECHA_INFORME, fecha)
        startActivity(intent)
    }

}