package org.christophertwo.cotizador

class JsPlatform : Platform {
    override val name: String = "WebJS"
}

actual fun getPlatform(): Platform = JsPlatform()