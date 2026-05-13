## Common Networking
This library mod unifies packet registration and dispatch across Fabric, Forge, NeoForge, and
Paper so developers using Jared's [MultiLoader Template][3] only deal with networking in the
common module. It is not limited to MultiLoader, it works in any multi-loader setup.

It also keeps your mod loader-agnostic on the wire: a Forge client can talk to a Fabric server,
a Fabric client can talk to a Paper server, and so on. You will still need your own handshake
packet if you care about gating.

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

// Paper (Bukkit plugin)
dependencies {
    compileOnly "mysticdrew:common-networking-paper:${version}"
}

// Common
dependencies {
    implementation "mysticdrew:common-networking-common:${version}"
}
```

### Paper module
The Paper backend lets a Bukkit plugin participate in the same packet protocol that the modded
loaders use, so a Fabric or NeoForge client can talk to a Paper server using the packets you
register through `Network.registerPacket(...)`.

Caveats specific to Paper:
- Server-side only. `sendToServer`, raw `send(packet, Connection)`, and `getRaw*Packet(...)`
  throw `UnsupportedOperationException`. Use `sendToClient(packet, serverPlayer)` or one of the
  multi-player variants on `Dispatcher`.
- PLAY-phase packets only. Configuration-phase packets are not supported because Bukkit's plugin
  messaging API does not expose the configuration phase.
- Your plugin must extend `JavaPlugin` and construct a `PaperNetworkHandler(this, Side.SERVER)`
  in `onEnable()`, then hand it to `new CommonNetworkMod(handler)`. Call `handler.shutdown()` in
  `onDisable()` to unregister channels. See
  [`CommonNetworkPaper`](paper/src/main/java/commonnetwork/CommonNetworkPaper.java).

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
- Paper entry: [`paper/src/testmod/java/example/paper/ExampleModPaper.java`](paper/src/testmod/java/example/paper/ExampleModPaper.java)

The example mod is compiled by each loader's `compileTestmodJava` task and packaged into a
`*-testmod.jar` by `:fabric:testmodJar`, `:forge:testmodJar`, `:neoforge:testmodJar`, and
`:paper:testmodJar`. It is not published to maven; it exists only so the library is verified
against a real consumer and so the IDE run configurations can launch a client/server that exercises
the API end-to-end. The Paper testmod ships server-only equivalents of the shared example packets
(see [`paper/src/testmod/java/example/paper/network/`](paper/src/testmod/java/example/paper/network/))
because the shared classes reference client-only Minecraft classes that are not available on a
Paper devbundle.

To launch a Paper test server with both the library and the example plugin, run
`:paper:runDevBundleServer`. The task uses Paper's bundled dev paperclip so it works even when the
target Minecraft version is not yet a published Paper release.

## Contributing
Pull Requests are welcome and encouraged.

### Working with the source
* IntelliJ IDEA
* JDK 25

### IntelliJ setup
Common Networking uses the [MultiLoader Template][3] pattern for sharing Fabric/Forge/NeoForge/Paper
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
