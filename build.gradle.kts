
buildscript {

    repositories {
        google()
        jcenter()
        mavenCentral()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:3.6.1")
        classpath(kotlin("gradle-plugin", dev.mobilehealth.reimaginedlamp.gradle.BuildConfig.kotlinVersion))
        classpath("org.jetbrains.kotlinx:atomicfu-gradle-plugin:0.14.2")
    }
}
allprojects {
    group = "dev.mobilehealth.reimaginedlamp"
    version = "1.0"

    repositories {
        jcenter()
        google()
    }
}
