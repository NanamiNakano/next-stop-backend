val exposedVersion: String by project

plugins {
    kotlin("jvm") version "1.9.0"
    application
    id("io.ktor.plugin") version "2.3.4"
    kotlin("plugin.serialization") version "1.9.0"

}

group = "dev.thynanami.nextstop"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    val logbackVersion = "1.4.11"
    testImplementation(kotlin("test"))

    implementation("org.jetbrains.exposed:exposed-core:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-crypt:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-dao:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-kotlin-datetime:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-json:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-money:$exposedVersion")
    implementation("org.postgresql:postgresql:42.2.27")

    implementation("io.ktor:ktor-server-core")
    implementation("io.ktor:ktor-server-netty")

    implementation("ch.qos.logback:logback-classic:$logbackVersion")
    implementation("io.github.oshai:kotlin-logging-jvm:5.1.0")
    implementation("io.ktor:ktor-server-content-negotiation")
    implementation("io.ktor:ktor-serialization-kotlinx-json")
    implementation("io.ktor:ktor-server-auth")
    implementation("io.ktor:ktor-network-tls-certificates")
    implementation("com.password4j:password4j:1.7.3")
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(17)
}

application {
    mainClass.set("io.ktor.server.netty.EngineMain")
}