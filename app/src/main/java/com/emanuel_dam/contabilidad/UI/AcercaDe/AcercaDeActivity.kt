package com.emanuel_dam.contabilidad.UI.AcercaDe

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.emanuel_dam.contabilidad2.R
import com.emanuel_dam.contabilidad2.databinding.ActivityAcercaDeBinding
import java.util.Calendar

class AcercaDeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAcercaDeBinding;

    override fun onCreate(savedInstanceState: Bundle?) {

        binding = ActivityAcercaDeBinding.inflate(layoutInflater);

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
        inicializarFecha()
        inicializarEventos()
        enlaceDeCorreo()
    }

    private fun inicializarEventos() {
        binding.botonRegresarDesdeAcercaDe.setOnClickListener {
            onBackPressed();
        }
    }


    private fun inicializarFecha() {
        val copyrightSymbol = "\u00A9"

        val añoActual = Calendar.getInstance().get(Calendar.YEAR)

        binding.copyrightSymbolYFecha.text = "${añoActual} ${copyrightSymbol}"

    }

    private fun enlaceDeCorreo() {
        var enlaceDeCorreo = binding.correoLink;
        val email = "emanuelarevalomartinez@gmail.com"

        enlaceDeCorreo.setOnClickListener {

            val intent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:")
                putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
            }

            if(intent.resolveActivity( packageManager ) != null ){
                startActivity(Intent.createChooser(intent, "Enviar correo"));
            } else {
                Toast.makeText(this, "No se encontro ninguna aplicación de correos", Toast.LENGTH_LONG).show();
            }

        }
        // mantener precionado el texto
        enlaceDeCorreo.setOnLongClickListener {
            val intent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:")
                putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
            }

            if(intent.resolveActivity( packageManager ) != null ){
                startActivity(Intent.createChooser(intent, "Enviar correo"));
            } else {
                Toast.makeText(this, "No se encontro ninguna aplicación de correos", Toast.LENGTH_LONG).show();
            }
            true
        }

    }

}