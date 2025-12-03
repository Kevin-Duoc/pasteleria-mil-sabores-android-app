// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    // La línea "alias(libs.plugins.kotlin.compose) apply false" que causaba el error ha sido eliminada.
}
