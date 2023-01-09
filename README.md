Initial project setup, more to come. 
## Current Maven Versions are not stable. 
### Add to project:
```
repositories {
    maven {
        url = "https://jm.gserv.me/repository/maven-public/"
    }
}
Fabric:
dependencies {
    modImplementation 'mysticdrew:common-networking:1.0.0+1.19.3-fabric'
}
Forge:
dependencies {
    implementation fg.deobf('mysticdrew:common-networking:1.0.0+1.19.3-forge')
}
Common:
dependencies {
    implementation fg.deobf('mysticdrew:common-networking:1.0.0+1.19.3-common')
}
```

## Requirements

* IntelliJ IDEA
* OpenJDK 1.17

### 1. Setup Common Networking with MultiLoader Template for IntelliJ IDEA

## IntelliJ IDEA
#### Common Networking uses the [MultiLoader Template][3] for combining sources of Fabric and Forge.

1. If your default JVM/JDK is not Java 17 you will encounter an error when opening the project. This error is fixed by going to `File > Settings > Build, Execution, Deployment > Build Tools > Gradle > Gradle JVM`and changing the value to a valid Java 17 JVM. You will also need to set the Project SDK to Java 17. This can be done by going to `File > Project Structure > Project SDK`. Once both have been set open the Gradle tab in IDEA and click the refresh button to reload the project.
2. Open the Gradle tab in IDEA if it has not already been opened. Navigate to `Your Project > Common > Tasks > vanilla gradle > decompile`. Run this task to decompile Minecraft.
3. Open the Gradle tab in IDEA if it has not already been opened. Navigate to `Your Project > Forge > Tasks > forgegradle runs > genIntellijRuns`. Run this task to set up run configurations for Forge.
4. Open your Run/Debug Configurations. Under the Application category there should now be options to run Forge and Fabric projects. Select one of the client options and try to run it.
5. Assuming you were able to run the game in step 7 your workspace should now be set up.

[3]: https://github.com/jaredlll08/MultiLoader-Template
