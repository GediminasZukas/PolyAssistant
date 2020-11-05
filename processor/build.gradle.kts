plugins {
    id("kotlin")
    maven
}

dependencies {
    implementation(Libs.KOTLIN_STDLIB)
    implementation(project(":annotations"))
    implementation(Libs.KOTLIN_POET)
    implementation(Libs.GSON)
}
