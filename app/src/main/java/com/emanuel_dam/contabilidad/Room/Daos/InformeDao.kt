package com.emanuel_dam.contabilidad.Room.Daos

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.emanuel_dam.contabilidad.Room.Entities.Informe


@Dao
interface InformeDao {

    // crear
    @Insert
    suspend fun insertarNuevoInforme(informe: Informe): Long;

    // actualizar
    @Update
    suspend fun actualizarInforme(informe: Informe): Int;

    @Query("UPDATE informes SET completado = :completado WHERE id = :id")
    suspend fun actualizarInformeCompletado(id: Long, completado: Boolean)

    // eliminar
    @Query("DELETE FROM informes WHERE id = :id")
    suspend fun eliminarInformePorId(id: Long)

    // obtener
    @Query("SELECT * FROM informes")
    fun getTodosLosInformes(): LiveData<MutableList<Informe>>

    @Query("SELECT * FROM informes WHERE id = :id")
    fun getInformePorId(id: Long): LiveData<Informe>

}