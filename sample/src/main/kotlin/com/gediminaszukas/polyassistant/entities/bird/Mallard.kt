package com.gediminaszukas.polyassistant.entities.bird

import com.gediminaszukas.polyassistant.Subtype

@Subtype
class Mallard : FlyingBird {

    override fun fly() {
        println("Mallard flying!")
    }
}