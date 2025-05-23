import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlin)
    alias(libs.plugins.binary.compatibility.validator)
    `maven-publish`
    signing
    java
}

group = "app.revanced"

tasks {
    processResources {
        expand("projectVersion" to project.version)
    }

    test {
        useJUnitPlatform()
        testLogging {
            events("PASSED", "SKIPPED", "FAILED")
        }
    }
}

dependencies {
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.xpp3)
    implementation(libs.smali)
    implementation(libs.multidexlib2)
    implementation(libs.apktool.lib)
    implementation(libs.kotlin.reflect)

    // TODO: Convert project to KMP.
    compileOnly(libs.android) {
        // Exclude, otherwise the org.w3c.dom API breaks.
        exclude(group = "xerces", module = "xmlParserAPIs")
    }

    testImplementation(libs.kotlin.test)
}

java {
    targetCompatibility = JavaVersion.VERSION_11
    withJavadocJar()
    withSourcesJar()
}

kotlin {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_11)
    }
}

publishing {
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/revanced/revanced-patcher")
            credentials {
                username = System.getenv("GITHUB_ACTOR")
                password = System.getenv("GITHUB_TOKEN")
            }
        }
    }

    publications {
        create<MavenPublication>("revanced-patcher-publication") {
            from(components["java"])

            version = project.version.toString()

            pom {
                name = "ReVanced Patcher"
                description = "Patcher used by ReVanced."
                url = "https://revanced.app"

                licenses {
                    license {
                        name = "GNU General Public License v3.0"
                        url = "https://www.gnu.org/licenses/gpl-3.0.en.html"
                    }
                }
                developers {
                    developer {
                        id = "ReVanced"
                        name = "ReVanced"
                        email = "contact@revanced.app"
                    }
                }
                scm {
                    connection = "scm:git:git://github.com/revanced/revanced-patcher.git"
                    developerConnection = "scm:git:git@github.com:revanced/revanced-patcher.git"
                    url = "https://github.com/revanced/revanced-patcher"
                }
            }
        }
    }
}

signing {
    useGpgCmd()
    sign(publishing.publications["revanced-patcher-publication"])
}
