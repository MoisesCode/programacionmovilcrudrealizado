package com.parcial1.parcial1

import Entity.Actividad
import Entity.Materia
import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AlertDialogLayout
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_lista_materia.*
import kotlinx.android.synthetic.main.activity_materia.*
import kotlinx.android.synthetic.main.dialog_actividad.*
import kotlinx.android.synthetic.main.dialog_actividad.view.*
import java.util.*

class MateriaActivity : AppCompatActivity() {

    var materiaLocal = Materia()
    var corte: Int = 1
    //val intent = Intent(this, MateriaActivity::class.java)
    //intent.putExtra("materia", )
    //startActivity(intent)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_materia)
        var materia: Materia = intent.getSerializableExtra("materia") as Materia
        materiaLocal = materia
        pintarMateria()
    }

    fun crearCodigo(): String {
        val date = Date()
        val id = "${date.day}${date.hours}${date.minutes}"
        return id
    }

    fun dialogo() {
        var actividad = Actividad()
        actividad.Id = crearCodigo()

        val view = View.inflate(this@MateriaActivity, R.layout.dialog_actividad, null)
        val builder = AlertDialog.Builder(this@MateriaActivity)
        builder.setView(view)

        val dialog = builder.create()
        dialog.show()

        val nombreEdit = view.findViewById<EditText>(R.id.editTextActividadNombre)
        val notaEdit = view.findViewById<EditText>(R.id.editTextActividadNota)
        val porcentajeEdit = view.findViewById<EditText>(R.id.editTextActividadPorcentaje)

        view.buttonOk.setOnClickListener {
            try {
                if(validarNombre(nombreEdit.text.toString())){
                    if(validarNota(notaEdit.text.toString().trim().toFloat())){
                        if(validarPorcentaje(porcentajeEdit.text.toString().trim().toFloat())){
                            var actividad = Actividad()
                            actividad.Nombre = nombreEdit.text.toString().trim()
                            actividad.Nota = notaEdit.text.toString().trim().toFloat()
                            actividad.Porcentaje = porcentajeEdit.text.toString().trim().toFloat()
                            actividad.Definitiva = actividad.calcularDefinitiva()

                            if(corte == 1){
                                var pPrimerCorte = 0f
                                for (item in materiaLocal.PrimerCorte){
                                    pPrimerCorte += item.Porcentaje
                                }
                                pPrimerCorte += porcentajeEdit.text.toString().trim().toFloat()
                                if(pPrimerCorte <= 100){
                                    materiaLocal.PrimerCorte.add(actividad)
                                    guardarMateria(materiaLocal)
                                    dialog.dismiss()
                                    finish()
                                }else{
                                    Toast.makeText(
                                        this@MateriaActivity,
                                        "El porcentaje acumulado de las actividades no puede ser mayor a 100", Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }

                            if(corte == 2){
                                var pSegundoCorte = 0f
                                for (item in materiaLocal.SegundoCorte){
                                    pSegundoCorte += item.Porcentaje
                                }
                                pSegundoCorte += porcentajeEdit.text.toString().trim().toFloat()
                                if(pSegundoCorte <= 100){
                                    materiaLocal.SegundoCorte.add(actividad)
                                    guardarMateria(materiaLocal)
                                    dialog.dismiss()
                                    finish()
                                }else{
                                    Toast.makeText(
                                        this@MateriaActivity,
                                        "El porcentaje acumulado de las actividades no puede ser mayor a 100", Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }

                            if(corte == 3){
                                var pTercerCorte = 0f
                                for (item in materiaLocal.TercerCorte){
                                    pTercerCorte += item.Porcentaje
                                }
                                pTercerCorte += porcentajeEdit.text.toString().trim().toFloat()
                                if(pTercerCorte <= 100){
                                    materiaLocal.TercerCorte.add(actividad)
                                    guardarMateria(materiaLocal)
                                    dialog.dismiss()
                                    finish()
                                }else{
                                    Toast.makeText(
                                        this@MateriaActivity,
                                        "El porcentaje acumulado de las actividades no puede ser mayor a 100", Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        }else{
                            Toast.makeText(
                                this@MateriaActivity,
                                "Porcentaje no valido", Toast.LENGTH_SHORT
                            ).show()
                        }
                    }else{
                        Toast.makeText(
                            this@MateriaActivity,
                            "Nota no valida", Toast.LENGTH_SHORT
                        ).show()
                    }
                }else{
                    Toast.makeText(
                        this@MateriaActivity,
                        "Nombre no valido", Toast.LENGTH_SHORT
                    ).show()
                }
            }catch (e: Exception){
                Toast.makeText(
                    this@MateriaActivity,
                    "${e.message}", Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    fun dialogo(actividad: Actividad, posicion: Int) {
        val view = View.inflate(this@MateriaActivity, R.layout.dialog_actividad, null)
        val builder = AlertDialog.Builder(this@MateriaActivity)
        builder.setView(view)

        val dialog = builder.create()
        dialog.show()

        val nombreEdit1 = view.findViewById<TextView>(R.id.editTextActividadNombre)
        val notaEdit1 = view.findViewById<TextView>(R.id.editTextActividadNota)
        val porcentajeEdit1 = view.findViewById<TextView>(R.id.editTextActividadPorcentaje)

        nombreEdit1.text = actividad.Nombre
        notaEdit1.text = actividad.Nota.toString()
        porcentajeEdit1.text = actividad.Porcentaje.toString()

        view.buttonEliminarActividad.setOnClickListener {
            if (corte == 1)
                materiaLocal.PrimerCorte.removeAt(posicion)

            if (corte == 2)
                materiaLocal.SegundoCorte.removeAt(posicion)

            if (corte == 3)
                materiaLocal.TercerCorte.removeAt(posicion)

            guardarMateria(materiaLocal)
            dialog.dismiss()
            finish()
        }

        view.buttonOk.setOnClickListener {
            val nombreEdit = view.findViewById<EditText>(R.id.editTextActividadNombre)
            val notaEdit = view.findViewById<EditText>(R.id.editTextActividadNota)
            val porcentajeEdit = view.findViewById<EditText>(R.id.editTextActividadPorcentaje)

            try {
                if(validarNombre(nombreEdit.text.toString())){
                    if(validarNota(notaEdit.text.toString().trim().toFloat())){
                        if(validarPorcentaje(porcentajeEdit.text.toString().trim().toFloat())){
                            actividad.Nombre = nombreEdit.text.toString().trim()
                            actividad.Nota = notaEdit.text.toString().trim().toFloat()
                            actividad.Porcentaje = porcentajeEdit.text.toString().trim().toFloat()
                            actividad.Definitiva = actividad.calcularDefinitiva()

                            if(corte == 1){
                                var pPrimerCorte = 0f
                                for (item in materiaLocal.PrimerCorte){
                                    if (item.Id != actividad.Id)
                                        pPrimerCorte += item.Porcentaje
                                }
                                pPrimerCorte += porcentajeEdit.text.toString().trim().toFloat()
                                if(pPrimerCorte <= 100){
                                    materiaLocal.PrimerCorte[posicion] = actividad
                                    guardarMateria(materiaLocal)
                                    dialog.dismiss()
                                    finish()
                                }else{
                                    Toast.makeText(
                                        this@MateriaActivity,
                                        "El porcentaje acumulado de las actividades no puede ser mayor a 100", Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }

                            if(corte == 2){
                                var pSegundoCorte = 0f
                                for (item in materiaLocal.SegundoCorte){
                                    if (item.Id != actividad.Id)
                                        pSegundoCorte += item.Porcentaje
                                }
                                pSegundoCorte += porcentajeEdit.text.toString().trim().toFloat()
                                if(pSegundoCorte <= 100){
                                    materiaLocal.SegundoCorte.add(actividad)
                                    guardarMateria(materiaLocal)
                                    dialog.dismiss()
                                    finish()
                                }else{
                                    Toast.makeText(
                                        this@MateriaActivity,
                                        "El porcentaje acumulado de las actividades no puede ser mayor a 100", Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }

                            if(corte == 3){
                                var pTercerCorte = 0f
                                for (item in materiaLocal.TercerCorte){
                                    if (item.Id != actividad.Id)
                                        pTercerCorte += item.Porcentaje
                                }
                                pTercerCorte += porcentajeEdit.text.toString().trim().toFloat()
                                if(pTercerCorte <= 100){
                                    materiaLocal.TercerCorte.add(actividad)
                                    guardarMateria(materiaLocal)
                                    dialog.dismiss()
                                    finish()
                                }else{
                                    Toast.makeText(
                                        this@MateriaActivity,
                                        "El porcentaje acumulado de las actividades no puede ser mayor a 100", Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        }else{
                            Toast.makeText(
                                this@MateriaActivity,
                                "Porcentaje no valido", Toast.LENGTH_SHORT
                            ).show()
                        }
                    }else{
                        Toast.makeText(
                            this@MateriaActivity,
                            "Nota no valida", Toast.LENGTH_SHORT
                        ).show()
                    }
                }else{
                    Toast.makeText(
                        this@MateriaActivity,
                        "Nombre no valido", Toast.LENGTH_SHORT
                    ).show()
                }
            }catch (e: Exception){
                Toast.makeText(
                    this@MateriaActivity,
                    "${e.message}", Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    fun agregarActividad(view: View) {
        dialogo()
    }

    fun guardarMateria(materia: Materia){
        var database = FirebaseDatabase.getInstance().reference
        database.child(materia.Id).setValue(materia)
        Toast.makeText(
            this@MateriaActivity,
            "Materia modificada", Toast.LENGTH_SHORT
        ).show()
    }

    fun consultarMateria(materia: Materia) : Materia {
        var mat = Materia()
        var database = FirebaseDatabase.getInstance().reference

        var getData = object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(
                    this@MateriaActivity,
                    "Error al recuperar lista", Toast.LENGTH_SHORT
                ).show()
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var materia = Materia()
                for(item in dataSnapshot.child("${materia.Id}").children){
                    materia.Nombre = item.child("nombre").getValue().toString()
                }
                mat = materia
            }

        }

        database.addValueEventListener(getData)
        //database.addListenerForSingleValueEvent(getData)
        return mat
    }

    fun validarNombre(nombre: String) : Boolean {
        if(nombre.length > 0 && nombre.length < 20)
            return true

        return false
    }
    fun validarNota(nota: Float) : Boolean {
        if(nota > 0 && nota <= 5.0)
            return true

        return false
    }
    fun validarPorcentaje(porcentaje: Float) : Boolean {
        if(porcentaje > 0 && porcentaje <= 100)
            return true

        return false
    }

    fun pintarMateria() {
        var editNombre = findViewById<TextView>(R.id.editTextTextNombreMateria)
        var textDefinitiva = findViewById<TextView>(R.id.textViewDefinitivaMateria1)
        editNombre.text = materiaLocal.Nombre
        textDefinitiva.text = materiaLocal.Definitiva.toString()

        listViewActividades.setOnItemClickListener { parant, view, posicion, id ->
            if (corte == 1){
                dialogo(materiaLocal.PrimerCorte[posicion], posicion)
            }

            if (corte == 2){
                dialogo(materiaLocal.SegundoCorte[posicion], posicion)
            }

            if (corte == 3){
                dialogo(materiaLocal.TercerCorte[posicion], posicion)
            }
        }
    }

    fun modificarVistaDefinitiva(){
        var textDefinitiva = findViewById<TextView>(R.id.textViewDefinitivaMateria1)
        materiaLocal.Definitiva = materiaLocal.calcularDefinitiva()
        textDefinitiva.text = materiaLocal.Definitiva.toString()
    }

    fun _buttonTercerCorte(view: View) {
        corte = 3
        if(materiaLocal.TercerCorte.size > 0){
            listViewActividades.adapter = ListaActividad(this, materiaLocal.TercerCorte)
            modificarVistaDefinitiva()
        }else{
            Toast.makeText(
                this@MateriaActivity,
                "No hay actividades", Toast.LENGTH_SHORT
            ).show()
        }
    }

    fun _buttonSegundoCorte(view: View) {
        corte = 2
        if(materiaLocal.SegundoCorte.size > 0){
            listViewActividades.adapter = ListaActividad(this, materiaLocal.SegundoCorte)
            modificarVistaDefinitiva()
        }else{
            Toast.makeText(
                this@MateriaActivity,
                "No hay actividades", Toast.LENGTH_SHORT
            ).show()
        }
    }

    fun _buttonPrimerCorte(view: View) {
        corte = 1
        if(materiaLocal.PrimerCorte.size > 0){
            listViewActividades.adapter = ListaActividad(this, materiaLocal.PrimerCorte)
            modificarVistaDefinitiva()
        }else{
            Toast.makeText(
                this@MateriaActivity,
                "No hay actividades", Toast.LENGTH_SHORT
            ).show()
        }
    }
}