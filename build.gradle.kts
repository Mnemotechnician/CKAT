plugins {
	kotlin("jvm") version "1.6.10"
}

java.sourceSets["main"].java {
    srcDir("src")
}

val mindustryVersion = "v135"
val jarName = "stingray"

repositories {
	mavenCentral()
	maven("https://jitpack.io")
}

dependencies {
	implementation(kotlin("stdlib-jdk8"))
	
	compileOnly("com.github.Anuken.Arc:arc-core:$mindustryVersion")
	compileOnly("com.github.Anuken.Mindustry:core:$mindustryVersion")
	
	//implementation("com.github.mnemotechnician:mkui:31") todo: do i need mkui?
	implementation(files("lib/Autoupdate-lib.jar"))
}

task("jarAndroid") {
	dependsOn("jar")
	
	doLast {
		val sdkRoot = System.getenv("ANDROID_HOME") ?: System.getenv("ANDROID_SDK_ROOT")
		
		if(sdkRoot == null || sdkRoot.isEmpty() || !File(sdkRoot).exists()) {
			throw GradleException("""
				No valid Android SDK found. Ensure that ANDROID_HOME is set to your Android SDK directory.
				Note: if the gradle daemon has been started before ANDROID_HOME env variable was defined, it won't be able to read this variable.
				In this case you have to run "./gradlew stop" and try again
			""".trimIndent());
		}
		
		println("searching for an android sdk... ")
		val platformRoot = File("$sdkRoot/platforms/").walkTopDown().findLast { 
			val fi = File(it, "android.jar")
			if (fi.exists()) {
				print(it)
				println(" — OK.")
			}
			fi.exists()
		}
		
		if (platformRoot == null) throw GradleException("No android.jar found. Ensure that you have an Android platform installed. (platformRoot = $platformRoot)")
		
		//collect dependencies needed to translate java 8+ bytecode code to android-compatible bytecode (yeah, android's dvm and art do be sucking)
		val dependencies = (configurations.compileClasspath.files + configurations.runtimeClasspath.files + File(platformRoot, "android.jar")).map { it.path }
		val dependenciesStr = Array<String>(dependencies.size * 2) {
			if (it % 2 == 0) "--classpath" else dependencies.elementAt(it / 2)
		}
		
		//dexing. As a result of this process, a .dex file will be added to the jar file. This requires d8 tool in your $PATH
		exec {
			workingDir("$buildDir/libs")
			commandLine("d8", *dependenciesStr, "--min-api", "14", "--output", "${jarName}-android.jar", "${jarName}-desktop.jar")
		}
	}
}

task<Jar>("release") {
	dependsOn("jarAndroid")
	
	duplicatesStrategy = DuplicatesStrategy.EXCLUDE
	archiveFileName.set("${jarName}-any-platform.jar")

	from(
		zipTree("$buildDir/libs/${jarName}-desktop.jar"),
		zipTree("$buildDir/libs/${jarName}-android.jar")
	)

	doLast {
		delete { delete("$buildDir/libs/${jarName}-desktop.jar") }
		delete { delete("$buildDir/libs/${jarName}-android.jar") }
	}
}


tasks.jar {
	duplicatesStrategy = DuplicatesStrategy.EXCLUDE
	archiveFileName.set("${jarName}-desktop.jar")

	from(*configurations.runtimeClasspath.files.map { if (it.isDirectory()) it else zipTree(it) }.toTypedArray())

	from(rootDir) {
		include("mod.hjson")
		include("icon.png")
	}

	from("assets/") {
		include("**")
	}
}