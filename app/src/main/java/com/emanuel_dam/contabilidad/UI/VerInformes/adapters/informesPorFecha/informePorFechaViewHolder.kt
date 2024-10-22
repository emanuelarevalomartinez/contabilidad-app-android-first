package com.emanuel_dam.contabilidad.UI.VerInformes.adapters.informesPorFecha

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.emanuel_dam.contabilidad2.databinding.ItemInformePorFechaBinding

class informePorFechaViewHolder( view: View ): RecyclerView.ViewHolder(view) {

    private var binding = ItemInformePorFechaBinding.bind(view);

    fun renderizar(fecha: String, hacerClickEnInformesPorFecha: (String) -> Unit){
        binding.textViewFechaInforme.text = fecha;

        binding.root.setOnClickListener {
            hacerClickEnInformesPorFecha(fecha);
        }
    }

}