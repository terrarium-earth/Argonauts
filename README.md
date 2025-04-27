# Odyssey Guilds

To add this library to your project, do the following:

Kotlin DSL:
```kotlin
repositories {
    maven(url = "https://maven.teamresourceful.com/repository/maven-public/")
}

dependencies {
    modImplementation(group = "earth.terrarium", name = "odyssey_guilds-$modLoader-$minecraftVersion", version = guildsVersion)
}
```

Groovy DSL:
```groovy
repositories {
    maven {
        url "https://maven.teamresourceful.com/repository/maven-public/"
    }
}

dependencies {
    modImplementation group: "earth.terrarium", name: "odyssey_guilds-$modLoader-$minecraftVersion", version: guildsVersion
}
```
