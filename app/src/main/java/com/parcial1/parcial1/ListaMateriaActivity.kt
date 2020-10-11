package com.parcial1.parcial1

import Entity.Actividad
import Entity.Materia
import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_lista_materia.*
import kotlinx.android.synthetic.main.activity_materia.*
import kotlinx.android.synthetic.main.dialog_actividad.view.*
import kotlinx.android.synthetic.main.dialog_materia.view.*
import java.util.*

class ListaMateriaActivity : AppCompatActivity() {
    var materias = mutableListOf<Materia>()
    var database = FirebaseDatabase.getInstance().reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista_materia)
        consultarMaterias()
    }

    fun CrearMateria(view: View) {
        val view = View.inflate(this@ListaMateriaActivity, R.layout.dialog_materia, null)
        val builder = AlertDialog.Builder(this@ListaMateriaActivity)
        builder.setView(view)

        val dialog = builder.create()
        dialog.show()

        val nombre = view.findViewById<EditText>(R.id.editTextMateriaNombre1)
        var materia = Materia()

        view.buttonOkMateria.setOnClickListener {
            if (nombre.text.length > 0 && nombre.text.length < 20){

                materia.Nombre = nombre.text.toString().trim()
                materia.Id = crearCodigo()

                var database = FirebaseDatabase.getInstance().reference
                database.child(materia.Id).setValue(materia)

                Toast.makeText(
                    this@ListaMateriaActivity,
                    "Materia registrada", Toast.LENGTH_SHORT
                ).show()

                dialog.dismiss()
            }
            else{
                Toast.makeText(
                    this@ListaMateriaActivity,
                    "Nombre no valido", Toast.LENGTH_SHORT
                ).show()
            }
        }
        //var intent = Intent(this@ListaMateriaActivity,MateriaActivity::class.java)
        //startActivity(intent)
    }

    fun consultarMaterias(){
        var getData = object : ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(
                    this@ListaMateriaActivity,
                    "Error al recuperar lista", Toast.LENGTH_SHORT
                ).show()
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                materias.clear()
                for(item in dataSnapshot.children){
                    var materia = Materia()
                    materia.Id = item.child("id").getValue().toString()
                    materia.Nombre = item.child("nombre").getValue().toString()
                    materia.Definitiva = item.child("definitiva").getValue().toString().toFloat()

                    for (act in item.child("primerCorte").children){
                        var actividad = Actividad()
                        actividad.Nombre = act.child("nombre").getValue().toString()
                        actividad.Porcentaje = act.child("porcentaje").getValue().toString().toFloat()
                        actividad.Nota = act.child("nota").getValue().toString().toFloat()
                        actividad.Definitiva = act.child("definitiva").getValue().toString().toFloat()
                        materia.PrimerCorte.add(actividad)
                    }
                    for (act in item.child("segundoCorte").children){
                        var actividad = Actividad()
                        actividad.Nombre = act.child("nombre").getValue().toString()
                        actividad.Porcentaje = act.child("porcentaje").getValue().toString().toFloat()
                        actividad.Nota = act.child("nota").getValue().toString().toFloat()
                        actividad.Definitiva = act.child("definitiva").getValue().toString().toFloat()
                        materia.SegundoCorte.add(actividad)
                    }
                    for (act in item.child("tercerCorte").children){
                        var actividad = Actividad()
                        actividad.Nombre = act.child("nombre").getValue().toString()
                        actividad.Porcentaje = act.child("porcentaje").getValue().toString().toFloat()
                        actividad.Nota = act.child("nota").getValue().toString().toFloat()
                        actividad.Definitiva = act.child("definitiva").getValue().toString().toFloat()
                        materia.TercerCorte.add(actividad)
                    }
                    materia.Definitiva = materia.calcularDefinitiva()
                    materias.add(materia)
                }

                pintarMaterias(materias)
            }
        }
        database.addValueEventListener(getData)
        //database.addListenerForSingleValueEvent(getData)
    }

    fun pintarMaterias(lista: MutableList<Materia>) {
        var materia = Materia()
        listViewMaterias.adapter = ListaMateria(this, lista)

        listViewMaterias.setOnItemClickListener { parant, view, posicion, id ->
            materia = lista[posicion]
            val intent = Intent(this, MateriaActivity::class.java)
            intent.putExtra("materia", materia)
            startActivity(intent)
        }
    }

    fun crearCodigo(): String {
        val date = Date()
        val id = "${date.day}${date.hours}${date.minutes}"
        return id
    }
}