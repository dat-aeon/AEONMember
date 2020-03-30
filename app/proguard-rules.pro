# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

-keep class com.google.android.gms.internal.**{*;}
-dontwarn com.google.android.gms.internal.**

-keep class com.tailoredapps.biometricauth.delegate.**{*;}
-dontwarn com.tailoredapps.biometricauth.delegate.**

-keep class com.fasterxml.jackson.databind.ext.**{*;}
-dontwarn com.fasterxml.jackson.databind.ext.**

-keep class com.google.android.gms.analytics.**{*;}
-dontwarn com.google.android.gms.analytics.**

-keep class com.google.android.gms.tagmanager.**{*;}
-dontwarn com.google.android.gms.tagmanager.**

-keep class com.tailoredapps.biometricauth.**{*;}
-dontwarn com.tailoredapps.biometricauth.**

-keep class okhttp3.internal.platform.**{*;}
-dontwarn okhttp3.internal.platform.**

-keep class com.squareup.**{*;}
-dontwarn com.squareup.**

-keep class retrofit2.**{*;}
-dontwarn retrofit2.**

-keep class okhttp3.**{*;}
-dontwarn okhttp3.**

-keep class mm.com.aeon.vcsaeon**{*;}
