plugins {
    id("kotlin")
    maven
}

java {
    withSourcesJar()
    withJavadocJar()
}

dependencies {
    implementation(Libs.KOTLIN_STDLIB)
    implementation(Libs.GSON)
}
