-keepattributes *Annotation*
-keepattributes SourceFile,LineNumberTable
-keep public class * extends java.lang.Exception
-keep class com.crashlytics.** { *; }
-keep class com.sibedge.team.common.** { *; }
-dontwarn com.crashlytics.**