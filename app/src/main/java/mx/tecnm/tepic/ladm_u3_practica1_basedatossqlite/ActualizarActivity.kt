package mx.tecnm.tepic.ladm_u3_practica1_basedatossqlite

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.activity_actualizar.*

class ActualizarActivity : AppCompatActivity() {
    var id = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_actualizar)
        val extras = intent.extras
        descripcionAct.setText(extras!!.getString("descripcion"))
        fcapturaAct.setText(extras!!.getString("captura"))
        fentregaAct.setText(extras!!.getString("entrega"))
        id = extras.getString("id").toString()

        actualizar.setOnClickListener {
            var oa = ObjetoActividad(descripcionAct.text.toString(), fcapturaAct.text.toString(), fentregaAct.text.toString())
            oa.id = id.toInt()
            oa.setContexto(this)

            if(oa.actualizar()) {
                Toast.makeText(this,"Actualizado con exito", Toast.LENGTH_LONG)
                    .show()
                finish()
            } else {
                AlertDialog.Builder(this)
                    .setTitle("Error")
                    .setMessage("Error en ActualizarActivity.kt activado en linea 36")
                    .setPositiveButton("¡A programar se ha dicho!"){d,i->}
                    .show()
            }
            finish()
        }
        regresar.setOnClickListener {
            finish();
        }
    }
}