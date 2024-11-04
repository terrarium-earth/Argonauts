architectury {
    val enabledPlatforms: String by rootProject
    common(enabledPlatforms.split(","))
}

dependencies {
    val placeholderApiVersion: String by project

    modCompileOnly(group = "tech.thatgravyboat", name = "commonats", version = "2.0")
    modImplementation(group = "eu.pb4", name = "placeholder-api", version = placeholderApiVersion)
}
