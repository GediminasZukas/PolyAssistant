package com.gediminaszukas.polyassistant

import GeneratedRuntimeTypeAdapterFactories
import com.gediminaszukas.polyassistant.entities.bird.FlyingBird
import com.gediminaszukas.polyassistant.entities.bird.Swan
import com.gediminaszukas.polyassistant.entities.shape.Shape
import com.gediminaszukas.polyassistant.entities.shape.ShapeHolder
import com.google.gson.Gson
import com.google.gson.GsonBuilder

fun main() {
    val gsonInstance = buildGsonInstance()

    val shapeHolder = ShapeHolder(Shape.Circle(area = 3.8))
    val shapeJson = gsonInstance.toJson(shapeHolder)
    val deserializedShapeHolder = gsonInstance.fromJson(shapeJson, ShapeHolder::class.java)
    println("Shape area is: ${deserializedShapeHolder.shape.area}")

    val bird = Swan()
    val birdJson = gsonInstance.toJson(bird)
    val deserializedBird = gsonInstance.fromJson(birdJson, FlyingBird::class.java)
    println("Flight status:")
    deserializedBird.fly()
}

fun buildGsonInstance(): Gson {
    return GsonBuilder().apply {
        GeneratedRuntimeTypeAdapterFactories.registerIn(this) // commenting out will cause failure
    }.create()
}
