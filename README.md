## Common Networking
This library mod unifies packet registration and dispatch across Fabric, Forge, and NeoForge so
developers using Jared's [MultiLoader Template][3] only deal with networking in the common module.
It is not limited to MultiLoader, it works in any multi-loader setup.

It also keeps your mod loader-agnostic on the wire: a Forge client can talk to a Fabric server
(and vice versa). You will still need your own handshake packet if you care about gating.

Note:
Being loader-agnostic may no longer be valid; modloaders have changed their registration systems
since this project was first created.

#### Downloads
- [CurseForge][1]
- [Modrinth][2]

### Add to your project
Versions follow `{mod_version}-{minecraft_version}`, e.g. `1.0.22-26.1.2`.

```groovy
repositories {
    maven {
        url = "https://maven.blamejared.com"
    }
}

// Fabric
dependencies {
    modImplementation "mysticdrew:common-networking-fabric:${version}"
}

// Forge
dependencies {
    implementation "mysticdrew:common-networking-forge:${version}"
}

// NeoForge
dependencies {
    implementation "mysticdrew:common-networking-neoforge:${version}"
}

// Common
dependencies {
    implementation "mysticdrew:common-networking-common:${version}"
}
```

### How to use
**1. Write your packet** with a static `type()` returning a `CustomPacketPayload.Type`, a
`STREAM_CODEC`, and a `handle(PacketContext<...>)` method:

```java
public class MyPacket
{
    public static final Identifier CHANNEL = Identifier.fromNamespaceAndPath(MOD_ID, "my_packet");
    public static final StreamCodec<FriendlyByteBuf, MyPacket> STREAM_CODEC =
            StreamCodec.ofMember(MyPacket::encode, MyPacket::new);

    public MyPacket() {}
    public MyPacket(FriendlyByteBuf buf) { /* decode */ }
    public void encode(FriendlyByteBuf buf) { /* encode */ }

    public static CustomPacketPayload.Type<CustomPacketPayload> type()
    {
        return new CustomPacketPayload.Type<>(CHANNEL);
    }

    public static void handle(PacketContext<MyPacket> ctx)
    {
        if (ctx.side() == Side.SERVER) {
            // server-side handling; ctx.sender() is the ServerPlayer
        } else {
            // client-side handling
        }
    }
}
```

The packet POJO does not need to implement `CustomPacketPayload` itself; the library wraps it
internally before sending it across the wire.

**2. Register your packets during startup** from your common (or common-client) mod class:

```java
Network.registerPacket(MyPacket.type(), MyPacket.class, MyPacket.STREAM_CODEC, MyPacket::handle);
```

`registerPacket` returns a chainable registrar, so you can register many packets fluently
(see [`ExamplePacketRegistration`](common/src/testmod/java/example/network/ExamplePacketRegistration.java)).

**3. Send packets** via either the static [`Dispatcher`](common/src/main/java/commonnetwork/api/Dispatcher.java)
facade or the [`NetworkHandler`](common/src/main/java/commonnetwork/api/NetworkHandler.java) returned by
[`Network.getNetworkHandler()`](common/src/main/java/commonnetwork/api/Network.java):

```java
Dispatcher.sendToServer(new MyPacket());
Dispatcher.sendToClient(new MyPacket(), serverPlayer);
Dispatcher.sendToAllClients(new MyPacket(), server);
```

### Example mod
A full working example lives under the `testmod` source set:
- Shared code: [`common/src/testmod/java/example/`](common/src/testmod/java/example/)
- Fabric entry: [`fabric/src/testmod/java/example/fabric/ExampleModFabric.java`](fabric/src/testmod/java/example/fabric/ExampleModFabric.java)
- Forge entry: [`forge/src/testmod/java/example/forge/ExampleModForge.java`](forge/src/testmod/java/example/forge/ExampleModForge.java)
- NeoForge entry: [`neoforge/src/testmod/java/example/neoforge/ExampleModNeoForge.java`](neoforge/src/testmod/java/example/neoforge/ExampleModNeoForge.java)

The example mod is compiled by each loader's `compileTestmodJava` task and packaged into a
`*-testmod.jar` by `:fabric:testmodJar`, `:forge:testmodJar`, and `:neoforge:testmodJar`. It is not
published to maven; it exists only so the library is verified against a real consumer and so the
IDE run configurations can launch a client/server that exercises the API end-to-end.

## Contributing
Pull Requests are welcome and encouraged.

### Working with the source
* IntelliJ IDEA
* JDK 25

### IntelliJ setup
Common Networking uses the [MultiLoader Template][3] pattern for sharing Fabric/Forge/NeoForge
sources via a single common module.

1. If your default JVM is not JDK 25, set it via `File > Settings > Build, Execution, Deployment >
   Build Tools > Gradle > Gradle JVM` and `File > Project Structure > Project SDK`. Then refresh
   the Gradle project.
2. Let the initial Gradle sync finish; it will fetch Minecraft, the loader toolchains, and the
   shared common sources.
3. Run configurations for each loader's client and server are generated automatically (Fabric via
   loom, NeoForge via moddev, Forge via ForgeGradle). They include the example mod's `testmod`
   source set on the classpath, so launching any of them loads the example mod alongside the
   library.

[1]: https://www.curseforge.com/minecraft/mc-mods/common-network
[2]: https://modrinth.com/mod/common-network
[3]: https://github.com/jaredlll08/MultiLoader-Template
