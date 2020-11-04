package com.gediminaszukas.polyassistant

import com.gediminaszukas.polyassistant.helpers.Generator
import com.gediminaszukas.polyassistant.helpers.Parser
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.ProcessingEnvironment
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.SourceVersion
import javax.lang.model.element.TypeElement

class Processor : AbstractProcessor() {

    private val parentTypeAnnotationName = Polymorphic::class.java.name
    private val childTypeAnnotationName = Subtype::class.java.name

    private var passedFirstProcessingRound = false

    override fun init(processingEnv: ProcessingEnvironment) {
        super.init(processingEnv)
        passedFirstProcessingRound = false
    }

    override fun process(annotations: Set<TypeElement>, roundEnv: RoundEnvironment): Boolean {
        if (passedFirstProcessingRound) return true

        val parentTypeAnnotation = processingEnv.elementUtils.getTypeElement(parentTypeAnnotationName)
        val childTypeAnnotation = processingEnv.elementUtils.getTypeElement(childTypeAnnotationName)

        val parser = Parser(roundEnv, processingEnv, parentTypeAnnotation, childTypeAnnotation)
        val polymorphicTypesHierarchies = parser.parseAnnotatedTypeElements()

        val generator = Generator(polymorphicTypesHierarchies, processingEnv)
        generator.generateCode()

        passedFirstProcessingRound = true

        return true
    }

    override fun getSupportedSourceVersion(): SourceVersion {
        return SourceVersion.latestSupported()
    }

    override fun getSupportedAnnotationTypes(): Set<String> {
        return setOf(parentTypeAnnotationName, childTypeAnnotationName)
    }
}
