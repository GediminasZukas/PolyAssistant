import org.gradle.api.JavaVersion

object Versions {
    const val GSON = "2.8.6"
    const val KOTLIN = "1.4.10"
    const val KOTLIN_POET = "1.7.2"
    val JVM_TARGET = JavaVersion.VERSION_1_8.toString()
}

object Libs {
    const val GSON = "com.google.code.gson:gson:${Versions.GSON}"
    const val KOTLIN_POET = "com.squareup:kotlinpoet:${Versions.KOTLIN_POET}"
    const val KOTLIN_STDLIB = "org.jetbrains.kotlin:kotlin-stdlib-jdk8"
}