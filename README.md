# PolyAssistant

[![](https://jitpack.io/v/GediminasZukas/PolyAssistant.svg)](https://jitpack.io/#GediminasZukas/PolyAssistant)

Incremental annotation processor which helps to (de)serialize Kotlin polymorphic types with [Gson](https://github.com/google/gson) in a simple way.

### Problem example

Consider such Kotlin sealed class:

```
sealed class Shape {
    abstract val area: Double
    data class Rectangle(override val area: Double) : Shape()
    data class Circle(override val area: Double) : Shape()
}
```

By default Gson doesn't know how to deserialize `Shape` and such attempt would produce an error:

```
val shape = Shape.Circle(area = 3.8)
val shapeJson = gsonInstance.toJson(shape)
val deserializedShape = gsonInstance.fromJson(shapeJson, Shape::class.java) // error
println("Shape area is: ${deserializedShape.area}")
```

### Solution with the library

1. Add few annotations to the sealed class hierarchy:

```
@Polymorphic
sealed class Shape {
    abstract val area: Double
    @Subtype
    data class Rectangle(override val area: Double) : Shape()
    @Subtype
    data class Circle(override val area: Double) : Shape()
}
```

2. Try to build your project and link generated code with your gson instance builder:

```
GsonBuilder().apply {
    GeneratedRuntimeTypeAdapterFactories.registerIn(this)
}.create()
```

Wolla! Step 2 is required only once. For further problems just annotate your types like in step 1.

Library supports not only sealed classes but also other languge-common polymorphic types hierarchies. 

### Internals

Annotation processor basically generates [RuntimeTypeAdapterFactory](https://github.com/google/gson/blob/master/extras/src/main/java/com/google/gson/typeadapters/RuntimeTypeAdapterFactory.java) objects (gson solution for polymorphic types serialization) and adds each of them into supplied ```GsonBuilder```.

### Download

```
dependencies {
    implementation("com.gediminaszukas.polyassistant:annotations:<version>")
    kapt("com.gediminaszukas.polyassistant:processor:<version>")
}
```

### License

```
Copyright (C) 2020 Gediminas Žukas

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
