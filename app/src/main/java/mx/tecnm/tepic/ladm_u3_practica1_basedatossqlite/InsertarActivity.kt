package mx.tecnm.tepic.ladm_u3_practica1_basedatossqlite

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.activity_insertar.*
import java.io.ByteArrayOutputStream

class InsertarActivity : AppCompatActivity() {
    val permisogaleria = 1
    val permisocamara = 2
    var toma: Uri? = null
    var id = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_insertar)
        var extras = intent.extras
        id = extras!!.getString("id").toString()
        galeria.setOnClickListener(){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                    val permisoArchivos = arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                    requestPermissions(permisoArchivos,permisogaleria)
                } else {
                    abreGaleria()
                }
            } else {
                abreGaleria()
            }
        }

        insertarEvi.setOnClickListener {
            val imagen = (evidencia.drawable as BitmapDrawable).bitmap
            var imagenBytes = convierteBytes(imagen)
            var evi = ObjetoEvidencia(
                id,
                imagenBytes
            )
            evi.setContexto(this)
            var res = evi.insertar()
            if(res == true) {
                mensaje("Se ha agregado la evidencia")
            } else {
                mensaje("Error, no se pude agregar")
            }
        }
        volverIns.setOnClickListener {
            finish()
        }
    }
    private fun mensaje(s: String) {
        AlertDialog.Builder(this).setTitle("Atención").setMessage(s)
            .setPositiveButton("Entendido"){
                    d,i-> d.dismiss()
            }
            .show()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode) {
            permisogaleria -> {
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    abreGaleria()
                else
                    Toast.makeText(this,"No se puede acceder al almacenamiento, por favor acepte los permisos", Toast.LENGTH_LONG)
                        .show()
            }
        }
    }

    private fun abreGaleria() {
        val galeriaIntent = Intent(Intent.ACTION_PICK)
        galeriaIntent.type = "image/*"
        startActivityForResult(galeriaIntent,permisogaleria)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK && requestCode == permisogaleria) {
            evidencia.setImageURI(data?.data)
        }
        if(resultCode == Activity.RESULT_OK && requestCode == permisocamara) {
            evidencia.setImageURI(toma)
        }
    }

    private fun convierteBytes(bitmap : Bitmap):ByteArray{
        var stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG,5,stream)

        return stream.toByteArray()
    }
}