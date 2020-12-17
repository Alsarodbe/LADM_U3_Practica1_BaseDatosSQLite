package mx.tecnm.tepic.ladm_u3_practica1_basedatossqlite

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteException

class ObjetoEvidencia(i:String, f:ByteArray?) {
    var id = 0
    var id_act = i
    var foto = f
    var contex : Context?= null

    fun setContexto(p:Context) {
        contex = p
    }

    fun insertar():Boolean {
        try {
            var base = BaseDatos(contex!!, "Tareas", null, 1)
            var insertar = base.writableDatabase
            var datos = ContentValues()
            datos.put("id_actividad", id_act.toInt())
            datos.put("foto", foto)
            var res = insertar.insert("EVIDENCIAS", "ID_EVIDENCIA",datos)
            if (res == -1L) {
                return false
            }
        }catch (e: SQLiteException) {
            return false
        }
        return true
    }

    fun recuperarID(id:String):ArrayList<ObjetoEvidencia>{
        var data = ArrayList<ObjetoEvidencia>()
        try{
            var bd = BaseDatos(contex!!,"Tareas",null,1 )
            var select = bd.readableDatabase
            var columnas = arrayOf("ID_EVIDENCIA,ID_ACTIVIDAD")
            var actividad = arrayOf(id)

            var cursor  = select.query("EVIDENCIAS", columnas, "ID_ACTIVIDAD = ?", actividad, null, null, null)
            if(cursor.moveToFirst()){
                do{
                    var temp = ObjetoEvidencia(cursor.getString(1), ByteArray(0))
                    temp.id = cursor.getInt(0)
                    data.add(temp)
                }while (cursor.moveToNext())
            }else{
            }
        }catch (e:SQLiteException){
        }
        return data
    }

    fun recuperarFoto(id:String): ByteArray?{
        var registro = ObjetoEvidencia("",ByteArray(0))

        try {
            var bd = BaseDatos(contex!!, "Tareas", null, 1)
            var select = bd.readableDatabase
            var busca = arrayOf("FOTO")
            var buscaID = arrayOf(id)

            var res = select.query("EVIDENCIAS", busca, "ID_EVIDENCIA = ?",buscaID, null, null, null)
            if(res.moveToFirst()){
                registro.foto = res.getBlob(0)
            }
        }catch (e:SQLiteException){
            e.message.toString()
        }
        return registro.foto
    }


    fun deleteID(id:String):Boolean{
        try{
            var base = BaseDatos(contex!!, "Tareas",null,1)
            var eliminar = base.writableDatabase
            var eliminarID = arrayOf(id)

            var res = eliminar.delete("EVIDENCIAS","ID_ACTIVIDAD = ?",eliminarID)
            if(res == 0){
                return false
            }
        }catch (e:SQLiteException){
            return false
        }
        return true
    }

}