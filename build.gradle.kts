plugins {
    kotlin("jvm") version "2.2.20" apply false
    kotlin("multiplatform") version "2.2.20" apply false
    id("io.ktor.plugin") version "3.3.1" apply false
}

subprojects {
    group = "com.example"
    version = "0.0.1"
}
