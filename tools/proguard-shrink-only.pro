-dontobfuscate
-dontoptimize
-keepattributes SourceFile, LineNumberTable, *Annotation*, Signature, InnerClasses, EnclosingMethod


##### work manager

-keep class * extends androidx.work.Worker
-keep class * extends androidx.work.InputMerger
# Keep all constructors on ListenableWorker, Worker (also marked with @Keep)
-keep public class * extends androidx.work.ListenableWorker {
    public <init>(...);
}
# We need to keep WorkerParameters for the ListenableWorker constructor
-keep class androidx.work.WorkerParameters

###### androidx
-dontwarn com.google.android.material.**
-keep class com.google.android.material.** { *; }

-dontwarn androidx.**
-keep class androidx.** { *; }
-keep interface androidx.** { *; }

##### butterknife
# Retain generated class which implement Unbinder.
-keep public class * implements butterknife.Unbinder { public <init>(**, android.view.View); }

# Prevent obfuscation of types which use ButterKnife annotations since the simple name
# is used to reflectively look up the generated ViewBinding.
-keep class butterknife.*
-keepclasseswithmembernames class * { @butterknife.* <methods>; }
-keepclasseswithmembernames class * { @butterknife.* <fields>; }

###### dagger
-dontwarn com.google.errorprone.annotations.**

###### RSS libraries
-keep class com.rometools.** { *;}
-dontnote com.rometools.rome.**
-dontwarn javax.xml.stream.**
-dontwarn org.jaxen.**
-dontwarn java.nio.**
-dontwarn org.codehaus.mojo.animal_sniffer.**
-dontwarn org.slf4j.impl.**

###### otto
-keepattributes *Annotation*
-keepclassmembers class ** {
    @com.squareup.otto.Subscribe public *;
    @com.squareup.otto.Produce public *;
}