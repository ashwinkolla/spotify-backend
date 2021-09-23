import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "2.5.0"
	id("io.spring.dependency-management") version "1.0.11.RELEASE"
	kotlin("jvm") version "1.5.0"
	kotlin("plugin.serialization") version "1.5.0"
	kotlin("plugin.spring") version "1.5.0"
}

group = "com.spotify-service"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	implementation("org.springframework.boot:spring-boot-starter-actuator")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("com.squareup.okhttp3:okhttp:4.2.2")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.8.+")
	implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.2.1")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "11"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}

val ktlint by configurations.creating

dependencies {
	ktlint("com.pinterest:ktlint:0.42.1") {
		attributes {
			attribute(Bundling.BUNDLING_ATTRIBUTE, objects.named(Bundling.EXTERNAL))
		}
	}
	// ktlint(project(":custom-ktlint-ruleset")) // in case of custom ruleset
}

val outputDir = "${project.buildDir}/reports/ktlint/"
val inputFiles = project.fileTree(mapOf("dir" to "src", "include" to "**/*.kt"))

val ktlintCheck by tasks.creating(JavaExec::class) {
	inputs.files(inputFiles)
	outputs.dir(outputDir)

	description = "Check Kotlin code style."
	classpath = ktlint
	main = "com.pinterest.ktlint.Main"
	args = listOf("src/**/*.kt")
}

val ktlintFormat by tasks.creating(JavaExec::class) {
	inputs.files(inputFiles)
	outputs.dir(outputDir)

	description = "Fix Kotlin code style deviations."
	classpath = ktlint
	main = "com.pinterest.ktlint.Main"
	args = listOf("-F", "src/**/*.kt")
}