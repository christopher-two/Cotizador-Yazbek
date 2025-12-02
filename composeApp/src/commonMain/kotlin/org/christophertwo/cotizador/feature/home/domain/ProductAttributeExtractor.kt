package org.christophertwo.cotizador.feature.home.domain

data class ProductAttribute(
    val key: String,
    val value: String
)

object ProductAttributeExtractor {

    fun extractAttributes(description: String): List<ProductAttribute> {
        val attributes = mutableListOf<ProductAttribute>()
        val lowerDesc = description.lowercase()

        // Tipo de cuello
        when {
            lowerDesc.contains("cuello redondo") -> attributes.add(ProductAttribute("cuello", "Redondo"))
            lowerDesc.contains("cuello v") -> attributes.add(ProductAttribute("cuello", "V"))
        }

        // Tipo de manga
        when {
            lowerDesc.contains("manga corta") -> attributes.add(ProductAttribute("manga", "Corta"))
            lowerDesc.contains("manga larga") -> attributes.add(ProductAttribute("manga", "Larga"))
            lowerDesc.contains("sin mangas") -> attributes.add(ProductAttribute("manga", "Sin Mangas"))
        }

        // Género
        when {
            lowerDesc.contains("caballero") -> attributes.add(ProductAttribute("genero", "Caballero"))
            lowerDesc.contains("dama") -> attributes.add(ProductAttribute("genero", "Dama"))
            lowerDesc.contains("jóvenes") -> attributes.add(ProductAttribute("genero", "Jóvenes"))
            lowerDesc.contains("niños") -> attributes.add(ProductAttribute("genero", "Niños"))
            lowerDesc.contains("bebés") -> attributes.add(ProductAttribute("genero", "Bebés"))
        }

        // Características especiales
        if (lowerDesc.contains("peso completo")) {
            attributes.add(ProductAttribute("caracteristica", "Peso Completo"))
        }
        if (lowerDesc.contains("silueta")) {
            attributes.add(ProductAttribute("caracteristica", "Con Silueta"))
        }
        if (lowerDesc.contains("jaspe") || lowerDesc.contains("neón")) {
            attributes.add(ProductAttribute("caracteristica", "Jaspe/Neón"))
        }
        if (lowerDesc.contains("sublitee")) {
            attributes.add(ProductAttribute("caracteristica", "SubliTee"))
        }

        return attributes
    }

    fun getAvailableValues(products: List<org.christophertwo.cotizador.data.Product>, key: String): List<String> {
        return products
            .flatMap { extractAttributes(it.descripcion) }
            .filter { it.key == key }
            .map { it.value }
            .distinct()
            .sorted()
    }
}

