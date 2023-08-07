plugins {
    standardKotlinJvmModule()
}

dependencies {
    implementation("org.postgresql:postgresql:42.6.0")
    implementation(project(":domain"))
}