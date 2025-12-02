package org.christophertwo.cotizador.core.util

import kotlin.math.pow
import kotlin.math.round

/**
 * Formatea un número Double a una cadena con el número especificado de decimales
 */
fun Double.formatDecimals(decimals: Int = 2): String {
    val multiplier = 10.0.pow(decimals)
    val rounded = round(this * multiplier) / multiplier

    // Convertir a string y agregar ceros si es necesario
    val str = rounded.toString()
    val parts = str.split(".")

    return if (parts.size == 1) {
        // No tiene decimales
        "$str.${"0".repeat(decimals)}"
    } else {
        val decimalPart = parts[1]
        if (decimalPart.length < decimals) {
            // Agregar ceros
            "$str${"0".repeat(decimals - decimalPart.length)}"
        } else if (decimalPart.length > decimals) {
            // Truncar
            "${parts[0]}.${decimalPart.substring(0, decimals)}"
        } else {
            str
        }
    }
}

/**
 * Formatea un Float a una cadena con el número especificado de decimales
 */
fun Float.formatDecimals(decimals: Int = 1): String {
    return this.toDouble().formatDecimals(decimals)
}

