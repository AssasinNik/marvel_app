plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("com.google.devtools.ksp")
    id ("dagger.hilt.android.plugin")
    id("org.jetbrains.kotlin.plugin.serialization")
    alias(libs.plugins.google.gms.google.services)

}

android {
    namespace = "com.example.marvel_app"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.marvel_app"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        renderscriptTargetApi = 30
        renderscriptSupportModeEnabled = true

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.0"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.compilercommon)
    implementation(libs.firebase.auth)
    implementation(libs.androidx.runtime.livedata)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    implementation(libs.kotlin.stdlib)

    implementation(libs.androidx.lifecycle.viewmodel.ktx)

    // Jetpack Compose
    implementation(libs.ui)
    implementation(libs.androidx.material)
    implementation(libs.ui.tooling)
    implementation(libs.androidx.navigation.compose)

    // Retrofit и OkHttp
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.okhttp)
    implementation(libs.logging.interceptor)

    // Timber для логирования
    implementation(libs.timber)

    // Kotlin Coroutines
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)

    // Жизненные циклы ViewModel и Runtime
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)

    // Gson для сериализации JSON
    implementation(libs.gson)

    // Coil для загрузки изображений
    implementation(libs.coil)
    implementation(libs.coil.compose)
    implementation (libs.accompanist.coil)

    // Hilt для Dependency Injection
    ksp(libs.hilt.compiler)
    implementation(libs.hilt.android)
    implementation (libs.androidx.hilt.navigation.compose)
    implementation (libs.androidx.hilt.navigation.compose.v100alpha01)

    // Kotlin Serialization для JSON
    implementation(libs.kotlinx.serialization.json)
    // Room
    implementation (libs.androidx.room.runtime.v261)
    implementation (libs.androidx.room.ktx.v261)
    ksp (libs.androidx.room.compiler.v261)

    implementation (libs.androidx.core.splashscreen)

    //Webview for Trailers
    implementation (libs.compose.webview)

    //Google
    implementation (libs.play.services.auth)

    // WorkManager
    implementation (libs.androidx.work.runtime.ktx)

    implementation ("com.google.guava:listenablefuture:9999.0-empty-to-avoid-conflict-with-guava")

}