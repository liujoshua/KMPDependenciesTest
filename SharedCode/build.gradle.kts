import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import dev.mobilehealth.reimaginedlamp.gradle.BuildConfig

plugins {
    id("com.android.library")
    kotlin("multiplatform")
    kotlin("native.cocoapods")
}

android {
    compileSdkVersion(28)

    defaultConfig {
        minSdkVersion(21)
        targetSdkVersion(28)
    }

    sourceSets {
        getByName("main") {
            manifest.srcFile("src/androidMain/AndroidManifest.xml")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().all {

        kotlinOptions {
            jvmTarget = "1.8"
        }
    }
}

kotlin {

    targets {
        android("android") {
            publishLibraryVariants("release", "debug")
        }
    }

    val buildForDevice = project.findProperty("kotlin.native.cocoapods.target") == "ios_arm"
    if (buildForDevice) {
        iosArm64("ios")
//        iosArm32("ios32")

//        val iOSMain by sourceSets.creating
//        sourceSets["iosMain"].dependsOn(iOSMain)
//        sourceSets["ios32Main"].dependsOn(iOSMain)
    } else {
        iosX64("ios")
    }

    //select iOS target platform depending on the Xcode environment variables
//    val iOSTarget: (String, KotlinNativeTarget.() -> Unit) -> KotlinNativeTarget =
//        if (System.getenv("SDK_NAME")?.startsWith("iphoneos") == true)
//            ::iosArm64
//        else
//            ::iosX64
//
//    iOSTarget("ios") {
//        binaries {
//            framework {
//                baseName = "SharedCode"
//            }
//        }
//    }

    sourceSets {
        commonMain {
            dependencies {
                implementation("org.jetbrains.kotlin:kotlin-stdlib-common")

                // cryptography
                api("com.soywiz.korlibs.krypto:krypto:${BuildConfig.kryptoVersion}")

                // napier
                implementation("com.github.aakira:napier:${BuildConfig.napierVersion}")
                // UUID
                api("com.benasher44:uuid:${BuildConfig.benasherUuidVersion}")
                // MOKO - MVVM
                api("dev.icerock.moko:mvvm:${BuildConfig.mokkoMvvmVersion}")
                // KTOR
//                implementation("io.ktor:ktor-client-core:${BuildConfig.ktorVersion}")
                // TIME
                implementation("com.soywiz.korlibs.klock:klock:${BuildConfig.klockVersion}")

            }
        }

    }

//
//    sourceSets["commonMain"].dependencies {
//        implementation("org.jetbrains.kotlin:kotlin-stdlib-common")
//
//        // cryptography
//        api("com.soywiz.korlibs.krypto:krypto:${BuildConfig.kryptoVersion}")
//
//        // UUID
//        api("com.benasher44:uuid:${BuildConfig.benasherUuidVersion}")
//
//        // TIME
//        implementation("io.islandtime:core:${BuildConfig.islandTimeVersion}")
//    }

    sourceSets["commonTest"].dependencies {
        implementation(kotlin("test-common"))
        implementation(kotlin("test-annotations-common"))
    }

    sourceSets["androidMain"].dependencies {
        implementation("org.jetbrains.kotlin:kotlin-stdlib")
        implementation("androidx.lifecycle:lifecycle-extensions:${BuildConfig.androidLifecycleVersion}")

        implementation("com.github.aakira:napier-android:${BuildConfig.napierVersion}")
    }

    sourceSets["androidTest"].dependencies {
        implementation(kotlin("test"))
        implementation(kotlin("test-junit"))
    }

    sourceSets["iosMain"].dependencies {
        api("org.jetbrains.kotlinx:kotlinx-serialization-runtime-native:0.20.0")

        implementation("com.github.aakira:napier-ios:${BuildConfig.napierVersion}")
        // KTOR
//        implementation("io.ktor:ktor-client-ios:${BuildConfig.ktorVersion}")
    }



    cocoapods {
        summary = "Working with cocoapods"
        homepage = "https://github.com/liujoshua/reimagined-lamp"
    }
}


//
//val packForXcode by tasks.creating(Sync::class) {
//    val targetDir = File(buildDir, "xcode-frameworks")
//
//    /// selecting the right configuration for the iOS
//    /// framework depending on the environment
//    /// variables set by Xcode build
//    // or XCODE_CONFIGURATION
//    val mode = System.getenv("CONFIGURATION") ?: "DEBUG"
//    val framework = kotlin.targets
//        .getByName<KotlinNativeTarget>("ios")
//        .binaries.getFramework(mode)
//    inputs.property("mode", mode)
//    dependsOn(framework.linkTask)
//
//    from({ framework.outputDirectory })
//    into(targetDir)
//
//    /// generate a helpful ./gradlew wrapper with embedded Java path
//    doLast {
//        val gradlew = File(targetDir, "gradlew")
//        gradlew.writeText(
//            "#!/bin/bash\n"
//                    + "export 'JAVA_HOME=${System.getProperty("java.home")}'\n"
//                    + "cd '${rootProject.rootDir}'\n"
//                    + "./gradlew \$@\n"
//        )
//        gradlew.setExecutable(true)
//    }
//}
//
//tasks.getByName("build").dependsOn(packForXcode)
