package Entity

import java.io.Serializable

class Materia : Serializable {

    private val PRIMERPORCETAJE: Float = 0.3f
    private val SEGUNDOPORCETAJE: Float = 0.3f
    private val TERCERORCETAJE: Float = 0.4f

    var Id: String = ""
    var Nombre: String = ""
    var PrimerCorte = mutableListOf<Actividad>()
    var SegundoCorte = mutableListOf<Actividad>()
    var TercerCorte = mutableListOf<Actividad>()
    var Definitiva: Float = 0f

    fun calcularDefinitiva(): Float{
        var notaPrimerCorte = 0f
        var notaSegundoCorte = 0f
        var notaTercerCorte = 0f
        var definitiva = 0f

        for (item in PrimerCorte){
            notaPrimerCorte += item.Definitiva
        }
        for (item in SegundoCorte){
            notaSegundoCorte += item.Definitiva
        }
        for (item in TercerCorte){
            notaTercerCorte += item.Definitiva
        }

        definitiva = (notaPrimerCorte * PRIMERPORCETAJE) + (notaSegundoCorte * SEGUNDOPORCETAJE) + (notaTercerCorte * TERCERORCETAJE)
        return definitiva
    }
}