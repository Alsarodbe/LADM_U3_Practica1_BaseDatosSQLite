package mx.tecnm.tepic.ladm_u3_practica1_basedatossqlite

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.activity_buscar.*

class BuscarActivity : AppCompatActivity() {
    var listaID = ArrayList<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_buscar)
        buscarA2.setOnClickListener {
            cargaInformacion()
        }

        lista.setOnItemClickListener{ adapterView, view, i, l ->
            mostrarAlertEliminarActualizar(i)
        }

        volverA2.setOnClickListener {
            finish()
        }
    }

    private fun mostrarAlertEliminarActualizar(posicion: Int) {
        var idLista = listaID.get(posicion)

        AlertDialog.Builder(this)
            .setTitle("Menú del registro ${posicion}")
            .setMessage("¿Que desea hacer?:")
            .setPositiveButton("Eliminar") {d,i-> eliminar(idLista)}
            .setNeutralButton("Cancelar") {d,i->}
            .setNegativeButton("Mas detalles") {d,i-> llamarVentanaDetalles(idLista)}
            .show()
    }

    private fun eliminar(id:String) {
        var c = ObjetoActividad("","","")
        c.setContexto(this)

        if(c.eliminar(id)) {
            mensaje("Se ha eliminado correctamente")

            var d = ObjetoEvidencia("",ByteArray(0))
            d.setContexto(this)
            d.deleteID(id)

            fcapA2.setText("")
            var v = Array<String>(0,{""})
            lista.adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,v)

        } else {
            mensaje("Error, se avisa en linea 56 de BuscarActivity.kt")
        }
    }

    private fun mensaje(s: String) {
        AlertDialog.Builder(this).setTitle("Aviso").setMessage(s)
            .setPositiveButton("Entendido"){
                    d,i-> d.dismiss()
            }
            .show()
    }

    private fun llamarVentanaDetalles(idLista: String) {
        var ventana = Intent(this,DetallesActivity::class.java)
        var c = ObjetoActividad("","","")
        c.setContexto(this)

        ventana.putExtra("id",idLista)

        startActivity(ventana)
    }

    private fun cargaInformacion(){
        try {
            var c = ObjetoActividad("","","")
            c.setContexto(this)
            var datos = c.recuperarFecha(fcapA2.text.toString())
            var actividades = ArrayList<String>()
            listaID = ArrayList<String>()
            for (i in datos){
                actividades.add("Descripción: ${i.desc}\nCapturado el: ${i.fcap}\nEntregado el: ${i.fent}")
                listaID.add(i.id.toString())
            }
            lista.adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,actividades)
            lista.setOnItemClickListener{ adapterView, view, i, l ->
                mostrarAlertEliminarActualizar(i)
            }
        }catch (e: Exception){
            mensaje(e.message.toString())
        }
    }
}