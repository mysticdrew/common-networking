package example.paper;

import example.ExampleModCommon;
import net.minecraft.server.level.ServerPlayer;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class ExampleModPaper extends JavaPlugin implements Listener
{
    private ExampleModCommon commonMod;

    @Override
    public void onEnable()
    {
        this.commonMod = new ExampleModCommon();
        getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event)
    {
        ServerPlayer player = ((CraftPlayer) event.getPlayer()).getHandle();
        commonMod.onPlayerJoinServer(player);
    }
}
