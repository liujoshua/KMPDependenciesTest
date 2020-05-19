import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import dev.mobilehealth.reimaginedlamp.gradle.BuildConfig

plugins {
    kotlin("multiplatform")
}

kotlin {
    //select iOS target platform depending on the Xcode environment variables
    val iOSTarget: (String, KotlinNativeTarget.() -> Unit) -> KotlinNativeTarget =
        if (System.getenv("SDK_NAME")?.startsWith("iphoneos") == true)
            ::iosArm64
        else
            ::iosX64

    iOSTarget("ios") {
        binaries {
            framework {
                baseName = "SharedCode"
            }
        }
    }

    jvm("android")

    sourceSets["commonMain"].dependencies {
        implementation("org.jetbrains.kotlin:kotlin-stdlib-common")

        implementation("co.touchlab:firestore:${BuildConfig.firebaseKmpVersion}")
        implementation("com.google.firebase:firebase-firestore:${BuildConfig.firebaseFirestoreVersion}")
        implementation("com.google.firebase:firebase-core:17.0.1${BuildConfig.firebaseCoreVersion}")
    }

    sourceSets["commonTest"].dependencies {
        implementation(kotlin("test-common"))
        implementation(kotlin("test-annotations-common"))
    }

    sourceSets["androidMain"].dependencies {
        implementation("org.jetbrains.kotlin:kotlin-stdlib")
    }

    sourceSets["androidTest"].dependencies {
        implementation(kotlin("test"))
        implementation(kotlin("test-junit"))
    }

    sourceSets["iosMain"].dependencies {
        api("org.jetbrains.kotlinx:kotlinx-serialization-runtime-native:0.20.0")
    }
}

val packForXcode by tasks.creating(Sync::class) {
    val targetDir = File(buildDir, "xcode-frameworks")

    /// selecting the right configuration for the iOS 
    /// framework depending on the environment
    /// variables set by Xcode build
    val mode = System.getenv("CONFIGURATION") ?: "DEBUG"
    val framework = kotlin.targets
        .getByName<KotlinNativeTarget>("ios")
        .binaries.getFramework(mode)
    inputs.property("mode", mode)
    dependsOn(framework.linkTask)

    from({ framework.outputDirectory })
    into(targetDir)

    /// generate a helpful ./gradlew wrapper with embedded Java path
    doLast {
        val gradlew = File(targetDir, "gradlew")
        gradlew.writeText(
            "#!/bin/bash\n"
                    + "export 'JAVA_HOME=${System.getProperty("java.home")}'\n"
                    + "cd '${rootProject.rootDir}'\n"
                    + "./gradlew \$@\n"
        )
        gradlew.setExecutable(true)
    }
}

tasks.getByName("build").dependsOn(packForXcode)