plugins {
	java
	id("org.springframework.boot") version "3.5.5"
	id("io.spring.dependency-management") version "1.1.7"
}

group = "com.example"
version = "0.0.1-SNAPSHOT"
description = "Demo project for Spring Boot"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(17)
	}
}

repositories {
	mavenCentral()
}

dependencies {
	compileOnly ("org.projectlombok:lombok:1.18.30")
	annotationProcessor("org.projectlombok:lombok:1.18.30")
	implementation("org.springframework.boot:spring-boot-starter")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa") // Optional: if using JPA
	implementation("com.oracle.database.jdbc:ojdbc11:23.9.0.25.07")// Oracle JDBC driver
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}
// For Gradle 5.0 and above
configurations {
	compileOnly {
		extendsFrom(annotationProcessor.get())
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}