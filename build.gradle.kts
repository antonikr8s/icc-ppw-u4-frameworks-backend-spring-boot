plugins {
	id("java")
	id("org.springframework.boot") version "4.0.7"
	id("io.spring.dependency-management") version "1.1.7"
}

group = "ec.edu.ups.icc"
version = "0.0.1-SNAPSHOT"

// Práctica 16: Java 21
java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(17)
	}
}
repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-web")
	developmentOnly("org.springframework.boot:spring-boot-devtools")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")

	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	runtimeOnly("org.postgresql:postgresql")
	implementation("org.springframework.boot:spring-boot-starter-validation")

	// Seguridad y JWT
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("io.jsonwebtoken:jjwt-api:0.12.6")
	runtimeOnly("io.jsonwebtoken:jjwt-impl:0.12.6")
	runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.12.6")

	// Monitoreo
	implementation("org.springframework.boot:spring-boot-starter-actuator")

	//implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.5.0")
	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:3.0.3")
}

// Práctica 16: Nombre determinista para el JAR
tasks.bootJar {
	archiveFileName.set("app.jar")
}

tasks.jar {
	enabled = false
}

tasks.named<Test>("test") {
	useJUnitPlatform()
}