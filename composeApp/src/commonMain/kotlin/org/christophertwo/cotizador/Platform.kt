package org.christophertwo.cotizador

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform