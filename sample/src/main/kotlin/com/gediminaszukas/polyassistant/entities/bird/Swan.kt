package com.gediminaszukas.polyassistant.entities.bird

import com.gediminaszukas.polyassistant.Subtype

@Subtype
class Swan : FlyingBird {

    override fun fly() {
        println("Swan flying!")
    }
}