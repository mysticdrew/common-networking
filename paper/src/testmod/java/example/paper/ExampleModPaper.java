package example.paper;

import commonnetwork.api.Network;
import example.paper.network.PaperExamplePacketRegistration;
import example.paper.network.PaperExamplePacketTwo;
import net.minecraft.server.level.ServerPlayer;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class ExampleModPaper extends JavaPlugin implements Listener
{
    @Override
    public void onEnable()
    {
        System.out.println("PAPER ENABLE");
        new PaperExamplePacketRegistration().init();
        getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event)
    {
        System.out.println("PLAYER JOIN" + event.getPlayer().getName());
        ServerPlayer player = ((CraftPlayer) event.getPlayer()).getHandle();
        Network.getNetworkHandler().sendToClient(new PaperExamplePacketTwo(), player, true);
    }
}
