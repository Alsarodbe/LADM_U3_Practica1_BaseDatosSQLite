package mx.tecnm.tepic.ladm_u3_practica1_basedatossqlite

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteException

class ObjetoActividad(descrip:String, captura:String, entrega: String) {
    var id = 0
    var desc = descrip
    var fcap = captura
    var fent = entrega
    var contex : Context?= null

    fun setContexto(p: Context) {
        contex = p
    }
    fun insertar():Boolean {
        try {
            var base = BaseDatos(contex!!, "Tareas", null, 1)
            var insertar = base.writableDatabase
            var datos = ContentValues()
            datos.put("descripcion", desc)
            datos.put("fechacaptura", fcap)
            datos.put("fechaentrega", fent)
            var res = insertar.insert("actividades", "id_actividad", datos)
            if (res.toInt() == -1) {
                return false
            }
        }catch (e: SQLiteException) {
            return false
        }
        return true
    }

    fun recuperarTodo(): ArrayList<ObjetoActividad> {
        var cursorlleno = ArrayList<ObjetoActividad>()
        try{
            var bd = BaseDatos(contex!!,"Tareas",null,1 )
            var select = bd.readableDatabase
            var cursor  = select.query("actividades", arrayOf("*"), null, null, null, null, null)
            if(cursor.moveToFirst()){
                do{
                    var temp = ObjetoActividad(cursor.getString(1),cursor.getString(2),cursor.getString(3))
                    temp.id = cursor.getInt(0)
                    cursorlleno.add(temp)
                }while (cursor.moveToNext())
            }
        }catch (e: SQLiteException){  }
        return cursorlleno
    }

    fun recuperarFecha(f:String): ArrayList<ObjetoActividad> {
        var cursorllenoEntrega = ArrayList<ObjetoActividad>()
        try{
            var bd = BaseDatos(contex!!,"Tareas",null,1 )
            var select = bd.readableDatabase
            var cursor  = select.query("actividades", arrayOf("*"), "fechaentrega = ?", arrayOf(f), null, null, null)
            if(cursor.moveToFirst()){
                do{
                    var temp = ObjetoActividad(cursor.getString(1),cursor.getString(2),cursor.getString(3))
                    temp.id = cursor.getInt(0)
                    cursorllenoEntrega.add(temp)
                }while (cursor.moveToNext())
            }
        }catch (e: SQLiteException){ }
        return cursorllenoEntrega
    }

    fun recuperarID(id:String): ObjetoActividad{
        var cursorllenoID = ObjetoActividad("","","")
        try {
            var bd = BaseDatos(contex!!, "Tareas", null, 1)
            var select = bd.readableDatabase
            var res = select.query("actividades", arrayOf("*"), "id_actividad = ?",arrayOf(id), null, null, null)
            if(res.moveToFirst()){
                cursorllenoID.id = id.toInt()
                cursorllenoID.desc = res.getString(1)
                cursorllenoID.fcap = res.getString(2)
                cursorllenoID.fent = res.getString(3)
            }
        }catch (e: SQLiteException){
            e.message.toString()
        }
        return cursorllenoID
    }

    fun eliminar(id:String):Boolean{
        try{
            var base = BaseDatos(contex!!, "Tareas",null,1)
            var eliminar = base.writableDatabase
            var res = eliminar.delete("actividades","id_actividad = ?",arrayOf(id))
            if(res == 0){
                return false
            }
        }catch (e: SQLiteException){
            return false
        }
        return true
    }

    fun actualizar():Boolean{
        try{
            var base = BaseDatos(contex!!, "Tareas",null,1)
            var actualizar = base.writableDatabase
            var datos = ContentValues()
            datos.put("descripcion", desc)
            datos.put("fechacaptura", fcap)
            datos.put("fechaentrega", fent)
            var res = actualizar.update("actividades",datos,"id_actividad = ?", arrayOf(id.toString()))
            if(res == 0){
                return false
            }
        }catch (e: SQLiteException){
            return false
        }
        return true
    }
}