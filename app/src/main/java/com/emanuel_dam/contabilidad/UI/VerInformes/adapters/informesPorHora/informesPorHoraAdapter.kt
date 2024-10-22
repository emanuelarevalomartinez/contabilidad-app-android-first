package com.emanuel_dam.contabilidad.UI.VerInformes.adapters.informesPorHora

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.emanuel_dam.contabilidad.Room.Entities.Informe
import com.emanuel_dam.contabilidad.Room.db.Ventas
import com.emanuel_dam.contabilidad.UI.VerInformes.adapters.informesPorHora.DiffUtil.InformePorHoraDifUtil
import com.emanuel_dam.contabilidad2.R

class informesPorHoraAdapter(
    var listaDeInformesPorHora: List<Informe>,
    private var hacerClickEnInformesPorHora: (Informe) -> Unit,
    private val lifecycleOwner: LifecycleOwner,
    private val baseDeDatos: Ventas,
): RecyclerView.Adapter<informesPorHoraViewHolder>() {

    fun actualizarLista(nuevaLista: List<Informe>){
        val diferenciasEnInformes = InformePorHoraDifUtil(listaDeInformesPorHora, nuevaLista);
        val resultadoComparacionEntreListas = DiffUtil.calculateDiff(diferenciasEnInformes);
        listaDeInformesPorHora = nuevaLista;
        resultadoComparacionEntreListas.dispatchUpdatesTo(this);

    }

    fun actualizarInforme(idInforme: Long, completado: Boolean) {
        val index = listaDeInformesPorHora.indexOfFirst { it.id == idInforme }
        if (index != -1) {
            listaDeInformesPorHora[index].completado = completado

            notifyItemChanged(index)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): informesPorHoraViewHolder {
        val view = (LayoutInflater.from(parent.context).inflate(R.layout.item_informe_por_hora, parent, false))
        return informesPorHoraViewHolder(view, lifecycleOwner,baseDeDatos);
    }

    override fun onBindViewHolder(holder: informesPorHoraViewHolder, position: Int) {
        holder.renderizado(listaDeInformesPorHora[position],hacerClickEnInformesPorHora);
    }

    override fun getItemCount(): Int {
        return listaDeInformesPorHora.size;
    }

}