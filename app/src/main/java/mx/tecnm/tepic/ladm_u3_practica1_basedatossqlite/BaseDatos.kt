package mx.tecnm.tepic.ladm_u3_practica1_basedatossqlite

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class BaseDatos(
    context: Context?,
    name: String,
    factory: SQLiteDatabase.CursorFactory?,
    version: Int)
    : SQLiteOpenHelper(context, name, factory, version) {
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("create table actividades(id_actividad integer primary key autoincrement, descripcion varchar(2000),fechacaptura date, fechaentrega date);")
        db?.execSQL("create table evidencias(id_evidencia integer primary key autoincrement,id_actividad integer,foto blob, foreign key(id_actividad) references actividades(id_actividad));")
    }
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
    }
}