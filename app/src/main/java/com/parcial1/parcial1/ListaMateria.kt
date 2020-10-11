package com.parcial1.parcial1

import Entity.Materia
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.layout_materia1.view.*

class ListaMateria(private val myContext: Context, private val listaMaterias: List<Materia>)
    : ArrayAdapter<Materia>(myContext,0, listaMaterias){

    override fun getView(posicion: Int, converterView: View?, parent: ViewGroup): View {
        val layout = LayoutInflater.from(myContext).inflate(R.layout.layout_materia1, parent, false)

        val materia = listaMaterias[posicion]
        layout.textViewNombreMateria.text = materia.Nombre
        layout.textViewDefinitiva.text = materia.Definitiva.toString()

        return layout
    }
}