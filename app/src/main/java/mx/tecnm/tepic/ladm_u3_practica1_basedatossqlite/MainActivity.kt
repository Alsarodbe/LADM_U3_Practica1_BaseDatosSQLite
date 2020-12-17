//Subido a https://github.com/MrWildFish/LADM_U3_Practica1_BaseDatosSQLite
package mx.tecnm.tepic.ladm_u3_practica1_basedatossqlite

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    var listaID = ArrayList<String>()
    var id = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        cargarBD();
        btnInsertar.setOnClickListener {
            if (etxtDescripcion.text.toString().equals("")){
                mensaje("No se permiten campos sin ingresar, ingrese la descripción");
            }else if(etxtCaptura.text.toString().equals("")){
                mensaje("No se permiten campos sin ingresar, ingrese la fecha de captura");
            }else if(etxtEntrega.text.toString().equals("")) {
                mensaje("No se permiten campos sin ingresar, ingrese la fecha de entrega");
            }
            else{
                var objAct = ObjetoActividad(
                        etxtDescripcion.text.toString(),
                        etxtCaptura.text.toString(),
                        etxtEntrega.text.toString()
                )
                objAct.setContexto(this)
                if(objAct.insertar()) {
                    mensaje("Se insertó con fecha de entrega: ${etxtEntrega.text}")
                    etxtDescripcion.setText("")
                    etxtCaptura.setText("")
                    etxtEntrega.setText("")
                    cargarBD()
                } else {
                    mensaje("No se pudo completar la operación :(")
                }
            }
            cargarBD()
        }
        btnBuscar.setOnClickListener {
            startActivityForResult(Intent(this,BuscarActivity::class.java),0)
        }
    }
    private fun mensaje(s: String) {
        AlertDialog.Builder(this).setTitle("ATENCIÓN").setMessage(s)
                .setPositiveButton("Entendido"){
                        d, _ -> d.dismiss()
                }
                .show()
    }
    private fun cargarBD(){//Carga los datos desde base de datos al ListView donde se muestran
        try {
            var c = ObjetoActividad("","","")
            c.setContexto(this)
            var actividades = ArrayList<String>();
            listaID = ArrayList<String>()
            for (i in c.recuperarTodo()){
                actividades.add("Descripción: "+i.desc+"\n"+"capturado el: "+i.fcap+"\n"+"entregado el: "+i.fent);
                listaID.add(i.id.toString());
            }
            listaActividades.adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,actividades)
            listaActividades.setOnItemClickListener{ adapterView, view, i, l ->
                menu(i);
            }
        }catch (e: Exception){
            mensaje(e.message.toString())
        }
    }

    private fun menu(posicion: Int) {
        var idLista = listaID.get(posicion)
        AlertDialog.Builder(this)
                .setTitle("Menú del registro ${posicion}")
                .setMessage("¿Que desea hacer?:")
                .setPositiveButton("Insertar una nueva evidencia") {d,i-> loadInsertarActivity(idLista)}
                .setNeutralButton("Cancelar") {d,i-> }
                .setNegativeButton("Actualizar información") {d,i-> loadActulizarActivity(idLista)}
                .show()
    }

    private fun loadInsertarActivity(idLista: String) {
        var launchactivity = Intent(this,InsertarActivity::class.java)
        launchactivity.putExtra("id",idLista)
        startActivityForResult(launchactivity,0)
    }

    private fun loadActulizarActivity(idLista: String) {
        var launchactivity = Intent(this,ActualizarActivity::class.java)
        var c = ObjetoActividad("","","")
        c.setContexto(this)//Recuperar datos del contexto
        launchactivity.putExtra("id",idLista)//Cuando se carga la info, idlista se llena con los ID's de los registros de la base de datos
        launchactivity.putExtra("descripcion",c.recuperarID(idLista).desc)//Recupera el registro con dicho ID, pero solo su descripcion
        launchactivity.putExtra("captura",c.recuperarID(idLista).fcap)
        launchactivity.putExtra("entrega",c.recuperarID(idLista).fent)
        startActivityForResult(launchactivity,0)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        cargarBD();
    }
}