package Entity

import java.io.Serializable

class Actividad : Serializable  {
    var Id: String = ""
    var Nombre: String = ""
    var Nota: Float = 0f
    var Porcentaje: Float = 0f
    var Definitiva: Float = 0f

    fun calcularDefinitiva(): Float {
        var definitva = 0f
        definitva = Nota * calcularPorcentaje()
        return definitva
    }

    private fun calcularPorcentaje(): Float {
        return Porcentaje / 100
    }
}