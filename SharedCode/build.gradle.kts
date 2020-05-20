import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import dev.mobilehealth.reimaginedlamp.gradle.BuildConfig

plugins {
    id("com.android.library")
    kotlin("multiplatform")
    id("com.android.library")
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
           // publishLibraryVariants("release", "debug")
        }

//        jvm("android")
//        jvm()
//        fromPreset(presets.android, "android")
    }
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

    sourceSets["commonMain"].dependencies {
        implementation("org.jetbrains.kotlin:kotlin-stdlib-common")

        implementation("co.touchlab:firestore:${BuildConfig.firebaseKmpVersion}")
        implementation("com.google.firebase:firebase-firestore:${BuildConfig.firebaseFirestoreVersion}")
        implementation("com.google.firebase:firebase-core:${BuildConfig.firebaseCoreVersion}")

        implementation("com.soywiz.korlibs.krypto:krypto:${BuildConfig.kryptoVersion}")
        api("com.benasher44:uuid:${BuildConfig.benasherUuidVersion}")
        implementation("com.soywiz.korlibs.klock:klock:${BuildConfig.klockVersion}")
        // MOKO - MVVM
        api("dev.icerock.moko:mvvm:${BuildConfig.mokkoMvvmVersion}")

        implementation("com.github.aakira:napier:${BuildConfig.napierVersion}")

        // COROUTINES
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-common:${BuildConfig.coroutineVersion}")
    }


    sourceSets["commonTest"].dependencies {
        implementation(kotlin("test-common"))
        implementation(kotlin("test-annotations-common"))
    }

    sourceSets["androidMain"].dependencies {
        implementation("org.jetbrains.kotlin:kotlin-stdlib")
        implementation("androidx.lifecycle:lifecycle-extensions:${BuildConfig.androidLifecycleVersion}")
        // coroutines
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:${BuildConfig.coroutineVersion}")

        // KTOR
//        implementation("io.ktor:ktor-client-android:${BuildConfig.ktorVersion}")
        implementation("com.github.aakira:napier-android:${BuildConfig.napierVersion}")

        implementation("com.google.firebase:firebase-firestore:${BuildConfig.firestoreAndroid}")
    }

    sourceSets["androidTest"].dependencies {
        implementation(kotlin("test"))
        implementation(kotlin("test-junit"))
    }

    sourceSets["iosMain"].dependencies {
        api("org.jetbrains.kotlinx:kotlinx-serialization-runtime-native:0.20.0")
        implementation("com.github.aakira:napier-ios:${BuildConfig.napierVersion}")

        // COROUTINEdownload.jetbrains.com/kotlin/native/clang-llvm-apple-8.0.0-darwin-macos.tar.gz
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-native:${BuildConfig.coroutineVersion}")
        // KTOR
//        implementation("io.ktor:ktor-client-ios:${BuildConfig.ktorVersion}")
    }
}

val packForXcode by tasks.creating(Sync::class) {
    val targetDir = File(buildDir, "xcode-frameworks")

    /// selecting the right configuration for the iOS 
    /// framework depending on the environment
    /// variables set by Xcode build
    // or XCODE_CONFIGURATION
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