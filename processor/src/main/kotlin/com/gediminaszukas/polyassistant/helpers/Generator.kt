package com.gediminaszukas.polyassistant.helpers

import com.gediminaszukas.polyassistant.RuntimeTypeAdapterFactory
import com.google.gson.GsonBuilder
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.TypeSpec
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.TypeElement

class Generator(
    private val polymorphicTypesHierarchies: Map<TypeElement, Set<TypeElement>>,
    private val processingEnv: ProcessingEnvironment
) {

    fun generateCode() {
        val fileSpec = FileSpec.builder("", GENERATED_OBJECT_NAME)
            .addType(
                TypeSpec.objectBuilder(GENERATED_OBJECT_NAME)
                    .addFunction(
                        createFunctionSpec()
                    ).build()
            ).build()
        fileSpec.writeTo(processingEnv.filer)
    }

    private fun createFunctionSpec(): FunSpec {
        val funcSpecBuilder = FunSpec.builder("registerIn")
            .addParameter("gsonBuilder", GsonBuilder::class.java)
        createFunctionBody(funcSpecBuilder)
        return funcSpecBuilder.build()
    }

    private fun createFunctionBody(funcSpecBuilder: FunSpec.Builder) {
        var factoryIndex = 0
        polymorphicTypesHierarchies.forEach { (parentType, parentTypeChildren) ->
            val factoryParamName = "factory${factoryIndex}"
            funcSpecBuilder.addStatement(
                "val $factoryParamName = %T.of($parentType::class.java)",
                RuntimeTypeAdapterFactory::class.java
            )

            parentTypeChildren.forEach { childType ->
                funcSpecBuilder.addStatement(".registerSubtype(${childType}::class.java)")
            }

            funcSpecBuilder.addStatement("gsonBuilder.registerTypeAdapterFactory($factoryParamName)")
            funcSpecBuilder.addStatement("")
            factoryIndex++
        }
    }

    companion object {
        private const val GENERATED_OBJECT_NAME = "GeneratedRuntimeTypeAdapterFactories"
    }
}
