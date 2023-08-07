plugins {
    standardKotlinJvmModule()
    application
}
repositories {
    gradlePluginPortal()
}
dependencies {
    implementation("org.jetbrains.kotlin", "kotlin-gradle-plugin", "1.8.10")
}
application {
    mainClass.set("co.vgw.webapi.app.MainKt")
}
tasks.distTar {
    archiveFileName.set("app-bundle.${archiveExtension.get()}")
}