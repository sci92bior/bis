pluginManagement {
	repositories {
		maven { url = uri("https://repo.spring.io/milestone") }
		maven { url = uri("https://repo.spring.io/snapshot") }
		gradlePluginPortal()
	}
}
rootProject.name = "bis"

include(
	"dependencies",
	":modules:model",
	":modules:security",
	":modules:application"
)
