plugins {
    standardKotlinJvmModule()
    alias(libs.plugins.kotlin.serialization)
}

dependencies {
    implementation(libs.bundles.ktor.server.app)
    implementation(project(":domain"))
}