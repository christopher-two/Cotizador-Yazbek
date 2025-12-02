package org.christophertwo.cotizador.data

import cotizador.composeapp.generated.resources.Res
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

/**
 * Servicio para manejar la lógica de la lista de precios de Yazbek.
 * Diseñado para Kotlin Multiplatform (KMP).
 */
class YazbekService {

    // Configuración del parser JSON para ser robusto
    private val jsonParser = Json {
        ignoreUnknownKeys = true // Ignora campos extra si el JSON cambia en el futuro
        isLenient = true         // Permite JSONs un poco más relajados
        coerceInputValues = true // Usa valores por defecto si el campo es nulo
        encodeDefaults = true
    }

    /**
     * Parsea el contenido crudo (String) del JSON a objetos Kotlin tipados.
     * Ideal para llamar después de leer el archivo de Resources.
     *
     * @param jsonContent El contenido del archivo .json como String.
     * @return El objeto [YazbekPriceList] con toda la data estructurada.
     */
    fun parsePriceList(jsonContent: String): YazbekPriceList {
        return jsonParser.decodeFromString(jsonContent)
    }

    suspend fun loadFromResources(): YazbekPriceList {
        // Asumiendo que tienes la dependencia de compose-resources y el archivo en commonMain/composeResources/files/
        // import org.jetbrains.compose.resources.Res
        // import yazbek_app.generated.resources.Res
        
        val bytes = Res.readBytes("files/Lista_Precios_Yazbek_Oct2025.json")
        val stringContent = bytes.decodeToString()
        return parsePriceList(stringContent)
    }
}

// -----------------------------------------------------------------------------
// MODELOS DE DATOS (Data Classes)
// -----------------------------------------------------------------------------

@Serializable
data class YazbekPriceList(
    @SerialName("metadata")
    val metadata: Metadata,
    @SerialName("catalogo")
    val catalogo: List<Category>
)

@Serializable
data class Metadata(
    @SerialName("marca")
    val marca: String,
    @SerialName("fecha_lista")
    val fechaLista: String,
    @SerialName("moneda")
    val moneda: String,
    @SerialName("impuestos")
    val impuestos: String,
    @SerialName("condiciones")
    val condiciones: Condiciones,
    @SerialName("nota")
    val nota: String
)

@Serializable
data class Condiciones(
    @SerialName("menudeo")
    val menudeo: String,
    @SerialName("mayoreo")
    val mayoreo: String
)

@Serializable
data class Category(
    @SerialName("categoria")
    val categoria: String,
    @SerialName("productos")
    val productos: List<Product>
)

@Serializable
data class Product(
    @SerialName("codigo")
    val codigo: String,
    @SerialName("descripcion")
    val descripcion: String,
    @SerialName("precios")
    val precios: List<PriceVariant>
)

@Serializable
data class PriceVariant(
    @SerialName("color")
    val color: String,
    @SerialName("talla")
    val talla: String,
    @SerialName("precio_mayoreo")
    val precioMayoreo: Double,
    @SerialName("precio_menudeo")
    val precioMenudeo: Double
)