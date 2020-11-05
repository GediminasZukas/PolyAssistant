import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version Versions.KOTLIN
}

allprojects {
    repositories {
        jcenter()
    }

    tasks.withType<Javadoc>().configureEach {
        (options as StandardJavadocDocletOptions).addStringOption("Xdoclint:none", "-quiet")
    }

    tasks.withType<JavaCompile>().configureEach {
        sourceCompatibility = Versions.JVM_TARGET
        targetCompatibility = Versions.JVM_TARGET
    }

    tasks.withType<KotlinCompile>().configureEach {
        kotlinOptions.jvmTarget = Versions.JVM_TARGET
    }
}
