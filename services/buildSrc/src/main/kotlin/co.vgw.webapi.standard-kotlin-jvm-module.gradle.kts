plugins {
    kotlin("jvm")
}
repositories {
    mavenCentral()
}

val catalogs = extensions.getByType<VersionCatalogsExtension>()
val libs = catalogs.named("libs")

dependencies {
    implementation(kotlin("stdlib"))
    testImplementation(kotlin("test"))
    testImplementation(libs.findLibrary("junit-params").get())
}
tasks.test {
    useJUnitPlatform()
}