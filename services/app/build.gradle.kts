plugins {
    standardKotlinJvmModule()
    application
}
dependencies {
    implementation(libs.bundles.ktor.server.app)
    implementation(project(":domain"))
    implementation(project(":http"))
}
application {
    mainClass.set("co.vgw.webapi.app.MainKt")
}
tasks.distTar {
    archiveFileName.set("app-bundle.${archiveExtension.get()}")
}