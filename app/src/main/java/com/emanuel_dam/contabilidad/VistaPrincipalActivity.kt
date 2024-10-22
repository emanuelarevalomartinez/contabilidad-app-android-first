package com.emanuel_dam.contabilidad

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.emanuel_dam.contabilidad.Room.db.Ventas
import com.emanuel_dam.contabilidad.UI.AcercaDe.AcercaDeActivity
import com.emanuel_dam.contabilidad.UI.CalcularMasas.CalcularMasasActivity
import com.emanuel_dam.contabilidad.UI.CrearNuevoInforme.CrearNuevoInformeActivity
import com.emanuel_dam.contabilidad.UI.VerInformes.VerInformesActivity
import com.emanuel_dam.contabilidad2.R
import com.emanuel_dam.contabilidad2.databinding.ActivityVistaPrincipalBinding

class VistaPrincipalActivity : AppCompatActivity() {

    private lateinit var binding: ActivityVistaPrincipalBinding;
     lateinit var baseDeDatos: Ventas;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        binding = ActivityVistaPrincipalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        baseDeDatos = Ventas.getDatabaseVentas(applicationContext)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        inicializarUI()
    }

    private fun inicializarUI() {
        inicializarEventos();
        //  insertarInformesDeEjemplo()
    }


    private fun inicializarEventos() {

        binding.botonCrearNuevoInforme.setOnClickListener {
                var intent = Intent(this, CrearNuevoInformeActivity::class.java);
               startActivity(intent);
        }

        binding.botonVerLosInformes.setOnClickListener {
              var intent = Intent(this, VerInformesActivity::class.java);
              startActivity(intent);
        }

        binding.botonCalcularMasas.setOnClickListener {
                var intent = Intent(this, CalcularMasasActivity::class.java);
               startActivity(intent);
        }

        binding.botonAcercaDe.setOnClickListener {
            var intent = Intent(this, AcercaDeActivity::class.java);
            startActivity(intent);
        }
    }
}