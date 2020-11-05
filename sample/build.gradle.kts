plugins {
    id("kotlin")
    id("kotlin-kapt")
}

dependencies {
    implementation(Libs.KOTLIN_STDLIB)
    implementation(Libs.GSON)
    implementation(project(":annotations"))
    kapt(project(":processor"))
}
