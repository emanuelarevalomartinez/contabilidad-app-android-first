package com.emanuel_dam.contabilidad.Room.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.emanuel_dam.contabilidad.Room.Daos.InformeDao
import com.emanuel_dam.contabilidad.Room.Entities.Informe

@Database( entities = [Informe::class], version = 1 )
abstract class Ventas: RoomDatabase() {

    abstract fun informeDao(): InformeDao;

    companion object {
        fun getDatabaseVentas(ctx: Context): Ventas {
            val db = Room.databaseBuilder(ctx, Ventas::class.java, "ventas").build();
            return db;


        }
    }

}