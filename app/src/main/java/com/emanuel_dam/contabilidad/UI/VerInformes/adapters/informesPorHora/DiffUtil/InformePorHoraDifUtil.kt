package com.emanuel_dam.contabilidad.UI.VerInformes.adapters.informesPorHora.DiffUtil

import androidx.recyclerview.widget.DiffUtil
import com.emanuel_dam.contabilidad.Room.Entities.Informe

class InformePorHoraDifUtil(
    private var listaViejaDeInformesPorHora: List<Informe>,
    private var listaNuevaDeInformesPorHora: List<Informe>,
): DiffUtil.Callback()
{
    override fun getOldListSize(): Int {
        return listaViejaDeInformesPorHora.size;
    }

    override fun getNewListSize(): Int {
        return listaNuevaDeInformesPorHora.size;
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return listaViejaDeInformesPorHora[oldItemPosition].id == listaNuevaDeInformesPorHora[newItemPosition].id;
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return listaViejaDeInformesPorHora[oldItemPosition] == listaNuevaDeInformesPorHora[newItemPosition];
    }

}