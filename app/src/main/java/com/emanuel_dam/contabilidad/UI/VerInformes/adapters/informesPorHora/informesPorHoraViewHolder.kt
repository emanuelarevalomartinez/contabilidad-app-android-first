package com.emanuel_dam.contabilidad.UI.VerInformes.adapters.informesPorHora

import android.app.Dialog
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.emanuel_dam.contabilidad.Room.Entities.Informe
import com.emanuel_dam.contabilidad.Room.db.Ventas
import com.emanuel_dam.contabilidad2.R
import com.emanuel_dam.contabilidad2.databinding.ItemInformePorHoraBinding
import kotlinx.coroutines.launch

class informesPorHoraViewHolder(
    view: View,
    private val lifecycleOwner: LifecycleOwner,
    private val baseDeDatos: Ventas,
): RecyclerView.ViewHolder(view) {

    private var binding = ItemInformePorHoraBinding.bind(view);

    fun renderizado(informePorHoraEspecifico: Informe, hacerClickEnInformesPorHora: (Informe) -> Unit){

        // garantiza que noexista un  conflicto entre la actualizacion de la  vista y el check
        binding.checkBoxInformePorHora.setOnCheckedChangeListener(null)

        binding.textViewHoraInforme.text = informePorHoraEspecifico.horaCreacionInforme;
        binding.checkBoxInformePorHora.isChecked = informePorHoraEspecifico.completado;

        binding.imageButtonEliminarInformePorHora.setOnClickListener {
            mostrarCuadroDeDialogoEliminar(informePorHoraEspecifico.id)
        }

        if (informePorHoraEspecifico.completado) {
            binding.cardViewItemInformePorHora.setCardBackgroundColor(ContextCompat.getColor(itemView.context, R.color.completado))
        } else {
            binding.cardViewItemInformePorHora.setCardBackgroundColor(ContextCompat.getColor(itemView.context, R.color.primario2))
        }

        binding.root.setOnClickListener {
            hacerClickEnInformesPorHora(informePorHoraEspecifico);
        }

        binding.checkBoxInformePorHora.setOnCheckedChangeListener { _, esChequeado ->
            marcarDesmarcarInformeCompletado(informePorHoraEspecifico.id,esChequeado);
        }
    }

    private fun marcarDesmarcarInformeCompletado(idInforme: Long, esChequeado: Boolean) {

        if(esChequeado){
            binding.cardViewItemInformePorHora.setCardBackgroundColor(ContextCompat.getColor(itemView.context, R.color.completado))
        } else {
            binding.cardViewItemInformePorHora.setCardBackgroundColor(ContextCompat.getColor(itemView.context, R.color.primario2))
        }
        lifecycleOwner.lifecycleScope.launch {
            baseDeDatos.informeDao().actualizarInformeCompletado(idInforme,esChequeado);
        }
    }

    private fun mostrarCuadroDeDialogoEliminar(idInforme: Long) {
        val dialogo = Dialog(itemView.context);
        dialogo.setContentView(R.layout.dialog_eliminar);

        val cancelarEliminarInforme: Button = dialogo.findViewById(R.id.cancelarEliminar);
        val aceptarEliminarInforme: Button = dialogo.findViewById(R.id.aceptarEliminar);

        cancelarEliminarInforme.setOnClickListener {
            dialogo.hide();
        }

        aceptarEliminarInforme.setOnClickListener {
            eliminarInforme(idInforme)
            dialogo.hide();
            Toast.makeText(itemView.context, "Eliminado", Toast.LENGTH_SHORT).show();
        }

        dialogo.show();
    }

    private fun eliminarInforme(idInforme: Long) {
        if (idInforme != 0L) {
            lifecycleOwner.lifecycleScope.launch {
                baseDeDatos.informeDao().eliminarInformePorId(idInforme);
            }
        }
    }

}