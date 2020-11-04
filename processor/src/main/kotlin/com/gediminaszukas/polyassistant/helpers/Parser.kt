package com.gediminaszukas.polyassistant.helpers

import javax.annotation.processing.ProcessingEnvironment
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.element.TypeElement
import javax.lang.model.util.ElementFilter
import javax.tools.Diagnostic

class Parser(
    private val roundEnv: RoundEnvironment,
    private val processingEnv: ProcessingEnvironment,
    private val parentTypeAnnotation: TypeElement,
    private val childTypeAnnotation: TypeElement
) {

    fun parseAnnotatedTypeElements(): Map<TypeElement, Set<TypeElement>> {
        val parentTypeElements = getElementsAnnotatedWith(parentTypeAnnotation)
        val childTypeElements = getElementsAnnotatedWith(childTypeAnnotation)
        requireNotEmptyCollections(parentTypeElements, childTypeElements)

        val polymorphicTypesHierarchies = mutableMapOf<TypeElement, MutableSet<TypeElement>>()
        setParentTypeElements(parentTypeElements, polymorphicTypesHierarchies)
        setChildTypeElements(childTypeElements, parentTypeElements, polymorphicTypesHierarchies)
        requireParentsWithoutEmptyChildren(polymorphicTypesHierarchies)

        return polymorphicTypesHierarchies
    }

    private fun getElementsAnnotatedWith(typeAnnotation: TypeElement): Set<TypeElement> {
        return ElementFilter.typesIn(roundEnv.getElementsAnnotatedWith(typeAnnotation))
    }

    private fun requireNotEmptyCollections(parentTypeElements: Set<TypeElement>, childTypeElements: Set<TypeElement>) {
        if (parentTypeElements.isEmpty() || childTypeElements.isEmpty()) {
            showBreakingErrorMessage(
                "No elements found annotated with @$parentTypeAnnotation or @$childTypeAnnotation"
            )
        }
    }

    private fun setParentTypeElements(
        parentTypeElements: Set<TypeElement>,
        polymorphicTypesHierarchies: MutableMap<TypeElement, MutableSet<TypeElement>>
    ) {
        parentTypeElements.forEach { parentType ->
            polymorphicTypesHierarchies[parentType] = mutableSetOf()
        }
    }

    private fun setChildTypeElements(
        childTypeElements: Set<TypeElement>,
        parentTypeElements: Set<TypeElement>,
        polymorphicTypesHierarchies: Map<TypeElement, MutableSet<TypeElement>>
    ) {
        childTypeElements.forEach { childTypeElement ->
            val parent = getParentElementOfChild(childTypeElement, parentTypeElements)
            val existingParentChildren = polymorphicTypesHierarchies[parent]
            existingParentChildren?.add(childTypeElement)
        }
    }

    private fun getParentElementOfChild(
        childTypeElement: TypeElement,
        parenTypeElements: Set<TypeElement>
    ): TypeElement? {
        val parentClass = getParentClass(childTypeElement, parenTypeElements)
        if (parentClass != null) {
            return parentClass
        }

        val parentInterface = getParentInterface(childTypeElement, parenTypeElements)
        if (parentInterface != null) {
            return parentInterface
        }

        showBreakingErrorMessage(
            "No parent element of child $childTypeElement found annotated with @$parentTypeAnnotation"
        )
        return null
    }

    private fun getParentClass(childTypeElement: TypeElement, parenTypeElements: Set<TypeElement>): TypeElement? {
        val childSuperClass = processingEnv.elementUtils.getTypeElement(childTypeElement.superclass.toString())
        return getParentTypeElementIfExists(parenTypeElements, childSuperClass)
    }

    private fun getParentInterface(childTypeElement: TypeElement, parenTypeElements: Set<TypeElement>): TypeElement? {
        return childTypeElement.interfaces
            .map { processingEnv.typeUtils.asElement(it) as TypeElement }
            .firstOrNull { getParentTypeElementIfExists(parenTypeElements, it) != null }
    }

    private fun getParentTypeElementIfExists(
        parenTypeElements: Set<TypeElement>,
        targetTypeElement: TypeElement
    ): TypeElement? {
        return parenTypeElements.find { it == targetTypeElement }
    }

    private fun requireParentsWithoutEmptyChildren(polymorphicTypesHierarchies: Map<TypeElement, Set<TypeElement>>) {
        polymorphicTypesHierarchies.forEach { (parent, children) ->
            if (children.isEmpty()) {
                showBreakingErrorMessage(
                    "No child elements of $parent found annotated with @$childTypeAnnotation"
                )
            }
        }
    }

    private fun showBreakingErrorMessage(errorMsg: String) {
        processingEnv.messager.printMessage(Diagnostic.Kind.ERROR, "$errorMsg \r")
    }
}
