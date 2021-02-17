import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
}

kotlin {
    jvm {
        compilations.all {
            kotlinOptions {
                jvmTarget = "1.8"
                useIR = true
            }
        }
    }
    ios {
        binaries {
            framework {
                baseName = "clientcore"
            }
        }
    }
    js(IR) {
        browser {
            binaries.executable()
            testTask {
                useKarma {
                    useChromeHeadless()
                }
            }
        }
    }

    sourceSets {
        // Apache 2, https://github.com/ktorio/ktor/releases/latest
        val ktorVersion = "1.5.1"

        commonMain {
            dependencies {
                api(project(":shared"))

                // Apache 2, https://github.com/ktorio/ktor/releases/latest
                api("io.ktor:ktor-client-core:$ktorVersion")
                // Apache 2, https://github.com/Kotlin/kotlinx.coroutines/releases/latest
                api("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.4.2-native-mt")
            }
        }
        commonTest {
            dependencies {

            }
        }

        val jsMain by getting {
            dependencies {
                // Apache 2, https://bintray.com/kotlin/kotlin-js-wrappers/kotlin-react
                implementation("org.jetbrains:kotlin-react-router-dom:5.2.0-pre.146-kotlin-1.4.30")
                val reactVersion = "17.0.1-pre.146-kotlin-1.4.30"
                implementation("org.jetbrains:kotlin-react:$reactVersion")
                implementation("org.jetbrains:kotlin-react-dom:$reactVersion")
            }
        }

        val jsTest by getting {
            dependencies {
                implementation(kotlin("test-js"))
            }
        }

        val jvmMain by getting {
            dependencies {
            }
        }
        val jvmTest by getting {
            dependencies {
                api(kotlin("test-junit"))
            }
        }

        val iosMain by getting {
            dependencies {
                implementation("io.ktor:ktor-client-ios:$ktorVersion")
            }
        }
        val iosTest by getting
    }
}

tasks {
    val packForXcode by creating(Sync::class) {
        group = "build"
        val mode = System.getenv("CONFIGURATION") ?: "DEBUG"
        val sdkName = System.getenv("SDK_NAME") ?: "iphonesimulator"
        val targetName = "ios" + if (sdkName.startsWith("iphoneos")) "Arm64" else "X64"
        val framework =
            kotlin.targets.getByName<KotlinNativeTarget>(targetName).binaries.getFramework(mode)
        inputs.property("mode", mode)
        dependsOn(framework.linkTask)
        val targetDir = File(buildDir, "xcode-frameworks")
        from({ framework.outputDirectory })
        into(targetDir)
    }
    build { dependsOn(packForXcode) }
}
