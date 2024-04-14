## Common Networking:
This library mod aims to unify networking and packet registration between mod loaders so that
Developers using Jared's [MultiLoader Template][3] only need to deal with networking in the common module. 
But, it is not limited to only usage with MultiLoader. This can be used in other projects as well.

This also allows your mod to be loader agnostic. The way packets are built and constructed, 
Forge version of the mod on the client, can talk to the Fabric version on the server. 
Though you will need to create your own handshake packet.

#### Downloads:
- [CurseForge][1]
- [Modrinth][2]

### Add to project:
```
repositories {
    maven {
        url = "https://jm.gserv.me/repository/maven-public/"
    }
}
Fabric:
dependencies {
    modImplementation 'mysticdrew:common-networking-fabric:1.0.3-1.20.1'
}
Forge:
dependencies {
    implementation fg.deobf('mysticdrew:common-networking-forge:1.0.3-1.20.1')
}
NeoForge:
dependencies {
    implementation fg.deobf('mysticdrew:common-networking-neoforge:1.0.3-1.20.1')
}
Common:
dependencies {
    implementation 'mysticdrew:common-networking-common:1.0.3-1.20.1'
}
```
### How to Use:
Using the mod is quite simple.

Register your packets anytime during startup in your [Common Mod Class](example/common/ExampleModCommon.java) and/or your [Common Client Mod Class](example/common/client/ExampleModCommonClient.java). 

See [Packet Registration Class](example/common/network/ExamplePacketRegistration.java) the example uses a class with an init method. This is not required.

Creating packets are very similar to how you would do it in Forge or Fabric already, see [ExamplePacketOne](example/common/network/ExamplePacketOne.java).

Sending a packet can either use the [Dispatcher](common/src/main/java/commonnetwork/api/Dispatcher.java) for static calls or by 
getting the [NetworkHandler](common/src/main/java/commonnetwork/api/NetworkHandler.java) from the [Network Class](common/src/main/java/commonnetwork/api/Network.java).

## Contributing: 

Pull Requests are welcome and encouraged. 

### Working with the Source

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

[1]: https://www.curseforge.com/minecraft/mc-mods/common-network

[2]: https://modrinth.com/mod/common-network

[3]: https://github.com/jaredlll08/MultiLoader-Template
