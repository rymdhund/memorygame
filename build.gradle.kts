plugins {
    id("java")
    application
}

group = "com.github.rymdhund"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.googlecode.lanterna:lanterna:3.1.1")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")
}

application {
    mainClass.set("com.github.rymdhund.Main")
}

tasks.getByName<Jar>("jar") {
    manifest {
        attributes("Main-Class" to application.mainClass)
    }
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}

val fatJar = task("fatJar", type = Jar::class) {
    manifest {
        attributes("Main-Class" to application.mainClass)
    }
    from(configurations.runtimeClasspath.get().map({ if (it.isDirectory) it else zipTree(it) }))
    with(tasks.jar.get() as CopySpec)
}