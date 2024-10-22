package com.emanuel_dam.contabilidad.Room.Entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Entity( tableName = "informes" )
data class Informe(
    @PrimaryKey( autoGenerate = true ) var id:Long = 0,
    @ColumnInfo(name = "fecha_creacion") val fechaInforme: String = obtenerFechaActual(),
    @ColumnInfo( name = "nombre_usuario" ) val nombreUsuario: String,
    @ColumnInfo( name = "numero_movil_usuario" ) val movilUsuario: Int,
    @ColumnInfo( name = "monto_total" ) val montoTotal: Double,
    @ColumnInfo( name = "codigo_confirmacion_sms" ) val codigoSMS: String,
    @ColumnInfo(name = "hora_creacion") val horaCreacionInforme: String = obtenerHoraActual(),
    @ColumnInfo( name = "productos_y_cantidad" ) val productosYSuCantidad: String,
    @ColumnInfo(name = "completado") var completado: Boolean = false,
    @ColumnInfo(name = "editado") val editado: Boolean = false,

    ){

    companion object {
        // Método para obtener la fecha actual en formato "yyyy-MM-dd"
        fun obtenerFechaActual(): String {
            val sdf = SimpleDateFormat("dd MM yyyy", Locale.getDefault())
            return sdf.format(Date())
        }

        // Método para obtener la hora actual en formato "HH:mm:ss"
        fun obtenerHoraActual(): String {
            val sdf = SimpleDateFormat("hh:mm:ss a", Locale.getDefault())
            return sdf.format(Date())
        }
    }

}