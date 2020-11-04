package com.gediminaszukas.polyassistant.entities.shape

import com.gediminaszukas.polyassistant.Polymorphic
import com.gediminaszukas.polyassistant.Subtype

@Polymorphic
sealed class Shape {

    abstract val area: Double

    @Subtype
    data class Rectangle(override val area: Double) : Shape()

    @Subtype
    data class Circle(override val area: Double) : Shape()
}
