plugins {
    id("kotlin")
    id("kotlin-kapt")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation(Libs.GSON)
    implementation(project(":annotations"))
    kapt(project(":processor"))
}
