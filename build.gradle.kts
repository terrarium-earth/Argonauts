plugins {
    id("earth.terrarium.cloche") version "0.9.4"
}

group = "earth.terrarium"
version = "2.0.0-alpha.5+1.21"

repositories {
    cloche.librariesMinecraft()
    mavenCentral()
    cloche.mavenNeoforged()

    cloche {
        main()

        // mavenNeoforgedMeta()
        maven(url = "https://maven.neoforged.net/mojang-meta")
        mavenFabric()
    }
}

cloche {
    minecraftVersion = "1.21"

    metadata {
        modId = "odyssey_allies"
        name = "Odyssey Allies"
        license = "MIT"
        description = "Form a guild to live and work together with your friends. Form a party to fight and adventure together with allies. Chat with your guild, party or friends list in a neat UI"
    }

    neoforge {
        loaderVersion = "21.0.167"

        data()

        runs {
            server()
            client()

            data()
        }
    }

    fabric {
        loaderVersion = "0.16.10"

        metadata {
            entrypoint("main", "org.example.fabric.FabricExampleMod::initialize")
        }

        data()
        client()

        dependencies {
            fabricApi("0.102.0")
        }

        runs {
            server()
            client()
            data()
        }
    }
}