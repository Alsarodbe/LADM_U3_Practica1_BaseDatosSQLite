package mx.tecnm.tepic.ladm_u3_practica1_basedatossqlite

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.activity_detalles.*

class DetallesActivity : AppCompatActivity() {
    var listaID = ArrayList<String>()
    var id = ""
    var foto:ByteArray? = ByteArray(0)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalles)

        var extras = intent.extras
        id = extras!!.getString("id").toString()

        cargaInformacion()

        volverDet.setOnClickListener {
            finish()
        }
    }

    private fun cargaInformacion(){
        try {
            var c = ObjetoActividad("","","")
            c.setContexto(this)
            var datos = c.recuperarID(id)

            textView1.setText("Descripción: ${datos.desc}")
            textView2.setText("Capturado el: ${datos.fcap}")
            textView3.setText("Entregado el: ${datos.fent}")

            var e = ObjetoEvidencia("",ByteArray(0))
            e.setContexto(this)
            var data = e.recuperarID(id)

            var tamaño = data.size-1
            var v = Array<String>(data.size,{""})

            listaID = ArrayList<String>()
            (0..tamaño).forEach {
                var evidencia = data[it]
                var item = "ID_EVIDENCIA: "+evidencia.id.toString()+"\n"+"ID_ACTIVIDAD: "+evidencia.id_act
                v[it] = item
                listaID.add(evidencia.id.toString())
            }

            listaFotos.adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,v)

            listaFotos.setOnItemClickListener { parent, view, position, id ->
                var img = ObjetoEvidencia("",ByteArray(0))
                img.setContexto(this)
                foto = img.recuperarFoto(listaID.get(position))

                fotoEvidencia.setImageBitmap(convierteFoto(foto))
            }

        }catch (e: Exception){
            mensaje(e.message.toString())
        }
    }

    private fun convierteFoto(imagen: ByteArray?): Bitmap? {
        return BitmapFactory.decodeByteArray(imagen, 0, imagen!!.size)
    }

    private fun mensaje(s: String) {
        AlertDialog.Builder(this).setTitle("Atención").setMessage(s)
            .setPositiveButton("Entendido"){
                    d,i-> d.dismiss()
            }
            .show()
    }
}