package com.emanuel_dam.contabilidad.UI.VerInformes.adapters.informesPorFecha


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.emanuel_dam.contabilidad.Room.Entities.Informe
import com.emanuel_dam.contabilidad2.R

class informesPorFechaAdapter(
    var listaDeInformesPorFecha: Map<String, MutableList<Informe>>,
    private val hacerClickEnInformesPorFecha: (String) -> Unit,
): RecyclerView.Adapter<informePorFechaViewHolder>()
{

    private val fechasEnOrdenMasRecientePrimero = listaDeInformesPorFecha.keys.sortedDescending()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): informePorFechaViewHolder {
        return informePorFechaViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_informe_por_fecha, parent, false))
    }

    override fun onBindViewHolder(holder: informePorFechaViewHolder, position: Int) {
        holder.renderizar(fechasEnOrdenMasRecientePrimero[position],hacerClickEnInformesPorFecha);
    }

    override fun getItemCount(): Int {
        return listaDeInformesPorFecha.size;
    }
}