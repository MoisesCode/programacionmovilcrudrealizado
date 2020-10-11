package com.parcial1.parcial1

import Entity.Actividad
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.layout_actividad1.view.*

class ListaActividad(private val myContext: Context, private val listaActividad: List<Actividad>)
    : ArrayAdapter<Actividad>(myContext,0, listaActividad) {

    override fun getView(posicion: Int, converterView: View?, parent: ViewGroup): View {
        val layout = LayoutInflater.from(myContext).inflate(R.layout.layout_actividad1, parent, false)

        val actividad = listaActividad[posicion]
        layout.textViewNombreActividad.text = actividad.Nombre
        layout.textViewActividadDefinitiva.text = actividad.Definitiva.toString()

        return layout
    }
}